package com.duse.android.connectandplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.duse.android.connectandplay.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by kristinaneel on 10/16/2016.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private Toolbar mToolbar;
    private BottomSheetBehavior mBottomSheetBehavior; //TODO: to use for custom bottomsheet
    private View mBottomSheetLayout;
    private GoogleMap mMap;
    private static final double DRAKE_UNIVERSITY_STADIUM_LAT = 41.605007;
    private static final double DRAKE_UNIVERSITY_STADIUM_LNG = -93.6563355;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //@assignee: Mahesh TODO: get last marker used /location
        //inflate layout
        setContentView(R.layout.activity_maps_bottomsheet);
        //shows Action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //This opens ExploreGamesActivity
        mBottomSheetLayout = findViewById(R.id.layout_explore_bottom_sheet);
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


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //@assignee: Mahesh TODO: save last location / marker used
    }

    /**
     * Configures maps when it is ready
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //can be replaced with latitude and longitude
        setCameraPosition(DRAKE_UNIVERSITY_STADIUM_LAT, DRAKE_UNIVERSITY_STADIUM_LNG);
        addMarker(DRAKE_UNIVERSITY_STADIUM_LAT, DRAKE_UNIVERSITY_STADIUM_LNG,
                "Flag Football this Saturday",
                "Software Engineering Group 2");
    }

    public void setCameraPosition(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void addMarker(double latitude, double longitude, String title, String organizer){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet("Organized by " + organizer)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
            //TODO: open intent for saved games
        } else if (id == R.id.action_profile){
            //TODO: open intent for profile
        } else if (id == R.id.action_about){
            //TODO: open intent for about
        }

        return super.onOptionsItemSelected(item);
    }
}

