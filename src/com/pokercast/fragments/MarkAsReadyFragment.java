package com.pokercast.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pokercast.R;
import com.pokercast.activities.GamePlayActivity;
import com.pokercast.activities.MarkAsReadyActivity;

/**
 *
 */
public class MarkAsReadyFragment extends Fragment {
    private Button mButton;

    public TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mark_as_ready, container, false);
        Intent intent = getActivity().getIntent();
        String user_name = intent.getStringExtra(JoinFragment.EXTRA_MESSAGE);
        mTextView = (TextView) v.findViewById(R.id.ready_text);
        mTextView.setText("Hello " + user_name + ", welcome to the game. Press 'Ready' to indicate that you are ready to start playing. Once all players click 'Ready', the game will begin.");
        mButton = (Button) v.findViewById(R.id.ready_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGamePlay(view);
            }
        });
        return v;
    }

    public void goToGamePlay(View view) {
        Intent intent = new Intent(getActivity(), GamePlayActivity.class);
        startActivity(intent);
    }
}