package com.duse.android.connectandplay.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.duse.android.connectandplay.Constant;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.data.GamesContract;
import com.duse.android.connectandplay.sync.GameSyncAdapter;

/**
 * Created by Mahesh Gaya on 10/29/16.
 */

public class LaunchActivity extends AppCompatActivity {

    /**
     * Splash screen
     * then go to Explore Activity if current user already exists
     * else go to Profile Activity for user to create a profile
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        GameSyncAdapter.initializeSyncAdapter(this);
        int userId = 0;
        Cursor userCursor = this.getContentResolver().query(GamesContract.UserEntry.CONTENT_URI,
                Constant.USER_PROJECTION,
                GamesContract.UserEntry.COLUMN_CURRENT_USER + " = ? ",
                new String[]{"1"},
                null);

        if (userCursor.moveToFirst()) {
            final android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do nothing for 3s
                    //intent to start ExploreActivity
                    Intent intent = new Intent(getApplicationContext(), ExploreGamesActivity.class);
                    //start ExploreActivity
                    LaunchActivity.this.startActivity(intent);

                    //remove activity from history stack
                    LaunchActivity.this.finish();
                }
            }, 3000);
        } else {
            final android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do nothing for 3s
                    //intent to start User Profile Activity
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    //start Profile Activity
                    LaunchActivity.this.startActivity(intent);

                    //remove activity from history stack
                    LaunchActivity.this.finish();
                }
            }, 3000);
        }



    }
}
