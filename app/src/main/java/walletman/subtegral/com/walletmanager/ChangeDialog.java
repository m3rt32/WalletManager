package walletman.subtegral.com.walletmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mert on 16.01.2018.
 */

public final class ChangeDialog{
    public void ShowWithMoneyList(final Context context, List<MoneyListItem> entries,final List<Integer> ids,final int position, final DatabaseHelper helper, final EntryStaticsFragment entryStaticsFragment,
                                  final IncomeFragment incomeFragment,final OutComeFragment outComeFragment){
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText placeText = new EditText(context);
        final EditText priceText = new EditText(context);
        final CheckBox transactionCheckBox = new CheckBox(context);
        placeText.setHint("Place");
        placeText.setText(entries.get(position).getPlace());
        priceText.setHint("Price");
        priceText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        priceText.setText(entries.get(position).getPrice());
        transactionCheckBox.setText("Bank Transaction");
        transactionCheckBox.setChecked(entries.get(position).getTransactionStatus());
        layout.addView(placeText);
        layout.addView(priceText);
        layout.addView(transactionCheckBox);
        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(context);
        alertDialog1.setView(layout);
        alertDialog1.setTitle("Change data");
        alertDialog1.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( helper.updateItem(placeText.getText().toString(),priceText.getText().toString(),transactionCheckBox.isChecked(),ids.get(position))>0){
                    Toast.makeText(context,"CHANGE SUCCESS",Toast.LENGTH_SHORT).show();
                    incomeFragment.populateListView();
                    outComeFragment.populateListView();
                }
            }
        }).setNegativeButton("CANCEL",null);
        alertDialog1.show();
    }
}
