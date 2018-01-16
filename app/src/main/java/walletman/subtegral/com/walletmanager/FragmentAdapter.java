package walletman.subtegral.com.walletmanager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mert on 13.01.2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragments = new ArrayList<>();
    public List<String> title = new ArrayList<>();

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addItem(Fragment fragment, String name) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
            title.add(name);
        }
    }
}
