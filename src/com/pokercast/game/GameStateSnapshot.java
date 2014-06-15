package com.pokercast.game;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains all information about a game at any given point.
 */
public class GameStateSnapshot {
    public static final String TAG = "GameStateSnapshot";

    public static final String CARDS = "cards";
    public static final String DEALER = "dealer";
    public static final String LITTLE_BLIND_POSITION = "little_blind_position";
    public static final String BIG_BLIND_POSITION = "big_blind_position";
    public static final String BIG_BLIND_VALUE = "big_blind_value";
    public static final String SMALLEST_POSSIBLE_RAISE = "smallest_possible_raise";
    public static final String TURN = "turn";
    public static final String NUM_PLAYERS = "num_players";
    public static final String PLAYERS = "players";

    private List<Integer> mCards = new ArrayList<Integer>(5);
    private int mDealer;
    private int mLittleBlindPosition;
    private int mBigBlindPosition;
    private int mBigBlindValue;
    private int mSmallestPossibleRaise;
    private int mTurn;
    private int mNumPlayers;
    private List<PlayerStateSnapshot> mPlayers;

    private GameStateSnapshot() {

    }

    public int getCard(int index) {
        return mCards.get(index);
    }

    public int getDealer() {
        return mDealer;
    }

    public int getLittleBlindPosition() {
        return mLittleBlindPosition;
    }

    public int getBigBlindPosition() {
        return mBigBlindPosition;
    }

    public int getBigBlindValue() {
        return mBigBlindValue;
    }

    public int getSmallestPossibleRaise() {
        return mSmallestPossibleRaise;
    }

    public int getTurn() {
        return mTurn;
    }

    public int getNumPlayers() {
        return mNumPlayers;
    }

    public PlayerStateSnapshot getPlayer(int index) {
        return mPlayers.get(index);
    }

    public String toJSONString() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArrayCards = new JSONArray();
            for (Integer card : mCards) {
                jsonArrayCards.put(card);
            }
            jsonObject.put(CARDS, jsonArrayCards);
            jsonObject.put(DEALER, mDealer);
            jsonObject.put(LITTLE_BLIND_POSITION, mLittleBlindPosition);
            jsonObject.put(BIG_BLIND_POSITION, mBigBlindPosition);
            jsonObject.put(BIG_BLIND_VALUE, mBigBlindValue);
            jsonObject.put(SMALLEST_POSSIBLE_RAISE, mSmallestPossibleRaise);
            jsonObject.put(TURN, mTurn);
            jsonObject.put(NUM_PLAYERS, mNumPlayers);
            JSONArray jsonArrayPlayers = new JSONArray();
            for (PlayerStateSnapshot playerStateSnapshot : mPlayers) {
                jsonArrayPlayers.put(playerStateSnapshot.toJSON());
            }
            jsonObject.put(PLAYERS, jsonArrayPlayers);
            return jsonObject.toString();

        } catch (JSONException jsonException) {
            return "";
        }
    }

    public static GameStateSnapshot fromJSONString(String JSONMessage) {
        try {
            JSONObject jsonObject = new JSONObject(JSONMessage);
            GameStateSnapshot gameStateSnapshot = new GameStateSnapshot();
            JSONArray jsonArrayCards = jsonObject.getJSONArray(CARDS);
            for (int ii = 0; ii < jsonArrayCards.length(); ii++) {
                gameStateSnapshot.mCards.add(jsonArrayCards.getInt(ii));
            }
            gameStateSnapshot.mDealer = jsonObject.getInt(DEALER);
            gameStateSnapshot.mLittleBlindPosition = jsonObject.getInt(LITTLE_BLIND_POSITION);
            gameStateSnapshot.mBigBlindPosition = jsonObject.getInt(BIG_BLIND_POSITION);
            gameStateSnapshot.mBigBlindValue = jsonObject.getInt(BIG_BLIND_VALUE);
            gameStateSnapshot.mSmallestPossibleRaise = jsonObject.getInt(SMALLEST_POSSIBLE_RAISE);
            gameStateSnapshot.mTurn = jsonObject.getInt(TURN);
            gameStateSnapshot.mNumPlayers = jsonObject.getInt(NUM_PLAYERS);
            JSONArray jsonArrayPlayers = jsonObject.getJSONArray(PLAYERS);
            for (int ii = 0; ii < gameStateSnapshot.mNumPlayers; ii++) {
                gameStateSnapshot.mPlayers.add(PlayerStateSnapshot.fromJSONString(jsonArrayPlayers.getString(ii)));
            }
            return gameStateSnapshot;

        } catch (JSONException jsonException) {
            Log.e(TAG, jsonException.toString());
            return new GameStateSnapshot();
        }
    }
}
