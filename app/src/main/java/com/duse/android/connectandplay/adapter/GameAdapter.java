package com.duse.android.connectandplay.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.activity.DetailActivity;
import com.duse.android.connectandplay.data.GamesContract;
import com.duse.android.connectandplay.fragment.BasketballFragment;
import com.duse.android.connectandplay.fragment.DetailFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/24/16.
 */

public class GameAdapter extends CursorRecyclerViewAdapter<GameAdapter.ViewHolder> {
    private Context mContext;
    public GameAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final int position = cursor.getPosition() + 1;
        final long gameId = cursor.getLong(BasketballFragment.COLUMN_GAME_ID);
        String title = cursor.getString(BasketballFragment.COLUMN_GAME_NAME);
        viewHolder.titleTextView.setText(title);

        String dateTime  = cursor.getString(BasketballFragment.COLUMN_DATE) + " | " +
                cursor.getString(BasketballFragment.COLUMN_TIME);
        viewHolder.dateTimeTextView.setText(dateTime);

        String location = cursor.getString(BasketballFragment.COLUMN_LOCATION);
        viewHolder.locationTextView.setText(location);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri detailUri = GamesContract.GameEntry.buildGameUri(gameId);
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.setData(detailUri);
                mContext.startActivity(detailIntent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_games_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //Strings
        @BindString(R.string.organized_by) String organizedByStr;
        @BindString(R.string.people_needed) String peopleStr;
        @BindString(R.string.person_needed) String personStr;
        //TextViews
        @BindView(R.id.card_view_game_title_text_view) TextView titleTextView;
        @BindView(R.id.card_view_game_date_time_text_view) TextView dateTimeTextView;
        @BindView(R.id.card_view_game_location_text_view) TextView locationTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
