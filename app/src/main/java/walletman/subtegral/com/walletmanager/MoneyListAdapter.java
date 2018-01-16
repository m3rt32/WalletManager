package walletman.subtegral.com.walletmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mert on 15.01.2018.
 */

public class MoneyListAdapter extends ArrayAdapter<MoneyListItem> {

    public MoneyListAdapter(@NonNull Context context, int resource, @NonNull List<MoneyListItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.moneylistitem ,parent,false);
        MoneyListItem moneyListItem = getItem(position);
        TextView place = v.findViewById(R.id.moneylist_place);
        TextView price = v.findViewById(R.id.moneylist_price);
        TextView date = v.findViewById(R.id.moneylist_date);
        place.setText(moneyListItem.getPlace());
        price.setText(moneyListItem.getPrice() + " TL");
        date.setText(moneyListItem.getDate());
        return v;
    }
}
