package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.fragment.AboutFragment;
import com.duse.android.connectandplay.fragment.DetailFragment;

/**
 * Created by earleyneverlate on 10/24/16.
 */

public class AboutActivity extends AppCompatActivity {

    /**
     * adds About Fragment to the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (savedInstanceState == null) {

            //add fragment to activity
            AboutFragment fragment = new AboutFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_about, fragment)
                    .commit();

        }

    }
}
