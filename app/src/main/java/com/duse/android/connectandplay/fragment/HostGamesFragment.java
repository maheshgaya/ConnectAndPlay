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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.adapter.DividerItemDecoration;
import com.duse.android.connectandplay.adapter.GameAdapter;
import com.duse.android.connectandplay.data.GamesContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/23/16.
 */

public class HostGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = HostGamesFragment.class.getSimpleName();

    @BindView(R.id.recycleview_hosted_games)RecyclerView mRecycleView;
    @BindView(R.id.empty_game_recycleview)TextView emptyTextView;
    private GameAdapter mGameAdapter;

    private static final int GAME_LOADER = 0;

    public HostGamesFragment(){
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
     * initializes the loader
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(GAME_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * initializes the views
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hosted_games, container, false);
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
     * Queries the content provider to get all the games
     * created by the current user
     * CURRENT USER is 1, other USERS are 0 for the user.current_user column in the database
     * @param id
     * @param args
     * @return cursor Loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long userId = 0;
        Uri userUri = GamesContract.UserEntry.CONTENT_URI;
        String sortOrder = GamesContract.UserEntry._ID + " ASC";
        Cursor userCursor = getContext().getContentResolver().query(
                userUri,
                new String[]{GamesContract.UserEntry._ID},
                GamesContract.UserEntry.COLUMN_CURRENT_USER + " = ? ",
                new String[]{"1"},
                sortOrder
        );

        try {
            if (userCursor.moveToFirst()) {
                int userIdIndex = userCursor.getColumnIndex(GamesContract.SportEntry._ID);
                userId = userCursor.getLong(userIdIndex);
            }
        } finally {
            userCursor.close();
        }

        Uri gameUri = GamesContract.GameEntry.buildGameUserUri(userId);

        return new CursorLoader(getActivity(),
                gameUri,
                Constant.GAME_PROJECTION,
                null,
                null,
                null);
    }

    /**
     * swaps data to the adapter and then checks if the adapter is empty or not
     * if it is then show the empty error message
     * @param loader
     * @param cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished: " + cursor.getCount());
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
