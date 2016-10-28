package com.duse.android.connectandplay;

import com.duse.android.connectandplay.data.GamesContract;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class Constant {
    /**
     * Add JSON Tags
     */

    //Games
    public static final String GAME_JSON = "games.json";
    public static final String GAME_ARRAY = "games";
    public static final String GAME_NAME = "Game Name";
    public static final String GAME_SPORT = "Sport";
    public static final String GAME_TIME = "Time"; //in 24hr
    public static final String GAME_DATE = "Date"; //MM/dd/yyyy
    public static final String GAME_LOCATION = "Location"; //full location, use PLACES API
    public static final String GAME_SHORT_DESC = "Short Description";
    public static final String GAME_PEOPLE_NEEDED = "Number of People Needed";
    public static final String GAME_ORGANIZER = "Organizer";

    //Users
    public static final String USER_JSON = "users.json";
    public static final String USER_ARRAY = "users";
    public static final String USER_USERNAME = "Username";
    public static final String USER_FIRST_NAME = "First Name";
    public static final String USER_LAST_NAME = "Last Name";
    public static final String USER_BIOGRAPHY = "Biography";

    //Sports
    public static final String SPORT_JSON = "sports.json";
    public static final String SPORT_ARRAY = "sports";
    public static final String SPORT_NAME = "name";

    //Locations
    public static final String LOCATION_JSON = "locations.json";
    public static final String LOCATION_ARRAY = "locations";
    public static final String LOCATION_ADDRESS = "Address";
    public static final String LOCATION_LATITUDE = "Latitude";
    public static final String LOCATION_LONGITUDE = "Longitude";

    /**
     * For Geocoding
     */

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.duse.android.connectandplay";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    /**
     * for querying database
     */
    //inner join for game, location, sport and user
    public static final String[] GAME_PROJECTION = {
            GamesContract.GameEntry.TABLE_NAME + "." + GamesContract.GameEntry._ID,
            GamesContract.GameEntry.COLUMN_GAME_NAME,
            GamesContract.GameEntry.COLUMN_TIME,
            GamesContract.GameEntry.COLUMN_DATE,
            GamesContract.GameEntry.COLUMN_SHORT_DESC,
            GamesContract.LocationEntry.COLUMN_ADDRESS,
            GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED,
            GamesContract.UserEntry.TABLE_NAME + "." + GamesContract.UserEntry.COLUMN_USERNAME,
            GamesContract.SportEntry.TABLE_NAME + "." + GamesContract.SportEntry.COLUMN_SPORT_NAME,
            GamesContract.LocationEntry.COLUMN_LATITUDE,
            GamesContract.LocationEntry.COLUMN_LONGITUDE
    };

    public static final int COLUMN_GAME_ID = 0;
    public static final int COLUMN_GAME_NAME = 1;
    public static final int COLUMN_TIME = 2;
    public static final int COLUMN_DATE = 3;
    public static final int COLUMN_DESCRIPTION = 4;
    public static final int COLUMN_ADDRESS = 5;
    public static final int COLUMN_PEOPLE_NEEDED = 6;
    public static final int COLUMN_USERNAME = 7;
    public static final int COLUMN_SPORT_NAME = 8;
    public static final int COLUMN_LATITUDE = 9;
    public static final int COLUMN_LONGITUDE = 10;

    //Participate projection
    public static final String[] PARTICIPATE_PROJECTION = {
            GamesContract.ParticipateEntry.TABLE_NAME + "." + GamesContract.ParticipateEntry._ID,
            GamesContract.ParticipateEntry.COLUMN_GAME_ID
    };

    public static final int COLUMN_PARTICIPATE_ID = 0;
    public static final int COLUMN_PARTICIPATE_GAME_ID = 1;

    //Sport projection
    public static final String[] SPORT_PROJECTION = {
            GamesContract.SportEntry._ID,
            GamesContract.SportEntry.COLUMN_SPORT_NAME
    };

    public static final int COLUMN_SPORT_ID = 0;
    public static final int COLUMN_SPORTNAME = 1;

    //For colors
    public static final int GAME_BASKETBALL_COLOR = 0;
    public static final int GAME_FOOTBALL_COLOR = 1;
    public static final int GAME_SOCCER_COLOR = 2;
    public static final int GAME_TENNIS_COLOR = 3;
    public static final int GAME_VOLLEYBALL_COLOR = 4;


    //For Bundle
    public static final String EXTRA_LATITIUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";

}
