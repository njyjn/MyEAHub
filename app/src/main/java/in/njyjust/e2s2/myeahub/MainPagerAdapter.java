package in.njyjust.e2s2.myeahub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by justin on 22/4/15.
 */
// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Location 1", "Location 2", "Fusion", "About"};

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new Location1v2();
            case 1:
                return new Location2v2();
            case 2:
                return new Fusionv2();
            case 3:
                return new About();
        }

        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
