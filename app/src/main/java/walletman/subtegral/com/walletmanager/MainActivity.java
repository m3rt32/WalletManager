package walletman.subtegral.com.walletmanager;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.TableLayout;


public class MainActivity extends AppCompatActivity {
    public EntryStaticsFragment entryStaticsFragment;
    public IncomeFragment incomeFragment;
    public OutComeFragment outComeFragment;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private FragmentAdapter fragmentAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViewPager();
    }

    private void setViewPager() {
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        outComeFragment = new OutComeFragment();
        outComeFragment.setContext(MainActivity.this);
        incomeFragment = new IncomeFragment();
        entryStaticsFragment = new EntryStaticsFragment();
        //Fragment communication
        outComeFragment.entryStaticsFragment = entryStaticsFragment;
        incomeFragment.entryStaticsFragment = entryStaticsFragment;
        entryStaticsFragment.outComeFragment = outComeFragment;
        entryStaticsFragment.incomeFragment = incomeFragment;
        //Fragment adapting
        fragmentAdapter.addItem(outComeFragment,"OUTCOME");
        fragmentAdapter.addItem(incomeFragment,"INCOME");
        fragmentAdapter.addItem(entryStaticsFragment,"STATICS");
        viewPager.setAdapter(fragmentAdapter);
        tabLayout  = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

}
