package com.duse.android.connectandplay.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.fragment.BasketballFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/23/16.
 */

public class GameAdapter extends CursorAdapter {

    public GameAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view =  LayoutInflater.from(context).inflate(R.layout.card_view_games_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String title = cursor.getString(BasketballFragment.COLUMN_GAME_NAME);
        viewHolder.titleTextView.setText(title);

        String dateTime  = cursor.getString(BasketballFragment.COLUMN_DATE) + " | " +
                cursor.getString(BasketballFragment.COLUMN_TIME);
        viewHolder.dateTimeTextView.setText(dateTime);

        String location = cursor.getString(BasketballFragment.COLUMN_LOCATION);
        viewHolder.locationTextView.setText(location);

        String description = cursor.getString(BasketballFragment.COLUMN_DESCRIPTION);
        viewHolder.descriptionTextView.setText(description);

        int peopleNeeded = cursor.getInt(BasketballFragment.COLUMN_PEOPLE_NEEDED);
        viewHolder.peopleNeededTextView.setText(peopleNeeded);

        String organizer = cursor.getString(BasketballFragment.COLUMN_USERNAME);
        viewHolder.organizerTextView.setText(organizer);


    }
    public static class ViewHolder{
        @BindView(R.id.card_view_game_title_text_view) TextView titleTextView;
        @BindView(R.id.card_view_more_image_button) ImageButton moreImageButton;
        @BindView(R.id.card_view_game_date_time_text_view) TextView dateTimeTextView;
        @BindView(R.id.card_view_game_location_text_view) TextView locationTextView;
        @BindView(R.id.card_view_game_desc_text_view) TextView descriptionTextView;
        @BindView(R.id.card_view_game_people_needed_text_view) TextView peopleNeededTextView;
        @BindView(R.id.card_view_organizer_text_view) TextView organizerTextView;
        @BindView(R.id.card_view_game_participate_button) Button participateButton;
        @BindView(R.id.card_view_game_share_button) Button shareButton;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
