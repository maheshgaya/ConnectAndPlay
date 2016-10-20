package com.duse.android.connectandplay.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class GamesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "games.db";
    public static final int DATABASE_VERSION = 1;

    public GamesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
