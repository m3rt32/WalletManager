package walletman.subtegral.com.walletmanager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mert on 15.01.2018.
 */

public class MoneyListItem {
    private String place;
    private String price;
    private String date;
    private boolean transactionStatus;
    public MoneyListItem(String place,String price,String date,boolean transactionStatus) {
        this.place = place;
        this.price = price;
        this.date = date;
        this.transactionStatus = transactionStatus;
    }
    public String getPlace(){
        return place;
    }
    public String getPrice(){
        return price;
    }
    public String getDate(){
        return date;
    }
    public boolean getTransactionStatus(){return transactionStatus;}
}
