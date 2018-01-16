package walletman.subtegral.com.walletmanager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Mert on 13.01.2018.
 */

public class OutComeFragment extends Fragment implements IPopulator{

    private Button button;
    private EditText place;
    private EditText price;
    private CheckBox checkBox;
    private Context context;
    private DatabaseHelper helper;
    private ListView listView;
    public EntryStaticsFragment entryStaticsFragment;
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {
        return layoutInflater.inflate(R.layout.outcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.add_button);
        place = view.findViewById(R.id.place);
        price = view.findViewById(R.id.price);
        listView = view.findViewById(R.id.last_outcome);
        checkBox = view.findViewById(R.id.transaction);
        helper = new DatabaseHelper(getContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.addItem(price.getText().toString(), place.getText().toString(), checkBox.isChecked(), false);
                populateListView();
                price.getText().clear();
                place.getText().clear();
                checkBox.setChecked(false);
                if(entryStaticsFragment!=null)
                    entryStaticsFragment.populateListView();
            }
        });
        populateListView();
    }

    public void populateListView() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 01);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        long targetTime = calendar.getTimeInMillis();
        List<String> dataset = helper.getItemsWithSize(10, false);
        final List<MoneyListItem> listItems = new ArrayList<>();
        final List<Integer> ids = new ArrayList<Integer>();
        for (String data : dataset) {
            String[] dataBlock = data.split("-");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(Long.parseLong(dataBlock[3]));
            Date date = calendar1.getTime();
            SimpleDateFormat format = new SimpleDateFormat("EEEE - dd MMMM YYYY");
            listItems.add(new MoneyListItem(dataBlock[2], dataBlock[1],format.format(date),dataBlock[4]=="1"));
            ids.add(Integer.parseInt(dataBlock[0]));
        }
        MoneyListAdapter outComeListAdapter = new MoneyListAdapter(getContext(), R.id.last_outcome, listItems);
        listView.setAdapter(outComeListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                        .setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ChangeDialog().ShowWithMoneyList(getContext(), listItems, ids, position, helper,
                                       entryStaticsFragment,entryStaticsFragment.incomeFragment,entryStaticsFragment.outComeFragment);
                            }
                        })
                        .setNeutralButton("REMOVE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               if( helper.deleteItem(ids.get(position))>0)
                                   Toast.makeText(getContext(),"DELETED",Toast.LENGTH_SHORT).show();
                                populateListView();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("Commiting data alter")
                        .setMessage("Select an action to alter this entry.");
                alertDialog.show();
            }
        });
    }


}

