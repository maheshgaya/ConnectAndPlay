package com.duse.android.connectandplay.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class GamesProvider  extends ContentProvider{
    private GamesDbHelper mOpenDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //code for UriMatcher
    private static final int GAME = 100; //dir
    private static final int GAME_WITH_ID = 101; //inner join item

    private static final int USER = 200; //dir
    private static final int USER_WITH_ID = 201; //item
    private static final int USER_CURRENT = 202; //item

    private static final int SPORT = 300; //dir


    private static UriMatcher buildUriMatcher(){
        //initialize uri matcher
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GamesContract.CONTENT_AUTHORITY;
        //add each uri
        //content://authority/game
        matcher.addURI(authority, GamesContract.GameEntry.TABLE_NAME, GAME);
        //content://authority/game/#
        matcher.addURI(authority, GamesContract.GameEntry.TABLE_NAME + "/#", GAME_WITH_ID);

        //content://authority/user
        matcher.addURI(authority, GamesContract.UserEntry.TABLE_NAME, USER);
        //content://authority/user/#
        matcher.addURI(authority, GamesContract.UserEntry.TABLE_NAME + "/#", USER_WITH_ID);
        //content://authority/user/current_user
        matcher.addURI(authority, GamesContract.UserEntry.TABLE_NAME + "/" +
                GamesContract.UserEntry.COLUMN_CURRENT_USER, USER_CURRENT);

        //content://authority/sport
        matcher.addURI(authority, GamesContract.SportEntry.TABLE_NAME, SPORT);


        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenDbHelper = new GamesDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case GAME:{
                return GamesContract.GameEntry.CONTENT_TYPE;
            }
            case GAME_WITH_ID:{
                return GamesContract.GameEntry.CONTENT_ITEM_TYPE;
            }
            case USER:{
                return GamesContract.UserEntry.CONTENT_TYPE;
            }
            case USER_WITH_ID:{
                return GamesContract.UserEntry.CONTENT_ITEM_TYPE;
            }
            case USER_CURRENT:{
                return GamesContract.UserEntry.CONTENT_ITEM_TYPE;
            }
            case SPORT:{
                return GamesContract.SportEntry.CONTENT_TYPE;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
