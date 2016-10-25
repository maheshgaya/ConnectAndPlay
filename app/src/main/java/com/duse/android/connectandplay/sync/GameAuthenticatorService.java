package com.duse.android.connectandplay.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Mahesh Gaya on 10/24/16.
 */

/**
 * A bound Service that instantiates the authenticator
 * when started.
 */
public class GameAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private GameAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new GameAuthenticator(this);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
