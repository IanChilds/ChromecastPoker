package com.pokercast.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.MediaRouteButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pokercast.R;
import com.pokercast.activities.JoinActivity;
import com.pokercast.castutils.CastManager;

/**
 *
 */
public class ConnectFragment extends Fragment implements CastManager.OnApplicationConnectedListener {
    public static final String TAG = "ConnectFragment";

    private CastManager mCastManager;
    private MediaRouteButton mMediaRouteButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCastManager = CastManager.createInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_connect, container, false);
        mMediaRouteButton = (MediaRouteButton) v.findViewById(R.id.media_route_button);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCastManager.startDiscoveringDevices(mMediaRouteButton);
        mCastManager.setOnApplicationConnectedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCastManager.stopDiscoveringDevices();
        mCastManager.setOnApplicationConnectedListener(null);
    }

    public void onApplicationConnected() {
        Intent intent = new Intent(getActivity(), JoinActivity.class);
        startActivity(intent);
    }
}