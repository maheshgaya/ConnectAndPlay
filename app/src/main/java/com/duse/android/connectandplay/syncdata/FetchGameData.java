package com.duse.android.connectandplay.syncdata;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class FetchGameData extends AsyncTask<Void, Void, Void>{
    private Context mContext;
    public FetchGameData(Context context){
        mContext = context;
    }
    private long addUser(){
        long userId = 0;
        return userId;
    }
    private long addGame(){
        long gameId = 0;
        return gameId;
    }

    private long addSport(){
        long sportId = 0;
        return sportId;
    }

    private void getUser(){

    }
    private void getGame(){

    }
    private void getSport(){

    }

    @Override
    protected Void doInBackground(Void... voids) {
        String gamesJson;
        String sportJson;
        String usersJson;

        try{
            //for games
            InputStream inputStream = mContext.getAssets().open("games.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            gamesJson = new String(buffer, "UTF-8");

            inputStream = mContext.getAssets().open("sports.json");
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            sportJson = new String(buffer, "UTF-8");

            inputStream = mContext.getAssets().open("users.json");
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            usersJson = new String(buffer, "UTF-8");

            try {
                readSportsJson(sportJson);
                readUsersJson(usersJson);
                readGamesJson(gamesJson);
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }


        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private void readUsersJson(String usersJson)
        throws JSONException{
        //read the json from the buffer
        try {
            JSONObject usersJsonObject  = new JSONObject(usersJson);
            JSONArray usersJsonArray = usersJsonObject.getJSONArray("users");
            //TODO: get string and add to database
        }catch (JSONException e){
            e.printStackTrace();
            return;
        }
    }
    private void readSportsJson(String sportsJson)
            throws JSONException{
        //read the json from the buffer
        try {
            JSONObject sportsJsonObject  = new JSONObject(sportsJson);
            JSONArray sportsJsonArray = sportsJsonObject.getJSONArray("sports");
            //TODO: get string and add to database
        }catch (JSONException e){
            e.printStackTrace();
            return;
        }
    }

    private void readGamesJson(String gamesJson)
            throws JSONException{
        //read the json from the buffer
        try {
            JSONObject gamesJsonObject  = new JSONObject(gamesJson);
            JSONArray gamesJsonArray = gamesJsonObject.getJSONArray("games");
            //TODO: get string and add to database
        }catch (JSONException e){
            e.printStackTrace();
            return;
        }

    }
}
