package com.duse.android.connectandplay.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class LatLongService extends IntentService {
    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public LatLongService() {
        super("LatLongService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //will get GeoCoding and Reverse GeoCoding
    }
}
