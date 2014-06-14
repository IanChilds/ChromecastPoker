package com.pokercast.castutils;

import android.content.Context;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.View;
import com.google.android.gms.cast.CastMediaControlIntent;

/**
 * Controls interaction between user's device and Chromecast.
 */
public class CastManager {
    public static final String APPLICATION_ID = "APPLICATION_ID";

    private static CastManager castManagerInstance = null;
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouterCallback mMediaRouterCallback;
    private MediaRouteButton mMediaRouteButton;

    protected CastManager(Context context) {
        mMediaRouter = MediaRouter.getInstance(context);
        mMediaRouteSelector = new MediaRouteSelector.Builder()
            .addControlCategory(CastMediaControlIntent.categoryForCast(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID))
            .build();
        mMediaRouterCallback = new MediaRouterCallback();
    }

    public static CastManager createInstance(Context context) {
        if (castManagerInstance == null) {
            castManagerInstance = new CastManager(context);
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

    public void setCastConnectButton(MediaRouteButton mediaRouteButton) {
        mediaRouteButton.setRouteSelector(mMediaRouteSelector);
    }

    public void startDiscoveringDevices() {
        Log.i("Route", "start discovering devices");
        mMediaRouter.addCallback(
            mMediaRouteSelector,
            mMediaRouterCallback,
            MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN);
    }

    public void stopDiscoveringDevices() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
    }

    private class MediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.i("Route", "Add route");
            mMediaRouteButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo info) {
            mMediaRouteButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
//            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            String routeId = info.getId();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            //    teardown();
            //  mSelectedDevice = null;
        }
    }
}

