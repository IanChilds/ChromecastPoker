package com.pokercast.castutils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Controls interaction between user's device and Chromecast.
 */
public class CastManager {
    public static final String TAG = "CastManager";
    public static final String APPLICATION_ID = "B525BDB6";//CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID;

    private static CastManager castManagerInstance = null;
    private final Context mApplicationContext;

    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouterCallback mMediaRouterCallback;
    private MediaRouteButton mMediaRouteButton;
    private int mRouteCount = 0;

    private CastDevice mSelectedDevice;
    private Cast.Listener mCastClientListener = new Cast.Listener() {
        @Override
        public void onApplicationStatusChanged() {
            if (mApiClient != null) {
                Log.d(TAG, "onApplicationStatusChanged: "
                    + Cast.CastApi.getApplicationStatus(mApiClient));
            }
        }

        @Override
        public void onVolumeChanged() {
            if (mApiClient != null) {
                Log.d(TAG, "onVolumeChanged: " + Cast.CastApi.getVolume(mApiClient));
            }
        }

        @Override
        public void onApplicationDisconnected(int errorCode) {
            teardown();
        }
    };
    private ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks();
    private ConnectionFailedListener mConnectionFailedListener = new ConnectionFailedListener();
    private boolean mWaitingForReconnect;
    private boolean mApplicationStarted = false;
    private OnApplicationConnectedListener mOnApplicationConnectedListener;

    private GoogleApiClient mApiClient;
    private PokerCastChannel mPokerCastChannel;
    private String mSessionId;

    protected CastManager(Context applicationContext) {
        mApplicationContext = applicationContext;
        mMediaRouter = MediaRouter.getInstance(mApplicationContext);
        mMediaRouteSelector = new MediaRouteSelector.Builder()
            .addControlCategory(CastMediaControlIntent.categoryForCast(APPLICATION_ID))
            .build();
        mMediaRouterCallback = new MediaRouterCallback();
    }

    public static CastManager createInstance(Context applicationContext) {
        if (castManagerInstance == null) {
            castManagerInstance = new CastManager(applicationContext);
        }
        return castManagerInstance;
    }

    public static CastManager getInstance() throws IllegalStateException {
        if (castManagerInstance == null) {
            throw new IllegalStateException("Should create instance before accessing it");
        } else {
            return castManagerInstance;
        }
    }

    private void setCastConnectButton(MediaRouteButton mediaRouteButton) {
        mMediaRouteButton = mediaRouteButton;
        mMediaRouteButton.setRouteSelector(mMediaRouteSelector);
    }

