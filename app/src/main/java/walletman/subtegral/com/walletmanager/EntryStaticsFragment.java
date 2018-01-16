package walletman.subtegral.com.walletmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mert on 13.01.2018.
 */

public class EntryStaticsFragment extends Fragment implements IPopulator{
    private DatabaseHelper helper;
    private ListView listView;
    private TextView totalOutcome;
    private TextView totalIncome;
    public IncomeFragment incomeFragment;
    public OutComeFragment outComeFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.entrystatics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new DatabaseHelper(getContext());
        listView = view.findViewById(R.id.totalList);
        totalOutcome = view.findViewById(R.id.statics_outcome);
        totalIncome = view.findViewById(R.id.statics_income);
        populateListView();
    }

    public void populateListView() {
        if(helper==null) //Fixes the bug that calling this method before fragment view "actually" created. Otherwise helper not initalized.
            return;
        List<DateBasedDailyMoney> entries = new ArrayList<>();
        entries = helper.getTotalData();
        final List<MoneyListItem> listItems = new ArrayList<>();
        final List<Integer> ids = new ArrayList<>();
        for(DateBasedDailyMoney entry:entries){
            listItems.add(entry.getListItemCasting());
            ids.add(entry.getId());
        }
        MoneyListAdapter moneyListAdapter = new MoneyListAdapter(getContext(),R.id.totalList,listItems);
        listView.setAdapter(moneyListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ChangeDialog().ShowWithMoneyList(getContext(), listItems, ids, position, helper,
                                 incomeFragment.entryStaticsFragment, incomeFragment, outComeFragment);

                        }
                        })
                        .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(helper.deleteItem(ids.get(position))>0){
                                    populateListView();
                                    incomeFragment.populateListView();
                                    outComeFragment.populateListView();
                                    Toast.makeText(getContext(),"DELETED",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setTitle("Altering Commitment")
                        .setMessage("Choose the action for altering the data")
                        .show();
            }
        });
        long outcome = helper.getTotalMoney(false);
        totalOutcome.setText(Long.toString(outcome) + " TL");
        totalIncome.setText(Long.toString(helper.getTotalMoney(true)) + " TL");
    }
}
