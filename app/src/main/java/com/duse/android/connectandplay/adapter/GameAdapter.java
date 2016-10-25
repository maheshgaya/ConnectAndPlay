package com.duse.android.connectandplay.adapter;

import android.content.Context;
import android.database.Cursor;
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
import com.duse.android.connectandplay.fragment.BasketballFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/24/16.
 */

public class GameAdapter extends CursorRecyclerViewAdapter<GameAdapter.ViewHolder> {
    public GameAdapter(Context context, Cursor cursor){
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final int position = cursor.getPosition() + 1;
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
                //TODO open detail view
                Toast.makeText(view.getContext(), position + " is tapped", Toast.LENGTH_SHORT).show();
            }
        });

//        String description = cursor.getString(BasketballFragment.COLUMN_DESCRIPTION);
//        viewHolder.descriptionTextView.setText(description);

//        int peopleNeeded = cursor.getInt(BasketballFragment.COLUMN_PEOPLE_NEEDED);
//        String peopleNeededStr;
//        if (peopleNeeded <= 1){
//            peopleNeededStr = peopleNeeded + viewHolder.personStr;
//        } else {
//            peopleNeededStr = peopleNeeded + viewHolder.peopleStr;
//        }
//        viewHolder.peopleNeededTextView.setText(peopleNeededStr);
//
//        String organizer = viewHolder.organizedByStr + cursor.getString(BasketballFragment.COLUMN_USERNAME);
//        viewHolder.organizerTextView.setText(organizer);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_games_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindString(R.string.organized_by) String organizedByStr;
        @BindString(R.string.people_needed) String peopleStr;
        @BindString(R.string.person_needed) String personStr;

        @BindView(R.id.card_view_game_title_text_view) TextView titleTextView;
        @BindView(R.id.card_view_game_date_time_text_view) TextView dateTimeTextView;
        @BindView(R.id.card_view_game_location_text_view) TextView locationTextView;
//        @BindView(R.id.card_view_game_desc_text_view) TextView descriptionTextView;
//        @BindView(R.id.card_view_game_people_needed_text_view) TextView peopleNeededTextView;
//        @BindView(R.id.card_view_organizer_text_view) TextView organizerTextView;
//        @BindView(R.id.card_view_game_participate_button)Button participateButton;
//        @BindView(R.id.card_view_game_share_button) Button shareButton;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
