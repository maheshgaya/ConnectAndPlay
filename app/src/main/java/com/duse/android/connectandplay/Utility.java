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

    public static String[] normalizeTime(int hourOfDay, int minute){
        int hour;
        String amPmStr;
        if (hourOfDay > 12){
            hour = hourOfDay - 12;
            amPmStr = "PM";
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


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
