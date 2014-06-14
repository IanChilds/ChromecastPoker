package com.pokercast.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.pokercast.R;
import com.pokercast.castutils.CastManager;

/**
 *
 */
public class ConnectFragment extends Fragment {
    public static final String TAG = "ConnectFragment";
    private CastManager mCastManager;
    private MediaRouteButton mMediaRouteButton;
    private Button mSendMessageButton;
    private Button mReadyMessageButton;

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
        mSendMessageButton = (Button) v.findViewById(R.id.send_message_button);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendJoinMessage();
            }
        });
        mReadyMessageButton = (Button) v.findViewById(R.id.ready_message_button);
        mReadyMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReadyMessage();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCastManager.startDiscoveringDevices(mMediaRouteButton);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCastManager.stopDiscoveringDevices();
    }

    public void sendJoinMessage() {
        Log.i(TAG, "Sending test message");
        mCastManager.sendJoinMessage("Binnie");
    }

    public void sendReadyMessage() {
        Log.i(TAG, "Sending ready message");
        mCastManager.sendReadyMessage("Binnie");
    }
}