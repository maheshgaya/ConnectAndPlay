package com.duse.android.connectandplay.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.data.GamesContract;

/**
 * Created by Mahesh Gaya on 10/17/16.
 */

public class FootballFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String[] GAME_PROJECTION ={
            GamesContract.GameEntry.TABLE_NAME + "." + GamesContract.GameEntry._ID,
            GamesContract.GameEntry.COLUMN_GAME_NAME,
            GamesContract.GameEntry.COLUMN_TIME,
            GamesContract.GameEntry.COLUMN_DATE,
            GamesContract.GameEntry.COLUMN_SHORT_DESC,
            GamesContract.GameEntry.COLUMN_LOCATION,
            GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED,
            GamesContract.UserEntry.TABLE_NAME + "." + GamesContract.UserEntry.COLUMN_USERNAME,
            GamesContract.SportEntry.TABLE_NAME + "." + GamesContract.SportEntry.COLUMN_SPORT_NAME
    };

    public static final int COLUMN_GAME_ID = 0;
    public static final int COLUMN_GAME_NAME = 1;
    public static final int COLUMN_TIME = 2;
    public static final int COLUMN_DATE = 3;
    public static final int COLUMN_DESCRIPTION = 4;
    public static final int COLUMN_LOCATION = 5;
    public static final int COLUMN_PEOPLE_NEEDED = 6;
    public static final int COLUMN_USERNAME = 7;
    public static final int COLUMN_SPORT_NAME = 8;
    public FootballFragment(){
        //required empty constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_football, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
