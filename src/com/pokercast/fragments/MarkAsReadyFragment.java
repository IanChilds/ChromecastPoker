package com.pokercast.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.pokercast.R;
import com.pokercast.activities.GamePlayActivity;
import com.pokercast.castutils.CastManager;

/**
 *
 */
public class MarkAsReadyFragment extends Fragment {
    public static final String TAG = "MarkAsReadyFragment";

    private Button mButton;
    private TextView mTextView;
    private CastManager mCastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCastManager = CastManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mark_as_ready, container, false);
        Intent intent = getActivity().getIntent();
        final String userName = intent.getStringExtra(JoinFragment.USER_NAME_EXTRA);
        mTextView = (TextView) v.findViewById(R.id.ready_text);
        mTextView.setText("Hello " + userName + ", welcome to the game. Press 'Ready' to indicate that you are ready to start playing. Once all players click 'Ready', the game will begin.");
        mButton = (Button) v.findViewById(R.id.ready_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGamePlay(userName);
            }
        });
        return v;
    }

    public void goToGamePlay(String userName) {
        sendReadyMessage(userName);
        Intent intent = new Intent(getActivity(), GamePlayActivity.class);
        startActivity(intent);
    }

    public void sendReadyMessage(String userName) {
        Log.i(TAG, "Sending ready message");
        mCastManager.sendReadyMessage(userName);
    }

}