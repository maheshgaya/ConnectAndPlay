package com.duse.android.connectandplay;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class Constant {
    /**
     * Add JSON Tags
     */

    //Games
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
    public static final String USER_ARRAY = "users";
    public static final String USER_USERNAME = "Username";
    public static final String USER_FIRST_NAME = "First Name";
    public static final String USER_LAST_NAME = "Last Name";
    public static final String USER_BIOGRAPHY = "Biography";

    //Sports
    public static final String SPORT_ARRAY = "sports";
    public static final String SPORT_NAME = "name";

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


}
