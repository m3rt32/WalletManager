package walletman.subtegral.com.walletmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class IncomeFragment extends Fragment implements  IPopulator {
    private EditText place;
    private EditText price;
    private ListView listView;
    private Button addButton;
    private CheckBox bankTransactionStatus;
    private DatabaseHelper helper;
    public EntryStaticsFragment entryStaticsFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        place = view.findViewById(R.id.income_place);
        price = view.findViewById(R.id.income_price);
        listView = view.findViewById(R.id.last_income);
        addButton = view.findViewById(R.id.add_button_income);
        bankTransactionStatus = view.findViewById(R.id.transaction_income);
        helper = new DatabaseHelper(getContext());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.addItem(price.getText().toString(), place.getText().toString(), bankTransactionStatus.isChecked(), true);
                populateListView();
                price.getText().clear();
                place.getText().clear();
                bankTransactionStatus.setChecked(false);
                if (entryStaticsFragment != null)
                    entryStaticsFragment.populateListView();
            }
        });
        populateListView();
    }

    public void populateListView() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 01);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        List<String> lastestItems = helper.getItemsWithSize(10, true);
        final List<MoneyListItem> listItems = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - dd MMMM YYYY");

        final List<Integer> ids = new ArrayList<>();
        for (String item : lastestItems) {
            String[] dataBlocks = item.split("-");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(Long.parseLong(dataBlocks[3]));
            Date date = calendar1.getTime();
            listItems.add(new MoneyListItem(dataBlocks[2], dataBlocks[1], dateFormat.format(date), dataBlocks[4] == "1"));
            ids.add(Integer.parseInt(dataBlocks[0]));
        }
        MoneyListAdapter adapter = new MoneyListAdapter(getContext(), R.id.last_income, listItems);
        listView.setAdapter(adapter);
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
                                System.out.println("CHANGE ACTION");
                                if (helper.deleteItem(ids.get(position)) > 0)
                                    Toast.makeText(getContext(), "DELETED", Toast.LENGTH_SHORT).show();
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

