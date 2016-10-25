package com.duse.android.connectandplay;

import android.content.Context;
import android.graphics.drawable.Drawable;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class Utility {

    /**
     * Gets the id for the icon from the R file
     * @param context
     * @param sport
     * @return
     */
    public static int getIconId(Context context, String sport){
        if (sport.equals(context.getString(R.string.basketball_query_key))){
            return R.mipmap.ic_sport_basketball;
        } else if (sport.equals(context.getString(R.string.football_query_key))){
            return R.mipmap.ic_sport_football;
        } else if (sport.equals(context.getString(R.string.soccer_query_key))){
            return R.mipmap.ic_sport_soccer;
        } else if (sport.equals(context.getString(R.string.tennis_query_key))){
            return R.mipmap.ic_sport_tennis;
        } else if (sport.equals(context.getString(R.string.volleyball_query_key))){
            return R.mipmap.ic_sport_volleyball;
        }
        return -1;
    }
}
