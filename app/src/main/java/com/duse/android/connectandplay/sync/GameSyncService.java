package com.duse.android.connectandplay.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Mahesh Gaya on 10/24/16.
 */

public class GameSyncService extends Service {
    private static final String TAG = GameSyncService.class.getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private static GameSyncAdapter sGameSyncAdapter = null;
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: executed");
        synchronized (sSyncAdapterLock) {
            if (sGameSyncAdapter == null) {
                sGameSyncAdapter = new GameSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sGameSyncAdapter.getSyncAdapterBinder();
    }
}
