package in.njyjust.e2s2.myeahub;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

    MainPagerAdapter mainPagerAdapter;
    ViewPager mViewPager;

    //Bundle bundle = new Bundle();
    //Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.settings);
    //View v = fragment.getView();

    //Fragment fragobj = new Fragment();

    //String username, password;

    //EditText usernameValue = (EditText) v.findViewById(R.id.username_input);
    //EditText passwordValue = (EditText) v.findViewById(R.id.password_input);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mainPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

    }

    /*public void onButtonPressed (View view)
    {
        if (!String.valueOf(usernameValue.getText()).isEmpty()
                && !String.valueOf(passwordValue.getText()).isEmpty())
        {
            bundle.putString(username, String.valueOf(usernameValue.getText()));
            bundle.putString(password, String.valueOf(passwordValue.getText()));
            fragobj.setArguments(bundle);


        } else {
            bundle.putString(username, "jeffrey");
            bundle.putString(password, "mypass");
            fragobj.setArguments(bundle);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}