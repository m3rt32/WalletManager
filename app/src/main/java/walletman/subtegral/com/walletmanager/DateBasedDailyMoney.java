package walletman.subtegral.com.walletmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mert on 15.01.2018.
 */

public class DateBasedDailyMoney {
    private Date date;
    private double price;
    private String place;
    private boolean isTransaction;
    private Integer id;
    public DateBasedDailyMoney(Date date,long price){
        this.date = date;
        this.price = price;
    }
    public DateBasedDailyMoney(Date date,Double price,String place,boolean isTransaction,Integer id) {
        this.date = date;
        this.price = price;
        this.place = place;
        this.isTransaction = isTransaction;
        this.id = id;
    }
    public Date getDate(){
        return date;
    }
    public double getPrice(){
        return (double) price;
    }
    public String getPlace(){ return place;}
    public boolean getTransactionStatus(){return isTransaction;}
    public Integer getId(){return id;}
    public MoneyListItem getListItemCasting(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - dd MMMM YYYY");
        return  new MoneyListItem(place,Double.toString(price),dateFormat.format(date),isTransaction);
    }
}
