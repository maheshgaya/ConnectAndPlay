package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.fragment.CreateGameFragment;
import com.duse.android.connectandplay.fragment.DetailFragment;

/**
 * Created by Mahesh Gaya on 10/25/16.
 */

public class CreateGameActivity extends AppCompatActivity {
    /**
     * adds Create Game Fragment to the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        if (savedInstanceState == null) {

            //add fragment to activity
            CreateGameFragment fragment = new CreateGameFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_create_game, fragment)
                    .commit();

        }
    }
}
