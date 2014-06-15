package com.pokercast.game;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Captures the state of a player at any given point.
 *
 * It's not possible to update the player state, as it is a snapshot. If the player state changes, then create a new
 * snapshot.
 *
 * Snapshots can only be created from JSON messages. That means that the client can only have the same idea of the player state
 * as the server.
 */
public class PlayerStateSnapshot {
    public static final String TAG = "PlayerStateSnapshot";
    public static final String NAME = "total_cash";
    public static final String TOTAL_CASH = "total_cash";
    public static final String CASH_LAID_THIS_HAND = "cash_laid_this_hand";
    public static final String CASH_LAID_THIS_BETTING_ROUND = "cash_laid_this_betting_round";
    public static final String FOLDED = "mFolded";

    private String mName;
    private int mTotalCash;
    private int mCashLaidThisHand;
    private int mCashLaidThisBettingRound;
    private boolean mFolded;

    private PlayerStateSnapshot() {
    }

    public String getName() {
        return mName;
    }

    public int getTotalCash() {
        return mTotalCash;
    }

    public int getCashLaidThisHand() {
        return mCashLaidThisHand;
    }

    public int getCashLaidThisBettingRound() {
        return mCashLaidThisBettingRound;
    }

    public boolean hasFolded() {
        return mFolded;
    }

    public JSONObject toJSON() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(NAME, getName());
            jsonObject.put(TOTAL_CASH, getTotalCash());
            jsonObject.put(CASH_LAID_THIS_HAND, getCashLaidThisHand());
            jsonObject.put(CASH_LAID_THIS_BETTING_ROUND, getCashLaidThisBettingRound());
            jsonObject.put(FOLDED, hasFolded());
            return jsonObject;

        } catch (JSONException jsonException) {
            Log.e(TAG, jsonException.toString());
            return new JSONObject();
        }
    }

    public static PlayerStateSnapshot fromJSONString (String JSONMessage) {
        try {
            JSONObject jsonObject = new JSONObject(JSONMessage);
            PlayerStateSnapshot player = new PlayerStateSnapshot();
            player.mName = jsonObject.getString(NAME);
            player.mTotalCash = jsonObject.getInt(TOTAL_CASH);
            player.mCashLaidThisHand = jsonObject.getInt(CASH_LAID_THIS_HAND);
            player.mCashLaidThisBettingRound = jsonObject.getInt(CASH_LAID_THIS_BETTING_ROUND);
            player.mFolded = jsonObject.getBoolean(FOLDED);
            return player;
        } catch (JSONException jsonException) {
            Log.e(TAG, jsonException.toString());
            return new PlayerStateSnapshot();
        }
    }
}
