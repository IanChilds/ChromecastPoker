package com.pokercast.castutils;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

/**
 * Sends and receives messages from Chromecast
 */
public class PokerCastChannel implements Cast.MessageReceivedCallback {
    @Override
    public void onMessageReceived(CastDevice castDevice, String s, String s2) {
    }
}
