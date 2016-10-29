package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.fragment.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (savedInstanceState == null) {

            //add fragment to activity
            ProfileFragment fragment = new ProfileFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_profile, fragment)
                    .commit();

        }


    }



}

