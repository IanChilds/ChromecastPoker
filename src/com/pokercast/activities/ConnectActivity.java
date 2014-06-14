package com.pokercast.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.pokercast.R;
import com.pokercast.fragments.ConnectFragment;

/**
 * Allows the user to choose a Chromecast to connect to.
 */

public class ConnectActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new ConnectFragment();
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        }
    }
}
