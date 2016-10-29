package com.duse.android.connectandplay.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.data.GamesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mahesh Gaya on 10/24/16.
 */

public class GameSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = GameSyncAdapter.class.getSimpleName();
    //in seconds
    //60 seconds * 60 minutes * 24 hours
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public GameSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        try{
            //for games
            InputStream inputStream = getContext().getAssets().open(Constant.GAME_JSON);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String gamesJson = new String(buffer, "UTF-8");

            inputStream = getContext().getAssets().open(Constant.SPORT_JSON);
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String sportJson = new String(buffer, "UTF-8");

            inputStream = getContext().getAssets().open(Constant.USER_JSON);
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String usersJson = new String(buffer, "UTF-8");

            inputStream = getContext().getAssets().open(Constant.LOCATION_JSON);
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String locationJson = new String(buffer, "UTF-8");

            try {
                readLocationJson(locationJson);
                readSportsJson(sportJson);
                readUsersJson(usersJson);
                readGamesJson(gamesJson);
                Log.d(TAG, "onPerformSync: Wow, this is done correctly!");
            }catch (JSONException e){
                e.printStackTrace();
                return;
            }


        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        return;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        GameSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        Log.d(TAG, "onAccountCreated: account is just created");
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.d(TAG, "syncImmediately: syncing data");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            Log.d(TAG, "getSyncAccount: account created");
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    /**************************
     * Add to Database
     **************************/

    /**
     *
     * @param username
     * @param firstname
     * @param lastname
     * @param biography
     * @return
     */
    private long addUser(String username, String firstname, String lastname, String biography){
        long userId;
        //check if user exists, username will be unique, and can only be changed once
        Cursor userCursor = getContext().getContentResolver().query(
                GamesContract.UserEntry.CONTENT_URI,
                new String[]{GamesContract.UserEntry._ID},
                GamesContract.UserEntry.COLUMN_USERNAME + " = ? ",
                new String[]{username},
                null
        );

        try {
            if (userCursor.moveToFirst()){
                int gameIdIndex = userCursor.getColumnIndex(GamesContract.UserEntry._ID);
                userId = userCursor.getLong(gameIdIndex);
            } else {
                //create a contentvalue to hold the data
                ContentValues userValues = new ContentValues();
                userValues.put(GamesContract.UserEntry.COLUMN_USERNAME, username);
                userValues.put(GamesContract.UserEntry.COLUMN_FIRST_NAME, firstname);
                userValues.put(GamesContract.UserEntry.COLUMN_LAST_NAME, lastname);
                userValues.put(GamesContract.UserEntry.COLUMN_BIOGRAPHY, biography);
                userValues.put(GamesContract.UserEntry.COLUMN_CURRENT_USER, 0); //false for current user

                //add to database
                Uri insertUri = getContext().getContentResolver().insert(
                        GamesContract.UserEntry.CONTENT_URI,
                        userValues
                );

                userId = ContentUris.parseId(insertUri);
            }
        }finally {
            userCursor.close();
        }


        return userId;
    }
    //gameName, sport, time, date, location, description, peopleNeeded, organizer
    private long addGame(String gameName, String sport, String time, String date,
                         String location, String description, int peopleNeeded,
                         String organizer){
        long gameId = -1;
        long sportId = -1;
        long userId = -1;
        long locationId = -1;

        // /check if game already exists, use different parameters, else add it to db

        //User, sport and location should always be there, if not cannot add to database
        Cursor sportCursor = getContext().getContentResolver().query(
                GamesContract.SportEntry.CONTENT_URI,
                new String[]{GamesContract.SportEntry._ID},
                GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                new String[]{sport},
                null
        );

        Cursor userCursor = getContext().getContentResolver().query(
                GamesContract.UserEntry.CONTENT_URI,
                new String[]{GamesContract.UserEntry._ID},
                GamesContract.UserEntry.COLUMN_USERNAME + " = ? ",
                new String[]{organizer},
                null
        );

        Cursor locationCursor = getContext().getContentResolver().query(
                GamesContract.LocationEntry.CONTENT_URI,
                new String[]{GamesContract.LocationEntry._ID},
                GamesContract.LocationEntry.COLUMN_ADDRESS + " = ? ",
                new String[]{location},
                null
        );

        if (sportCursor.moveToFirst()){
            int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
            sportId = sportCursor.getLong(sportIndex);
            Log.d(TAG, "addGame: sportId" +  sportId);
        }
        if (userCursor.moveToFirst()){
            int userIndex = userCursor.getColumnIndex(GamesContract.UserEntry._ID);
            userId = userCursor.getLong(userIndex);
            Log.d(TAG, "addGame: userId" +  userId);
        }
        if (locationCursor.moveToFirst()) {
            int locationIndex = locationCursor.getColumnIndex(GamesContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIndex);
            Log.d(TAG, "addGame: locationId" +  locationId);
        }

        try {
            if (userCursor.getCount() > 0 && sportCursor.getCount() > 0 && locationCursor.getCount() > 0) {
                Log.d(TAG, "addGame: sport, location and user is in database");
                Cursor gameCursor = getContext().getContentResolver().query(
                        GamesContract.GameEntry.CONTENT_URI,
                        new String[]{GamesContract.GameEntry._ID},
                        GamesContract.GameEntry.COLUMN_GAME_NAME + " = ? AND " +
                                GamesContract.GameEntry.COLUMN_SPORT_ID + " = ? AND " +
                                GamesContract.GameEntry.COLUMN_ORGANIZER_ID + " = ? AND " +
                                GamesContract.GameEntry.COLUMN_LOCATION_ID + " = ? AND " +
                                GamesContract.GameEntry.COLUMN_DATE + " = ? ",
                        new String[]{gameName, Long.toString(sportId), Long.toString(userId), Long.toString(locationId), date},
                        null
                );
                try {
                    if (gameCursor.moveToFirst()) {
                        //if record exists, return that record
                        int gameIndex = gameCursor.getColumnIndex(GamesContract.GameEntry._ID);
                        gameId = gameCursor.getLong(gameIndex);
                    } else {
                        //Create contentValue to hold the data
                        ContentValues gameValues = new ContentValues();
                        gameValues.put(GamesContract.GameEntry.COLUMN_GAME_NAME, gameName);
                        gameValues.put(GamesContract.GameEntry.COLUMN_SPORT_ID, sportId);
                        gameValues.put(GamesContract.GameEntry.COLUMN_ORGANIZER_ID, userId);
                        gameValues.put(GamesContract.GameEntry.COLUMN_TIME, time);
                        gameValues.put(GamesContract.GameEntry.COLUMN_DATE, date);
                        gameValues.put(GamesContract.GameEntry.COLUMN_LOCATION_ID, locationId);
                        gameValues.put(GamesContract.GameEntry.COLUMN_SHORT_DESC, description);
                        gameValues.put(GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED, peopleNeeded);

                        //insert the record into database
                        Uri insertUri = getContext().getContentResolver().insert(
                                GamesContract.GameEntry.CONTENT_URI,
                                gameValues
                        );
                        gameId = ContentUris.parseId(insertUri);
                    }
                } finally {
                    gameCursor.close();
                }
            }
        } finally {
            userCursor.close();
            sportCursor.close();
            locationCursor.close();
        }
        return gameId;
    }

    private long addSport(String sportName){
        long sportId;
        //check if sport already exists, else add it to db
        Cursor sportCursor = getContext().getContentResolver().query(
                GamesContract.SportEntry.CONTENT_URI,
                new String[]{GamesContract.SportEntry._ID},
                GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                new String[]{sportName},
                null
        );
        try {
            if (sportCursor.moveToFirst()){
                //if record exists, return that record
                int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                sportId = sportCursor.getLong(sportIndex);
            } else {
                //Create contentValue to hold the data
                ContentValues sportValues = new ContentValues();
                //insert the record into database
                sportValues.put(GamesContract.SportEntry.COLUMN_SPORT_NAME, sportName);

                Uri insertUri = getContext().getContentResolver().insert(
                        GamesContract.SportEntry.CONTENT_URI,
                        sportValues
                );
                sportId = ContentUris.parseId(insertUri);
            }
        } finally {
            sportCursor.close();
        }

        return sportId;
    }

    private long addLocation(String address, double latitude, double longitude){
        long locationId;
        //check if location already exists, else add it to db
        Cursor locationCursor = getContext().getContentResolver().query(
                GamesContract.LocationEntry.CONTENT_URI,
                new String[]{GamesContract.LocationEntry._ID},
                GamesContract.LocationEntry.COLUMN_LATITUDE + " = ? AND " +
                        GamesContract.LocationEntry.COLUMN_LONGITUDE + " = ? ",
                new String[]{Double.toString(latitude), Double.toString(longitude)},
                null
        );

        try{
            if (locationCursor.moveToFirst()){
                //if record exists, return that record
                int locationIndex = locationCursor.getColumnIndex(GamesContract.SportEntry._ID);
                locationId = locationCursor.getLong(locationIndex);
            } else {
                //Create contentValue to hold the data
                ContentValues locationValues = new ContentValues();
                //insert the record into database
                locationValues.put(GamesContract.LocationEntry.COLUMN_ADDRESS, address);
                locationValues.put(GamesContract.LocationEntry.COLUMN_LATITUDE, latitude);
                locationValues.put(GamesContract.LocationEntry.COLUMN_LONGITUDE, longitude);

                Uri insertUri = getContext().getContentResolver().insert(
                        GamesContract.LocationEntry.CONTENT_URI,
                        locationValues
                );
                locationId = ContentUris.parseId(insertUri);
            }
        } finally {
            locationCursor.close();
        }
        return  locationId;
    }

    /**************************
     * Read from JSON
     **************************/
    private void readUsersJson(String usersJson)
            throws JSONException{
        //read the json from the buffer

        JSONObject usersJsonObject  = new JSONObject(usersJson);
        JSONArray usersJsonArray = usersJsonObject.getJSONArray(Constant.USER_ARRAY);

        for (int i = 0; i < usersJsonArray.length(); i++) {
            JSONObject userObject = usersJsonArray.getJSONObject(i);
            String username = userObject.getString(Constant.USER_USERNAME);
            String firstname = userObject.getString(Constant.USER_FIRST_NAME);
            String lastname = userObject.getString(Constant.USER_LAST_NAME);
            String biography = userObject.getString(Constant.USER_BIOGRAPHY);

            addUser(username, firstname, lastname, biography); //add to db
        }

    }
    private void readSportsJson(String sportsJson)
            throws JSONException{
        //read the json from the buffer

        JSONObject sportsJsonObject  = new JSONObject(sportsJson);
        JSONArray sportsJsonArray = sportsJsonObject.getJSONArray(Constant.SPORT_ARRAY);

        for (int i = 0; i < sportsJsonArray.length(); i++){
            JSONObject sportObject = sportsJsonArray.getJSONObject(i);
            String sportName = sportObject.getString(Constant.SPORT_NAME);

            addSport(sportName); //add to db
        }
    }

    private void readLocationJson(String locationJson)
        throws JSONException{
        //read the json from the buffer
        JSONObject locationsJsonObject = new JSONObject(locationJson);
        JSONArray locationsJsonArray = locationsJsonObject.getJSONArray(Constant.LOCATION_ARRAY);

        for (int i = 0; i < locationsJsonArray.length(); i++){
            JSONObject locationObject = locationsJsonArray.getJSONObject(i);
            String address = locationObject.getString(Constant.LOCATION_ADDRESS);
            Double latitude = locationObject.getDouble(Constant.LOCATION_LATITUDE);
            Double longitude = locationObject.getDouble(Constant.LOCATION_LONGITUDE);
            addLocation(address, latitude, longitude); //add to db
        }
    }

    private void readGamesJson(String gamesJson)
            throws JSONException{
        //read the json from the buffer

        JSONObject gamesJsonObject  = new JSONObject(gamesJson);
        JSONArray gamesJsonArray = gamesJsonObject.getJSONArray(Constant.GAME_ARRAY);

        for (int i = 0; i < gamesJsonArray.length(); i++){
            JSONObject gameObject = gamesJsonArray.getJSONObject(i);
            String gameName = gameObject.getString(Constant.GAME_NAME);
            String sport = gameObject.getString(Constant.GAME_SPORT);
            String time = gameObject.getString(Constant.GAME_TIME);
            String date = gameObject.getString(Constant.GAME_DATE);
            String location = gameObject.getString(Constant.GAME_LOCATION);
            String description = gameObject.getString(Constant.GAME_SHORT_DESC);
            int peopleNeeded = gameObject.getInt(Constant.GAME_PEOPLE_NEEDED);
            String organizer = gameObject.getString(Constant.GAME_ORGANIZER);
            Log.d(TAG, "readGamesJson: " + gameName + " " + sport + " " + time + " " + date + " " + location);
            addGame(gameName, sport, time, date, location, description, peopleNeeded, organizer);//add to db
        }


    }
}
