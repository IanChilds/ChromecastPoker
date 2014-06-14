package com.pokercast.castutils;

import android.util.Log;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

/**
 * Sends and receives messages from Chromecast
 */
public class PokerCastChannel implements Cast.MessageReceivedCallback {
    public static final String TAG = "PokerCastChannel";

    public String getNamespace() {
        return "urn:x-cast:com.pokercast.custom";
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
        Log.d(TAG, "onMessageReceived: " + message);
    }
}
