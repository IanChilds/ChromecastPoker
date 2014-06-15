package com.pokercast.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pokercast.R;
import com.pokercast.activities.MarkAsReadyActivity;
import com.pokercast.castutils.CastManager;

/**
 *
 */
public class JoinFragment extends Fragment {
    public final static String TAG = "JoinFragment";
    public final static String USER_NAME_EXTRA = "com.pokercast.username";

    private EditText mEditText;
    private Button mButton;
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
        View v = inflater.inflate(R.layout.fragment_join, container, false);
        mEditText = (EditText) v.findViewById(R.id.user_name);
        mButton = (Button) v.findViewById(R.id.join_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJoinButtonClicked(view);
            }
        });
        return v;
    }

    public void onJoinButtonClicked(View view) {
        if (mEditText.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "Please enter a valid username", Toast.LENGTH_LONG).show();
        } else {
            String userName = mEditText.getText().toString();
            sendJoinMessage(userName); // Actually should maybe wait for callback to do this.
            Intent intent = new Intent(getActivity(), MarkAsReadyActivity.class);
            intent.putExtra(USER_NAME_EXTRA, userName);
            startActivity(intent);
        }
    }

    public void sendJoinMessage(String userName) {
        Log.i(TAG, "Sending join message");
        mCastManager.sendJoinMessage(userName);
    }
}