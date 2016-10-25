package com.duse.android.connectandplay.fragment;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;
import com.duse.android.connectandplay.data.GamesContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_URI = "URI";
    private Uri mUri;
    private static final int DETAIL_LOADER = 0; //inner join between game, user, and sport
    private static final int PARTICIPATE_LOADER = 1; //participate table
    private static final double DRAKE_UNIVERSITY_STADIUM_LAT = 41.605007;
    private static final double DRAKE_UNIVERSITY_STADIUM_LNG = -93.6563355;

    private GoogleMap mGoogleMap;

    //TextViews
    @BindView(R.id.game_title_text_view)TextView mTitleTextView;
    @BindView(R.id.game_date_time_text_view)TextView mDateTimeTextView;
    @BindView(R.id.game_location_text_view)TextView mLocationTextView;
    @BindView(R.id.game_desc_text_view)TextView mDescTextView;
    @BindView(R.id.game_people_needed_text_view)TextView mPeopleNeededTextView;
    @BindView(R.id.organizer_text_view)TextView mOrganizerTextView;
    @BindView(R.id.map_detail) MapView mMapView;

    //Buttons
    @BindView(R.id.game_participate_button)Button mParticipateButton;
    @BindView(R.id.game_share_button)Button mShareButton;

    //ImageButton
    @BindView(R.id.game_icon)ImageButton mIconImageButton;

    //Strings
    @BindString(R.string.organized_by)String mOrganizedByStr;
    @BindString(R.string.people_needed)String mPeopleStr;
    @BindString(R.string.person_needed)String mPersonStr;
    @BindString(R.string.report_string)String mReportStr;
    @BindString(R.string.participate_button)String mParticipateStr;
    @BindString(R.string.participating_button)String mParticipatingStr;


    //for game inner join table
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

    //For participate table
    private static final String[] PARTICIPATE_PROJECTION = {
            GamesContract.ParticipateEntry.TABLE_NAME + "." + GamesContract.ParticipateEntry._ID,
            GamesContract.ParticipateEntry.COLUMN_GAME_ID
    };

    public static final int COLUMN_PARTICIPATE_ID = 0;
    public static final int COLUMN_PARTICIPATE_GAME_ID = 1;

    public DetailFragment(){
        //required default constructor
    }


    //TODO: Add to Participate table
    //TODO: Remove from Participate table

    /**
     * Set Options Menu to true
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //Get Options menu

    }

    /**
     * inflate options menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.game_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * deals with options menu item
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            //We are not actually implementing the full report system since there is no
            //central server
            Toast.makeText(getContext(), mReportStr, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * creates the view and bind the view items
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        //bind the views
        ButterKnife.bind(this, rootView);
        //intialize the map
        mMapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this.getActivity());
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //TODO: set zoom for maps based on location table
                mGoogleMap = googleMap;
                Log.d(TAG, "onMapReady: ");
                //can be replaced with latitude and longitude
                setCameraPosition(DRAKE_UNIVERSITY_STADIUM_LAT, DRAKE_UNIVERSITY_STADIUM_LNG);
                addMarker(DRAKE_UNIVERSITY_STADIUM_LAT, DRAKE_UNIVERSITY_STADIUM_LNG,
                        "Flag Football this Saturday",
                        "Software Engineering Group 2");
                mMapView.onResume();
            }
        });

        mUri = getActivity().getIntent().getData();
        mParticipateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add function for participate

            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add function for share
            }
        });

        return rootView;
    }

    public void setCameraPosition(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    public void addMarker(double latitude, double longitude, String title, String organizer){
        Float[] color = new Float[]{
                BitmapDescriptorFactory.HUE_GREEN, //basketball
                BitmapDescriptorFactory.HUE_CYAN, //football
                BitmapDescriptorFactory.HUE_ORANGE, //tennis
                BitmapDescriptorFactory.HUE_VIOLET, //volleyball
                BitmapDescriptorFactory.HUE_ROSE
        }; //TODO: use color per game


        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet("Organized by " + organizer)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                .setTag(0);
    }


    /**
     * initializes loaders
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(PARTICIPATE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();

    }
    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * query the content provider via loaders
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri == null){
            return null;
        }

        switch (id){
            case DETAIL_LOADER:{
                return new CursorLoader(
                        getActivity(),
                        mUri, //GAME_WITH_ID
                        GAME_PROJECTION,
                        null,
                        null,
                        null
                );
            }
            case PARTICIPATE_LOADER:{
                int gameId = Integer.parseInt(mUri.getLastPathSegment());
                Uri participateUri = GamesContract.ParticipateEntry.CONTENT_URI;
                return new CursorLoader(
                        getActivity(),
                        participateUri,
                        PARTICIPATE_PROJECTION,
                        GamesContract.ParticipateEntry.COLUMN_GAME_ID + " = ?",
                        new String[]{Integer.toString(gameId)},
                        null
                );
            }
            default:{
                return null;
            }
        }

    }

    /**
     * Update the UI once the loader has finished
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case DETAIL_LOADER:{
                if (data.getCount() > 0 && data.moveToFirst()){
                    String title = data.getString(COLUMN_GAME_NAME);
                    String time = data.getString(COLUMN_TIME);
                    String date = data.getString(COLUMN_DATE);
                    String location = data.getString(COLUMN_LOCATION);
                    String desc = data.getString(COLUMN_DESCRIPTION);
                    int peopleNeeded = data.getInt(COLUMN_PEOPLE_NEEDED);
                    String peopleNeededStr;
                    if (peopleNeeded <= 1){
                        peopleNeededStr = mPersonStr;
                    } else {
                        peopleNeededStr = mPeopleStr;
                    }
                    String organizer = data.getString(COLUMN_USERNAME);
                    String sport = data.getString(COLUMN_SPORT_NAME);

                    //sets image
                    int imageUrl = Utility.getIconId(getContext(), sport);
                    Picasso
                            .with(getContext())
                            .load(imageUrl)
                            .error(R.drawable.ic_placeholder)
                            .into(mIconImageButton);

                    //sets texts for detail fragment
                    mTitleTextView.setText(title);
                    mDateTimeTextView.setText(date + " | " + time);
                    mLocationTextView.setText(location);
                    mDescTextView.setText(desc);
                    mPeopleNeededTextView.setText(peopleNeeded + " " + peopleNeededStr);
                    mOrganizerTextView.setText(mOrganizedByStr + " " + organizer);

                }
                break;
            }
            case PARTICIPATE_LOADER:{
                if (data.getCount() > 0 && data.moveToFirst()){
                    //Allow to Unfavorite
                    mParticipateButton.setText(mParticipatingStr);
                    if (Build.VERSION.SDK_INT <= 23){
                        mParticipateButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        mParticipateButton.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    }

                } else {
                    //Allow to Favorite
                    mParticipateButton.setText(mParticipateStr);
                    if (Build.VERSION.SDK_INT <= 23){
                        mParticipateButton.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        mParticipateButton.setTextColor(getResources().getColor(android.R.color.black, null));
                    }

                }
                break;
            }
        }
    }

    /**
     * does nothing
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
