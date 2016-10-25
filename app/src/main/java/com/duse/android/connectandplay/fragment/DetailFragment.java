package com.duse.android.connectandplay.fragment;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String DETAIL_URI = "URI";
    private Uri mUri;
    private static final int DETAIL_LOADER = 0; //inner join between game, user, and sport
    private static final int PARTICIPATE_LOADER = 1;

    //TextViews
    @BindView(R.id.game_title_text_view)TextView titleTextView;
    @BindView(R.id.game_date_time_text_view)TextView dateTimeTextView;
    @BindView(R.id.game_location_text_view)TextView locationTextView;
    @BindView(R.id.game_desc_text_view)TextView descTextView;
    @BindView(R.id.game_people_needed_text_view)TextView peopleNeededTextView;
    @BindView(R.id.organizer_text_view)TextView organizerTextView;
    //Buttons
    @BindView(R.id.game_participate_button)Button participateButton;
    @BindView(R.id.game_share_button)Button shareButton;
    //ImageButton
    @BindView(R.id.game_icon)ImageButton iconImageButton;

    //Strings
    @BindString(R.string.organized_by)String organizedByStr;
    @BindString(R.string.people_needed)String peopleStr;
    @BindString(R.string.person_needed)String personStr;
    @BindString(R.string.report_string)String reportStr;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.game_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_report) {
            Toast.makeText(getContext(), reportStr, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        mUri = getActivity().getIntent().getData();
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add function for participate
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add function for share
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(PARTICIPATE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

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
                        peopleNeededStr = personStr;
                    } else {
                        peopleNeededStr = peopleStr;
                    }
                    String organizer = data.getString(COLUMN_USERNAME);
                    String sport = data.getString(COLUMN_SPORT_NAME);

                    //sets image
                    int imageUrl = Utility.getIconId(getContext(), sport);
                    Picasso
                            .with(getContext())
                            .load(imageUrl)
                            .error(R.drawable.ic_placeholder)
                            .into(iconImageButton);

                    //sets texts for detail fragment
                    titleTextView.setText(title);
                    dateTimeTextView.setText(date + " | " + time);
                    locationTextView.setText(location);
                    descTextView.setText(desc);
                    peopleNeededTextView.setText(peopleNeeded + " " + peopleNeededStr);
                    organizerTextView.setText(organizedByStr + " " + organizer);

                }
                break;
            }
            case PARTICIPATE_LOADER:{
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
