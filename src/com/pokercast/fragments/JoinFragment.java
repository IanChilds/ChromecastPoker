package com.pokercast.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.pokercast.R;
import com.pokercast.activities.MarkAsReadyActivity;

/**
 *
 */
public class JoinFragment extends Fragment {
    private Button mButton;

    public final static String EXTRA_MESSAGE = "com.example.ChromecastPoker";
    public EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                goToMarkAsReady(view);
            }
        });
        return v;
    }

    public void goToMarkAsReady(View view) {
        Intent intent = new Intent(getActivity(), MarkAsReadyActivity.class);
        String message = mEditText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}