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
import android.widget.TextView;

import com.duse.android.connectandplay.Constant;
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

public class FootballFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.recycleview_football) RecyclerView mRecycleView;
    @BindString(R.string.football_query_key) String mFootballQueryKey;
    @BindView(R.id.empty_game_recycleview)TextView emptyTextView;

    private GameAdapter mGameAdapter;

    private static final int GAME_LOADER = 0;


    public FootballFragment(){
        //required empty constructor
    }

    /**
     * retains the instance
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * initializes the cursor loader
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(GAME_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * inflates the recycleview and the layout for this fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_football, container, false);
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

    /**
     * requests data from database for games
     * @param id
     * @param args
     * @return cursor loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long sportId = 0;
        Uri sportUri = GamesContract.SportEntry.CONTENT_URI;
        String sortOrder = GamesContract.SportEntry._ID + " ASC";
        Cursor sportCursor = getContext().getContentResolver().query(
                sportUri,
                new String[]{GamesContract.SportEntry._ID},
                GamesContract.SportEntry.COLUMN_SPORT_NAME + " = ? ",
                new String[]{mFootballQueryKey},
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
                Constant.GAME_PROJECTION,
                null,
                null,
                null);
    }

    /**
     * gets the data from the cursor and puts that into the adapter
     * @param loader
     * @param cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mGameAdapter.swapCursor(cursor);
        if (cursor.getCount() == 0){
            mRecycleView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * resets the adapter
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGameAdapter.swapCursor(null);
    }
}
