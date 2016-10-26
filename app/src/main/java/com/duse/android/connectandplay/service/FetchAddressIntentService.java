package com.duse.android.connectandplay.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Geocoder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class FetchAddressIntentService extends IntentService {
    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    /**
     * Gets address in plain text or gets Latitude and Longitude
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //Gets GeoCoding
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
    }

    //TODO: add respective methods to deal with getting address or getting latitude and longitude
}
