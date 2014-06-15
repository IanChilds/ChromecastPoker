package com.pokercast.castutils;

import android.util.Log;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Sends and receives messages from Chromecast
 */
public class PokerCastChannel implements Cast.MessageReceivedCallback {
    public static final String TAG = "PokerCastChannel";

    private OnGameStateUpdatedListener mOnGameStateUpdatedListener;

    public String getNamespace() {
        return "urn:x-cast:com.pokercast.custom";
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
        Log.d(TAG, "onMessageReceived: " + message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            String type = jsonObject.getString("type");
            if (type == "game_state") {
                updateGameState(message);
            }
        } catch (JSONException jsonException) {

        }
    }

    private void updateGameState(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);

            if (mOnGameStateUpdatedListener != null) {
                mOnGameStateUpdatedListener.onGameStateUpdated();
            }
        } catch (JSONException jsonException) {

        }
    }

    public void setOnGameStateUpdatedListener(OnGameStateUpdatedListener onGameStateUpdatedListener) {
        mOnGameStateUpdatedListener = onGameStateUpdatedListener;
    }

    public interface OnGameStateUpdatedListener {
        public void onGameStateUpdated();
    }
}
