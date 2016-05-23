package in.njyjust.e2s2.myeahub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by justin on 22/4/15.
 */
public class Settings extends Fragment {

    public Settings() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.settings, container, false);

        return v;
    }
}