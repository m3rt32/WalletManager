package walletman.subtegral.com.walletmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Mert on 14.01.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "wallet";
    public final String TABLE_NAME = "money";
    public final String PLACE = "place";
    public final String PRICE = "place";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, price TEXT NOT NULL, place TEXT NOT NULL,timestamp INTEGER," +
                "bank_withdraw INTEGER,isIncome TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addItem(String price, String place, boolean withdraw, boolean isIncome) {
        SQLiteDatabase db = this.getWritableDatabase();
        Date date = new Date();
        //date.setYear(Calendar.getInstance().get(Calendar.YEAR)-1900);
        ContentValues cv = new ContentValues();
        cv.put("price", price);
        cv.put("place", place);
        cv.put("timestamp", Long.toString(date.getTime()));
        cv.put("bank_withdraw", withdraw ? 1 : 0);
        cv.put("isIncome", isIncome ? "1" : "0");
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id=?", new String[]{id.toString()});
    }

    public Integer updateItem(String place, String price, boolean transaction, Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("place", place);
        cv.put("price",price);
        System.out.println("PRICE:" + price);
        cv.put("bank_withdraw", transaction);
        return db.update(TABLE_NAME, cv, "id=?", new String[]{id.toString()});
    }

    public List<String> getItemsWithSize(Integer size, boolean isIncome) {

        List<String> strings = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (!isIncome) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE isIncome='0' ORDER BY timestamp DESC lIMIT " + size, null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE isIncome='1' ORDER BY timestamp DESC LIMIT " + size, null);
        }
        while (cursor.moveToNext()) {
            strings.add(cursor.getString(0) + "-"
                    + cursor.getString(1) + "-"
                    + cursor.getString(2) + "-"
                    + cursor.getString(3) + "-"
                    +cursor.getString(4));
        }
        return strings;
    }

    public List<DateBasedDailyMoney> getTotalData() {
        List<DateBasedDailyMoney> dateBasedDailyMonies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY timestamp DESC", null);
        while (cursor.moveToNext()) {
            Date date = new Date();
            date.setTime(Long.parseLong(cursor.getString(3)));
            dateBasedDailyMonies.add(new DateBasedDailyMoney(date, cursor.getDouble(1), cursor.getString(2), cursor.getInt(4) == 1
                    , cursor.getInt(0)));
        }
        return dateBasedDailyMonies;
    }

    public long getTotalMoney(boolean isIncome) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE isIncome=?", new String[]{isIncome ? "1" : "0"});
        long money = 0;
        while (cursor.moveToNext()) {
            money += cursor.getLong(1);
        }
        return money;
    }

    public List<DateBasedDailyMoney> getMonthlyMoneyByDays(int month) {
        List<DateBasedDailyMoney> dateBasedDailyMonies = new ArrayList<>();
        Calendar calendarBegin = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        calendarBegin.set(Calendar.MONTH, month);
        calendarBegin.set(Calendar.HOUR_OF_DAY, 0);
        calendarBegin.set(Calendar.MINUTE, 0);
        calendarBegin.set(Calendar.SECOND, 0);

        calendarEnd.set(Calendar.MONTH, month);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 0);
        calendarEnd.set(Calendar.SECOND, 0);
        int todaysDay = today.get(Calendar.DAY_OF_MONTH);
        int startDay = todaysDay - 7; //Day <7 state fix REQUIRED!
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = startDay; i <= todaysDay; i++) {
            int j = i;
            calendarEnd.set(Calendar.DAY_OF_MONTH, j);
            calendarBegin.set(Calendar.DAY_OF_MONTH, j);
            long dateBegin = calendarBegin.getTimeInMillis();
            long dateEnd = calendarEnd.getTimeInMillis();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            long money = 0;
            SimpleDateFormat format = new SimpleDateFormat("dd MM YYYY");

            while (cursor.moveToNext()) {
                System.out.println(cursor.getString(2));
                // System.out.println(cursor.getString(3));
                Long dat = Long.parseLong(cursor.getString(3));
                if (dat < dateEnd && dat > dateBegin)
                    System.out.println("HERE");
                money += Long.parseLong(cursor.getString(1));
            }
            if (money > 0) {
                Date date = new Date();
                date.setTime(dateBegin);
                dateBasedDailyMonies.add(new DateBasedDailyMoney(date, money));
            }
        }
        return dateBasedDailyMonies;
    }
}
