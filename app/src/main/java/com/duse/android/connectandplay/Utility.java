package com.duse.android.connectandplay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

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

    /**
     * Normalizes the time. Converts 24hr into 12hr
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static String[] normalizeTime(int hourOfDay, int minute){
        int hour;
        String amPmStr;
        if (hourOfDay > 12){
            hour = hourOfDay - 12 ;
            amPmStr = "PM";
        } else if (hourOfDay == 0) {
            hour = 12;
            amPmStr = "AM";
        } else {
            hour = hourOfDay;
            amPmStr = "AM";
        }

        String hourStr;
        if (hour < 10){
            hourStr = "0" + hour;
        } else {
            hourStr = String.valueOf(hour);
        }
        String minuteStr;
        if (minute < 10){
            minuteStr = "0" + minute;
        } else {
            minuteStr = String.valueOf(minute);
        }
        return new String[]{hourStr, minuteStr, amPmStr};
    }

    /**
     * normalize Date
     * @param month
     * @param dayOfMonth
     * @param year
     * @return date in full, i.e. October 31, 2016
     */
    public static String[] normalizeDate(int month, int dayOfMonth, int year){
        String monthStr = "";
        switch (month){
            case Calendar.JANUARY:{
                monthStr = "January";
                break;
            }
            case Calendar.FEBRUARY:{
                monthStr = "February";
                break;
            }
            case Calendar.MARCH:{
                monthStr = "March";
                break;
            }
            case Calendar.APRIL:{
                monthStr = "April";
                break;
            }
            case Calendar.MAY:{
                monthStr = "May";
                break;
            }
            case Calendar.JUNE:{
                monthStr = "June";
                break;
            }
            case Calendar.JULY:{
                monthStr = "July";
                break;
            }
            case Calendar.AUGUST:{
                monthStr = "August";
                break;
            }
            case Calendar.SEPTEMBER:{
                monthStr = "September";
                break;
            }
            case Calendar.OCTOBER:{
                monthStr = "October";
                break;
            }
            case Calendar.NOVEMBER:{
                monthStr = "November";
                break;
            }
            case Calendar.DECEMBER:{
                monthStr = "December";
                break;
            }

        }
        String dayOfMonthStr = String.valueOf(dayOfMonth);
        String yearStr = String.valueOf(year);
        return new String[]{monthStr, dayOfMonthStr, yearStr};
    }

}
