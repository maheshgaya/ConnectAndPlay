package com.duse.android.connectandplay.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.adapter.DividerItemDecoration;
import com.duse.android.connectandplay.adapter.GameAdapter;
import com.duse.android.connectandplay.data.GamesContract;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/17/16.
 */

public class BasketballFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.recycleview_basketball)  RecyclerView mRecycleView;
    @BindString(R.string.basketball_query_key) String mBasketballQueryKey;


    private GameAdapter mGameAdapter;

    private static final int GAME_LOADER = 0;


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


    private static final String[] PARTICIPATE_PROJECTION = {
            GamesContract.ParticipateEntry.TABLE_NAME + "." + GamesContract.ParticipateEntry._ID,
            GamesContract.ParticipateEntry.COLUMN_GAME_ID
    };

    public static final int COLUMN_PARTICIPATE_ID = 0;
    public static final int COLUMN_PARTICIPATE_GAME_ID = 1;

    public BasketballFragment(){
        //required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(GAME_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_basketball, container, false);
        ButterKnife.bind(this, rootView);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        mGameAdapter = new GameAdapter(getContext(), null);
        mRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearTrailerLayoutManager = new LinearLayoutManager(getContext());
        linearTrailerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearTrailerLayoutManager);
        mRecycleView.setAdapter(mGameAdapter);
        mRecycleView.addItemDecoration(itemDecoration);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long sportId = 0;
        Uri sportUri = GamesContract.SportEntry.CONTENT_URI;
        String sortOrder = GamesContract.SportEntry._ID + " ASC";
        Cursor sportCursor = getContext().getContentResolver().query(
                sportUri,
                new String[]{GamesContract.SportEntry._ID},
                GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                new String[]{mBasketballQueryKey},
                sortOrder
        );

        try {
            if (sportCursor.moveToFirst()) {
                int sportIdIndex = sportCursor.getColumnIndex(GamesContract.SportEntry._ID);
                sportId = sportCursor.getLong(sportIdIndex);
            }
        } finally {
            sportCursor.close();
        }

        Uri gameUri = GamesContract.GameEntry.buildGameSportUri(sportId);

        return new CursorLoader(getActivity(),
                gameUri,
                GAME_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mGameAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGameAdapter.swapCursor(null);
    }
}
