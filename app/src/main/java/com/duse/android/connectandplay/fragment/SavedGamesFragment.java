package com.duse.android.connectandplay.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
 * Created by Mahesh Gaya on 10/23/16.
 */

public class SavedGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.recycleview_saved_games)RecyclerView mRecycleView;
    @BindView(R.id.empty_game_recycleview)TextView emptyTextView;
    private static final int SAVED_LOADER = 0;

    private GameAdapter mGameAdapter;

    public SavedGamesFragment(){
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
        getLoaderManager().initLoader(SAVED_LOADER, null, this);
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
        View rootView = inflater.inflate(R.layout.fragment_saved_games, container, false);
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
     * gets participate data from table
     * @param id
     * @param args
     * @return cursor loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                GamesContract.ParticipateEntry.buildParticipateGamesUri(),
                Constant.GAME_PROJECTION,
                null,
                null,
                null
        );
    }

    /**
     * swaps data to adapter
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
