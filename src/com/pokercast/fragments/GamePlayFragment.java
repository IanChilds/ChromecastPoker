package com.pokercast.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.pokercast.R;
import com.pokercast.castutils.CastManager;
import com.pokercast.castutils.PokerCastChannel;
import com.pokercast.game.Game;

public class GamePlayFragment extends Fragment implements PokerCastChannel.OnGameStateUpdatedListener {
    public static final String TAG = "GamePlayFragment";
    private CastManager mCastManager;
    private Game mGame;

    private TextView mCashTextView;
    private ImageView mCardOne;
    private ImageView mCardTwo;
    private Button mShowOrHideCards;
    private TextView mGameState;
    private Button mOptionOne;
    private Button mOptionTwo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCastManager = CastManager.getInstance();
        mGame = Game.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_play, container, false);
        mCardOne = (ImageView) v.findViewById(R.id.card_one_image);
        mCardTwo = (ImageView) v.findViewById(R.id.card_two_image);
        mShowOrHideCards = (Button) v.findViewById(R.id.show_or_hide_button);
        mGameState = (TextView) v.findViewById(R.id.game_state);
        mOptionOne = (Button) v.findViewById(R.id.option_one_button);
        mOptionTwo = (Button) v.findViewById(R.id.option_two_button);
        mCastManager.getPokerCastChannel().setOnGameStateUpdatedListener(this);

        return v;
    }

    public void onGameStateUpdated() {
        String lastMove = mGame.getLastMove();

    }

}
