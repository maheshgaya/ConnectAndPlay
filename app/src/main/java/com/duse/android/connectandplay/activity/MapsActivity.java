package com.duse.android.connectandplay.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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

import com.duse.android.connectandplay.service.FetchAddressIntentService;
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
import com.google.android.gms.maps.model.MarkerOptions;

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
    @BindView(R.id.layout_explore_bottom_sheet) View mBottomSheetLayout;

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 1;
    private static final double DRAKE_UNIVERSITY_STADIUM_LAT = 41.605007;
    private static final double DRAKE_UNIVERSITY_STADIUM_LNG = -93.6563355;

    private boolean mPermissionDenied = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //@assignee: Mahesh TODO: get last marker used /location
        //inflate layout
        setContentView(R.layout.activity_maps_bottomsheet);
        ButterKnife.bind(this);
        //shows Action bar
        setSupportActionBar(mToolbar);


        //This opens ExploreGamesActivity
        mBottomSheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExploreGamesActivity.class);
                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GameSyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //@assignee: Mahesh TODO: save last location / marker used
    }


    public void updateGames(){
        GameSyncAdapter.syncImmediately(this);
    }

    /**
     * Configures maps when it is ready
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        enableMyLocation();
        //TODO: replace with latitude and longitude
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng position) {
                Toast.makeText(getApplicationContext(), "lat: " + position.latitude +
                        " \nlng: " + position.longitude, Toast.LENGTH_SHORT).show();
                addMarker(position.latitude, position.longitude, "test", "mahesh");
            }
        });

        setCameraPosition(DRAKE_UNIVERSITY_STADIUM_LAT, DRAKE_UNIVERSITY_STADIUM_LNG);
        addMarker(DRAKE_UNIVERSITY_STADIUM_LAT, DRAKE_UNIVERSITY_STADIUM_LNG,
                "Flag Football this Saturday",
                "Software Engineering Group 2");
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
    public void addMarker(double latitude, double longitude, String title, String organizer){
        Float[] color = new Float[]{
                BitmapDescriptorFactory.HUE_GREEN, //basketball
                BitmapDescriptorFactory.HUE_CYAN, //football
                BitmapDescriptorFactory.HUE_ORANGE, //tennis
                BitmapDescriptorFactory.HUE_VIOLET, //volleyball
                BitmapDescriptorFactory.HUE_ROSE
        }; //TODO: use color per game


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet("Organized by " + organizer)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                .setTag(0);
    }

    /**
     * Inflates menus for this class
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    /**
     * Adds logic to the menus for this class
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.pref_general.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_saved_games) {
            Intent intent = new Intent(this, YourGamesActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_profile){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_about){
            //TODO: open intent for about
        } /*else if (id == R.id.action_refresh){
            updateGames();
        }*/

        return super.onOptionsItemSelected(item);
    }

    //TODO: @assignee Mahesh create markers from database
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