    public void startDiscoveringDevices(MediaRouteButton mediaRouteButton) {
        Log.i("Route", "start discovering devices");
        setCastConnectButton(mediaRouteButton);
        mMediaRouter.addCallback(
            mMediaRouteSelector,
            mMediaRouterCallback,
            MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    public void stopDiscoveringDevices() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    private void teardown() {
        Log.d(TAG, "teardown");
        if (mApiClient != null) {
            Log.d(TAG, "mApiClient is not null");
            if (mApplicationStarted) {
                Log.d(TAG, "Application has started");
                if (mApiClient.isConnected()) {
                    Log.d(TAG, "mApiClient is connected");
                    try {
                        Cast.CastApi.stopApplication(mApiClient, mSessionId);
                        if (mPokerCastChannel != null) {
                            Cast.CastApi.removeMessageReceivedCallbacks(
                                mApiClient,
                                mPokerCastChannel.getNamespace());
                            mPokerCastChannel = null;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception while removing channel", e);
                    }
                    mApiClient.disconnect();
                }
                mApplicationStarted = false;
            }
            mApiClient = null;
        }
        mSelectedDevice = null;
        mWaitingForReconnect = false;
        mSessionId = null;
    }

    private void createApiClient() {
        Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
            .builder(mSelectedDevice, mCastClientListener);

        mApiClient = new GoogleApiClient.Builder(mApplicationContext)
            .addApi(Cast.API, apiOptionsBuilder.build())
            .addConnectionCallbacks(mConnectionCallbacks)
            .addOnConnectionFailedListener(mConnectionFailedListener)
            .build();

        mApiClient.connect();
    }

    private void connectApiClient() {
        if (mApiClient != null) {
            mApiClient.connect();
        }
    }

    private void sendMessage(String message) {
        if (mApiClient != null && mPokerCastChannel != null) {
            try {
                Cast.CastApi.sendMessage(mApiClient, mPokerCastChannel.getNamespace(), message)
                    .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status result) {
                                if (!result.isSuccess()) {
                                    Log.e(TAG, "Sending message failed");
                                } else {
                                    Log.i(TAG, "Sent message was successful");
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e(TAG, "Exception while sending message", e);
            }
        }
    }

    public void sendJoinMessage(String userName) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "join");
            jsonObject.put("name", userName);
            sendMessage(jsonObject.toString());
        } catch (JSONException exception) {
            Log.e(TAG, "SendJoinMessage Failed");
        }

    }

    public void sendReadyMessage(String userName) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "ready");
            jsonObject.put("name", userName);
            sendMessage(jsonObject.toString());
        } catch (JSONException exception) {
            Log.e(TAG, "SendJoinMessage Failed");
        }
    }

    public interface OnApplicationConnectedListener {
        public void onApplicationConnected();
    }

    public void setOnApplicationConnectedListener(OnApplicationConnectedListener onApplicationConnectedListener) {
        mOnApplicationConnectedListener = onApplicationConnectedListener;
    }

    public PokerCastChannel getPokerCastChannel() {
        return mPokerCastChannel;
    }

    private class MediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo info) {
            mRouteCount++;
            Log.i(TAG, "Add route");
            Toast.makeText(mApplicationContext, "Routed added", Toast.LENGTH_SHORT).show();
            if (mRouteCount == 1 && mMediaRouteButton != null) {
                mMediaRouteButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo info) {
            mRouteCount--;
            Log.i(TAG, "Remote route");
            if (mRouteCount == 0 && mMediaRouteButton != null) {
                mMediaRouteButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            Toast.makeText(mApplicationContext, "Routed selected", Toast.LENGTH_SHORT).show();

            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            createApiClient();
            connectApiClient();
            //String routeId = info.getId();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            teardown();
            mSelectedDevice = null;
        }
    }

    private class ConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle connectionHint) {
            if (mWaitingForReconnect) {
                mWaitingForReconnect = false;
                //reconnectChannels();
            } else {
                try {
                    Cast.CastApi.launchApplication(mApiClient, APPLICATION_ID, false)
                        .setResultCallback(
                            new ResultCallback<Cast.ApplicationConnectionResult>() {
                                @Override
                                public void onResult(Cast.ApplicationConnectionResult result) {
                                    Status status = result.getStatus();
                                    if (status.isSuccess()) {
                                        ApplicationMetadata applicationMetadata =
                                            result.getApplicationMetadata();
                                        mSessionId = result.getSessionId();
                                        mApplicationStarted = true;
                                        String applicationStatus = result.getApplicationStatus();
                                        boolean wasLaunched = result.getWasLaunched();

                                        if (mOnApplicationConnectedListener != null) {
                                            mOnApplicationConnectedListener.onApplicationConnected();
                                        }

                                        mPokerCastChannel = new PokerCastChannel();
                                        try {
                                            Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                                mPokerCastChannel.getNamespace(),
                                                mPokerCastChannel);
                                        } catch (IOException e) {
                                            Log.e(TAG, "Exception while creating channel", e);
                                        }

                                    } else {
                                        teardown();
                                    }
                                }
                            });

                } catch (Exception e) {
                    Log.e(TAG, "Failed to launch application", e);
                }
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            mWaitingForReconnect = true;
        }
    }

    private class ConnectionFailedListener implements GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            teardown();
        }
    }

}

