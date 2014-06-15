package com.pokercast.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.pokercast.R;
import com.pokercast.fragments.GamePlayFragment;

/**
 * Activity that forms the majority of the processing for the app. Shows the user their cards, and displays the
 * current state of the game. If it is the user's turn, then gives them options to decide what to do.
 */
public class GamePlayActivity extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new GamePlayFragment();
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        }
    }
}