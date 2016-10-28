package com.duse.android.connectandplay.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.PermissionUtils;
import com.duse.android.connectandplay.R;

import com.duse.android.connectandplay.data.GamesContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.duse.android.connectandplay.sync.GameSyncAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kristinaneel on 10/16/2016.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor>,
        ActivityCompat.OnRequestPermissionsResultCallback{

    //Binding views
    @BindView(R.id.toolbar) Toolbar mToolbar;
//    @BindView(R.id.layout_explore_bottom_sheet) View mBottomSheetLayout;

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 1;
    private static final double DRAKE_UNIVERSITY_STADIUM_LAT = 41.605007;
    private static final double DRAKE_UNIVERSITY_STADIUM_LNG = -93.6563355;

    private Double mLatitudeArgs;
    private Double mLongitudeArgs;

    private boolean mPermissionDenied = false;

    //inner join between game, user, and sport and for colors also
    private static final int GAME_BASKETBALL_LOADER = 0;
    private static final int GAME_FOOTBALL_LOADER = 1;
    private static final int GAME_SOCCER_LOADER = 2;
    private static final int GAME_TENNIS_LOADER = 3;
    private static final int GAME_VOLLEYBALL_LOADER = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if user comes from a detail mapview
        if (getIntent().getExtras() != null) {
            mLatitudeArgs = getIntent().getExtras().getDouble(Constant.EXTRA_LATITIUDE);
            mLongitudeArgs = getIntent().getExtras().getDouble(Constant.EXTRA_LONGITUDE);
        }




        //inflate layout
        setContentView(R.layout.activity_maps_bottomsheet);
        ButterKnife.bind(this);
        //shows Action bar
        setSupportActionBar(mToolbar);
        //adds back button
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportLoaderManager().initLoader(GAME_BASKETBALL_LOADER, null, this);
        getSupportLoaderManager().initLoader(GAME_FOOTBALL_LOADER, null, this);
        getSupportLoaderManager().initLoader(GAME_SOCCER_LOADER, null, this);
        getSupportLoaderManager().initLoader(GAME_TENNIS_LOADER, null, this);
        getSupportLoaderManager().initLoader(GAME_VOLLEYBALL_LOADER, null, this);
    }


    /**
     * Configures maps when it is ready
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        enableMyLocation();
        /*mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng position) {
                Toast.makeText(getApplicationContext(), "lat: " + position.latitude +
                        " \nlng: " + position.longitude, Toast.LENGTH_SHORT).show();
            }
        });*/

        //Get markers
        getSupportLoaderManager().restartLoader(GAME_BASKETBALL_LOADER, null, this);
        getSupportLoaderManager().restartLoader(GAME_FOOTBALL_LOADER, null, this);
        getSupportLoaderManager().restartLoader(GAME_SOCCER_LOADER, null, this);
        getSupportLoaderManager().restartLoader(GAME_TENNIS_LOADER, null, this);
        getSupportLoaderManager().restartLoader(GAME_VOLLEYBALL_LOADER, null, this);

        if (mLatitudeArgs != null && mLongitudeArgs != null){
            setCameraPosition(mLatitudeArgs, mLongitudeArgs);
        }




    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, REQUEST_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != REQUEST_LOCATION) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    /**
     * Set the camera zoom
     * @param latitude
     * @param longitude
     */
    public void setCameraPosition(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }


    /**
     * Adds a marker on the map
     * @param latitude
     * @param longitude
     * @param title
     * @param organizer
     */
    public void addMarker(double latitude, double longitude, String title, String organizer, int color, Uri uri){
        Float bitMapColor;
        switch (color){
            case GAME_BASKETBALL_LOADER:{
                bitMapColor = BitmapDescriptorFactory.HUE_GREEN; //basketball
                break;
            }
            case GAME_FOOTBALL_LOADER:{
                bitMapColor = BitmapDescriptorFactory.HUE_CYAN; //football
                break;
            }
            case GAME_SOCCER_LOADER:{
                bitMapColor = BitmapDescriptorFactory.HUE_ORANGE; //soccer
                break;
            }
            case GAME_TENNIS_LOADER:{
                bitMapColor = BitmapDescriptorFactory.HUE_VIOLET; //tennis
                break;
            }
            case GAME_VOLLEYBALL_LOADER:{
                bitMapColor = BitmapDescriptorFactory.HUE_ROSE; //volleyball
                break;
            }
            default:{
                bitMapColor = BitmapDescriptorFactory.HUE_RED;
                break;
            }
        }

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet("Organized by " + organizer)
                .icon(BitmapDescriptorFactory.defaultMarker(bitMapColor)))
                .setTag(uri);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTag().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case GAME_BASKETBALL_LOADER:{
                Cursor sportCursor = this.getContentResolver().query(
                        GamesContract.SportEntry.CONTENT_URI,
                        new String[]{GamesContract.SportEntry._ID},
                        GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                        new String[]{getResources().getString(R.string.basketball_query_key)},
                        null
                );
                long sportId = 0;
                try {
                    if (sportCursor.moveToFirst()) {
                        int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                        sportId = sportCursor.getLong(sportIndex);
                    }
                } finally {
                    sportCursor.close();
                }


                return new CursorLoader(
                        getApplicationContext(),
                        GamesContract.GameEntry.buildGameSportUri(sportId),
                        Constant.GAME_PROJECTION,
                        null,
                        null,
                        null
                );
            }
            case GAME_FOOTBALL_LOADER:{
                Cursor sportCursor = this.getContentResolver().query(
                        GamesContract.SportEntry.CONTENT_URI,
                        new String[]{GamesContract.SportEntry._ID},
                        GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                        new String[]{getResources().getString(R.string.football_query_key)},
                        null
                );
                long sportId = 0;
                try {
                    if (sportCursor.moveToFirst()) {
                        int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                        sportId = sportCursor.getLong(sportIndex);
                    }
                } finally {
                    sportCursor.close();
                }


                return new CursorLoader(
                        getApplicationContext(),
                        GamesContract.GameEntry.buildGameSportUri(sportId),
                        Constant.GAME_PROJECTION,
                        null,
                        null,
                        null
                );
            }
            case GAME_SOCCER_LOADER:{
                Cursor sportCursor = this.getContentResolver().query(
                        GamesContract.SportEntry.CONTENT_URI,
                        new String[]{GamesContract.SportEntry._ID},
                        GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                        new String[]{getResources().getString(R.string.soccer_query_key)},
                        null
                );
                long sportId = 0;
                try {
                    if (sportCursor.moveToFirst()) {
                        int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                        sportId = sportCursor.getLong(sportIndex);
                    }
                } finally {
                    sportCursor.close();
                }


                return new CursorLoader(
                        getApplicationContext(),
                        GamesContract.GameEntry.buildGameSportUri(sportId),
                        Constant.GAME_PROJECTION,
                        null,
                        null,
                        null
                );
            }
            case GAME_TENNIS_LOADER:{
                Cursor sportCursor = this.getContentResolver().query(
                        GamesContract.SportEntry.CONTENT_URI,
                        new String[]{GamesContract.SportEntry._ID},
                        GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                        new String[]{getResources().getString(R.string.tennis_query_key)},
                        null
                );
                long sportId = 0;
                try {
                    if (sportCursor.moveToFirst()) {
                        int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                        sportId = sportCursor.getLong(sportIndex);
                    }
                } finally {
                    sportCursor.close();
                }


                return new CursorLoader(
                        getApplicationContext(),
                        GamesContract.GameEntry.buildGameSportUri(sportId),
                        Constant.GAME_PROJECTION,
                        null,
                        null,
                        null
                );
            }
            case GAME_VOLLEYBALL_LOADER:{
                Cursor sportCursor = this.getContentResolver().query(
                        GamesContract.SportEntry.CONTENT_URI,
                        new String[]{GamesContract.SportEntry._ID},
                        GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                        new String[]{getResources().getString(R.string.volleyball_query_key)},
                        null
                );
                long sportId = 0;
                try {
                    if (sportCursor.moveToFirst()) {
                        int sportIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                        sportId = sportCursor.getLong(sportIndex);
                    }
                } finally {
                    sportCursor.close();
                }


                return new CursorLoader(
                        getApplicationContext(),
                        GamesContract.GameEntry.buildGameSportUri(sportId),
                        Constant.GAME_PROJECTION,
                        null,
                        null,
                        null
                );
            }
            default:{
                return null;
            }
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case GAME_BASKETBALL_LOADER:{
                while (data.moveToNext()){
                    //get latitude, longitude, title and organizer
                    double latitude = data.getDouble(Constant.COLUMN_LATITUDE);
                    double longitude = data.getDouble(Constant.COLUMN_LONGITUDE);
                    String title = data.getString(Constant.COLUMN_GAME_NAME);
                    String organizer = data.getString(Constant.COLUMN_USERNAME);

                    //get gameId also to set tag
                    long gameId = data.getLong(Constant.COLUMN_GAME_ID);
                    Uri gameUri = GamesContract.GameEntry.buildGameUri(gameId);

                    addMarker(latitude, longitude, title, organizer, GAME_BASKETBALL_LOADER, gameUri);
                }
                break;
            }
            case GAME_FOOTBALL_LOADER:{
                while (data.moveToNext()){
                    //get latitude, longitude, title and organizer
                    double latitude = data.getDouble(Constant.COLUMN_LATITUDE);
                    double longitude = data.getDouble(Constant.COLUMN_LONGITUDE);
                    String title = data.getString(Constant.COLUMN_GAME_NAME);
                    String organizer = data.getString(Constant.COLUMN_USERNAME);

                    //get gameId also to set tag
                    long gameId = data.getLong(Constant.COLUMN_GAME_ID);
                    Uri gameUri = GamesContract.GameEntry.buildGameUri(gameId);
                    addMarker(latitude, longitude, title, organizer, GAME_FOOTBALL_LOADER, gameUri);
                }
                break;
            }
            case GAME_SOCCER_LOADER:{
                while (data.moveToNext()){
                    //get latitude, longitude, title and organizer
                    double latitude = data.getDouble(Constant.COLUMN_LATITUDE);
                    double longitude = data.getDouble(Constant.COLUMN_LONGITUDE);
                    String title = data.getString(Constant.COLUMN_GAME_NAME);
                    String organizer = data.getString(Constant.COLUMN_USERNAME);

                    //get gameId also to set tag
                    long gameId = data.getLong(Constant.COLUMN_GAME_ID);
                    Uri gameUri = GamesContract.GameEntry.buildGameUri(gameId);
                    addMarker(latitude, longitude, title, organizer, GAME_SOCCER_LOADER, gameUri);
                }
                break;
            }
            case GAME_TENNIS_LOADER:{
                while (data.moveToNext()){
                    //get latitude, longitude, title and organizer
                    double latitude = data.getDouble(Constant.COLUMN_LATITUDE);
                    double longitude = data.getDouble(Constant.COLUMN_LONGITUDE);
                    String title = data.getString(Constant.COLUMN_GAME_NAME);
                    String organizer = data.getString(Constant.COLUMN_USERNAME);

                    //get gameId also to set tag
                    long gameId = data.getLong(Constant.COLUMN_GAME_ID);
                    Uri gameUri = GamesContract.GameEntry.buildGameUri(gameId);
                    addMarker(latitude, longitude, title, organizer, GAME_TENNIS_LOADER, gameUri);
                }
                break;
            }
            case GAME_VOLLEYBALL_LOADER:{
                while (data.moveToNext()){
                    //get latitude, longitude, title and organizer
                    double latitude = data.getDouble(Constant.COLUMN_LATITUDE);
                    double longitude = data.getDouble(Constant.COLUMN_LONGITUDE);
                    String title = data.getString(Constant.COLUMN_GAME_NAME);
                    String organizer = data.getString(Constant.COLUMN_USERNAME);

                    //get gameId also to set tag
                    long gameId = data.getLong(Constant.COLUMN_GAME_ID);
                    Uri gameUri = GamesContract.GameEntry.buildGameUri(gameId);
                    addMarker(latitude, longitude, title, organizer, GAME_VOLLEYBALL_LOADER, gameUri);
                }
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

