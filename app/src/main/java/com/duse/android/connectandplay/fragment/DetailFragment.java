package com.duse.android.connectandplay.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
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

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.Utility;
import com.duse.android.connectandplay.activity.MapsActivity;
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

    //for sharing
    private String mShareLocation;
    private String mShareDate;
    private String mShareTime;
    private String mShareGameTitle;

    //for opening mapsActivity
    private Double mMarkerLatitude;
    private Double mMarkerLongitude;

    //for people needed
    private int mPeopleCount;

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

    //for sharing
    @BindString(R.string.share)String mShareStr;
    @BindString(R.string.share_date)String mShareDateStr;
    @BindString(R.string.share_time)String mShareTimeStr;
    @BindString(R.string.share_join)String mShareJoinStr;
    @BindString(R.string.share_location)String mShareLocationStr;
    @BindString(R.string.share_title)String mShareTitleStr;

    //for marker colors
    @BindString(R.string.basketball_query_key)String mBasketballQueryKey;
    @BindString(R.string.football_query_key)String mFootballQueryKey;
    @BindString(R.string.soccer_query_key)String mSoccerQueryKey;
    @BindString(R.string.tennis_query_key)String mTennisQueryKey;
    @BindString(R.string.volleyball_query_key)String mVolleyballQueryKey;

    public DetailFragment(){
        //required default constructor
    }


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
                mGoogleMap = googleMap;
                mMapView.onResume();
                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        openMapsActivity();
                    }
                });
            }
        });

        mUri = getActivity().getIntent().getData();
        mParticipateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gameId = Integer.parseInt(mUri.getLastPathSegment());
                String buttonText = mParticipateButton.getText().toString();
                if (buttonText.equals(mParticipatingStr)){
                    //remove from db
                    boolean removeResult = removeParticipate(gameId);
                    mParticipateButton.setText(mParticipateStr);
                    if (Build.VERSION.SDK_INT < 23){
                        mParticipateButton.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        mParticipateButton.setTextColor(getResources().getColor(android.R.color.black, null));
                    }

                } else if (buttonText.equals(mParticipateStr)) {
                    //add to db
                    long participateId = addParticipate(gameId);
                    mParticipateButton.setText(mParticipatingStr);
                    if (Build.VERSION.SDK_INT < 23){
                        mParticipateButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        mParticipateButton.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    }

                }


            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(createShareIntent(), mShareTitleStr));
            }
        });


        return rootView;
    }

    public void openMapsActivity(){
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        mapIntent.putExtra(Constant.EXTRA_LATITIUDE, mMarkerLatitude);
        mapIntent.putExtra(Constant.EXTRA_LONGITUDE, mMarkerLongitude);
        startActivity(mapIntent);
    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //I'm interested in "Game Title" on "Date" at "Time" at "Location". Join me!
        String shareMessage = mShareStr + " " + mShareGameTitle + " " + mShareDateStr +
                " " + mShareDate + " " + mShareTimeStr + " " + mShareTime +
                " " + mShareLocationStr + " " + mShareLocation + mShareJoinStr;
        shareIntent.putExtra(Intent.EXTRA_TEXT,  shareMessage);
        return shareIntent;
    }

    private long addParticipate(int gameId) {
        long participateId;
        //check if participate is already in table
        Cursor participateCursor = getContext().getContentResolver().query(
                GamesContract.ParticipateEntry.CONTENT_URI,
                new String[]{GamesContract.ParticipateEntry._ID},
                GamesContract.ParticipateEntry.COLUMN_GAME_ID + " = ? ",
                new String[]{Integer.toString(gameId)},
                null
        );

        try {
            if (participateCursor.moveToFirst()){
                //if exists
                int participateIndex = participateCursor.getColumnIndex(GamesContract.ParticipateEntry._ID);
                participateId = participateCursor.getLong(participateIndex);
            } else {
                //else add
                ContentValues participateValues = new ContentValues();
                participateValues.put(GamesContract.ParticipateEntry.COLUMN_GAME_ID, gameId);
                Uri insertUri = getContext().getContentResolver().insert(
                        GamesContract.ParticipateEntry.CONTENT_URI,
                        participateValues
                );
                participateId = ContentUris.parseId(insertUri);

                //update people needed (minus 1)
                ContentValues gameValues = new ContentValues();
                gameValues.put(GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED, mPeopleCount - 1);
                int updateGameUri = getContext().getContentResolver().update(
                        GamesContract.GameEntry.CONTENT_URI,
                        gameValues,
                        GamesContract.GameEntry._ID + " = ?",
                        new String[]{mUri.getLastPathSegment()}
                );

                getLoaderManager().restartLoader(PARTICIPATE_LOADER, null, this);
                getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
            }
        }finally {
            participateCursor.close();
        }

        return participateId;
    }


    private boolean removeParticipate(int gameId) {
        //check if movie exists in table
        Cursor participateCursor = getContext().getContentResolver().query(
                GamesContract.ParticipateEntry.CONTENT_URI,
                new String[]{GamesContract.ParticipateEntry._ID},
                GamesContract.ParticipateEntry.COLUMN_GAME_ID + " = ? ",
                new String[]{Integer.toString(gameId)},
                null
        );
        //then remove it
        try {
            if (participateCursor.moveToFirst()){
                getContext().getContentResolver().delete(
                        GamesContract.ParticipateEntry.CONTENT_URI,
                        GamesContract.ParticipateEntry.COLUMN_GAME_ID + " = ? ",
                        new String[]{Integer.toString(gameId)}
                );

                //update people needed (add 1)
                ContentValues gameValues = new ContentValues();
                gameValues.put(GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED, mPeopleCount + 1);
                int updateGameUri = getContext().getContentResolver().update(
                        GamesContract.GameEntry.CONTENT_URI,
                        gameValues,
                        GamesContract.GameEntry._ID + " = ?",
                        new String[]{mUri.getLastPathSegment()}
                );

                getLoaderManager().restartLoader(PARTICIPATE_LOADER, null, this);
                getLoaderManager().restartLoader(DETAIL_LOADER, null, this);

                return true;
            } else {
                return false;
            }
        } finally {
            participateCursor.close();
        }
    }

    public void setCameraPosition(double latitude, double longitude){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    public void addMarker(double latitude, double longitude, String title, String organizer, int color){
        Float bitMapColor;
        switch (color){
            case Constant.GAME_BASKETBALL_COLOR:{
                bitMapColor = BitmapDescriptorFactory.HUE_GREEN; //basketball
                break;
            }
            case Constant.GAME_FOOTBALL_COLOR:{
                bitMapColor = BitmapDescriptorFactory.HUE_CYAN; //football
                break;
            }
            case Constant.GAME_SOCCER_COLOR:{
                bitMapColor = BitmapDescriptorFactory.HUE_ORANGE; //soccer
                break;
            }
            case Constant.GAME_TENNIS_COLOR:{
                bitMapColor = BitmapDescriptorFactory.HUE_VIOLET; //tennis
                break;
            }
            case Constant.GAME_VOLLEYBALL_COLOR:{
                bitMapColor = BitmapDescriptorFactory.HUE_ROSE; //volleyball
                break;
            }
            default:{
                bitMapColor = BitmapDescriptorFactory.HUE_RED;
                break;
            }
        }

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet("Organized by " + organizer)
                .icon(BitmapDescriptorFactory.defaultMarker(bitMapColor)))
                .setTag(0);

        setCameraPosition(latitude, longitude); //sets the camera position
    }

    private void checkPersonNeed(int peopleNeeded){
        //check to see if peopleNeeded is zero and participate button text is participate
        //then disable participate
        if (peopleNeeded == 0 && mParticipateButton.getText().equals(mParticipateStr)){
            if (Build.VERSION.SDK_INT < 23){
                mParticipateButton.setTextColor(getResources().getColor(android.R.color.darker_gray));
                mPeopleNeededTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                mParticipateButton.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
                mPeopleNeededTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
            }
            mParticipateButton.setEnabled(false);



        } else {
            mParticipateButton.setEnabled(true);
            if (Build.VERSION.SDK_INT < 23){
                mParticipateButton.setTextColor(getResources().getColor(android.R.color.black));
                mPeopleNeededTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                mParticipateButton.setTextColor(getResources().getColor(android.R.color.black, null));
                mPeopleNeededTextView.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            }
        }
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
                        Constant.GAME_PROJECTION,
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
                        Constant.PARTICIPATE_PROJECTION,
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
                    String title = data.getString(Constant.COLUMN_GAME_NAME);
                    String time = data.getString(Constant.COLUMN_TIME);
                    String date = data.getString(Constant.COLUMN_DATE);
                    String location = data.getString(Constant.COLUMN_ADDRESS);
                    String desc = data.getString(Constant.COLUMN_DESCRIPTION);
                    int peopleNeeded = data.getInt(Constant.COLUMN_PEOPLE_NEEDED);
                    String peopleNeededStr;
                    if (peopleNeeded <= 1){
                        peopleNeededStr = mPersonStr;
                    } else {
                        peopleNeededStr = mPeopleStr;
                    }
                    String organizer = data.getString(Constant.COLUMN_USERNAME);
                    String sport = data.getString(Constant.COLUMN_SPORT_NAME);
                    double latitude = data.getDouble(Constant.COLUMN_LATITUDE);
                    double longitude = data.getDouble(Constant.COLUMN_LONGITUDE);
                    mMarkerLatitude = latitude;
                    mMarkerLongitude = longitude;

                    //add the marker
                    int color;
                    if (sport.equals(mBasketballQueryKey)){
                        color = Constant.GAME_BASKETBALL_COLOR;
                    } else if (sport.equals(mFootballQueryKey)){
                        color = Constant.GAME_FOOTBALL_COLOR;
                    } else if (sport.equals(mSoccerQueryKey)){
                        color = Constant.GAME_SOCCER_COLOR;
                    } else if (sport.equals(mTennisQueryKey)){
                        color = Constant.GAME_TENNIS_COLOR;
                    } else if (sport.equals(mVolleyballQueryKey)){
                        color = Constant.GAME_VOLLEYBALL_COLOR;
                    } else {
                        color = -1; //will pickup default color if not changed
                    }
                    addMarker(latitude, longitude, title, organizer, color);

                    //for sharing
                    mShareDate = date;
                    mShareTime = time;
                    mShareLocation = location;
                    mShareGameTitle = title;

                    //controls the number of people
                    mPeopleCount = peopleNeeded;

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

                    //check to see if peopleNeeded is zero and participate button text is participate
                    //then disable participate
                    checkPersonNeed(peopleNeeded);
                }
                break;
            }
            case PARTICIPATE_LOADER:{
                if (data.getCount() > 0 && data.moveToFirst()){
                    //Allow to remove Participation
                    mParticipateButton.setText(mParticipatingStr);
                    if (Build.VERSION.SDK_INT < 23){
                        mParticipateButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        mParticipateButton.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                    }

                } else {
                    //Allow to Participate
                    mParticipateButton.setText(mParticipateStr);
                    if (Build.VERSION.SDK_INT < 23){
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
