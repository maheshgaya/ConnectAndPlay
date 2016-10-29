package com.duse.android.connectandplay.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.adapter.ViewPagerAdapter;
import com.duse.android.connectandplay.fragment.BasketballFragment;
import com.duse.android.connectandplay.fragment.FootballFragment;
import com.duse.android.connectandplay.fragment.SoccerFragment;
import com.duse.android.connectandplay.fragment.TennisFragment;
import com.duse.android.connectandplay.fragment.VolleyballFragment;
import com.duse.android.connectandplay.sync.GameSyncAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/17/16.
 * Tutorial: <a href="http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/">Android Hive</a>
 */

public class ExploreGamesActivity extends AppCompatActivity{
    private static final String TAG = ExploreGamesActivity.class.getSimpleName();
    //Binding views
    @BindView(R.id.toolbar)  Toolbar mToolbar; //action bar
    @BindView(R.id.tabs) TabLayout mTabLayout; //tabs
    @BindView(R.id.viewpager) ViewPager mViewPager; //page content
    @BindView(R.id.fab_add) FloatingActionButton mFab; //fab

    //Binding drawables
    @BindDrawable(R.drawable.ic_close) Drawable closeIcon;
    @BindDrawable(R.drawable.ic_actionbar)Drawable logo;

    //Binding strings
    @BindString(R.string.basketball_label) String basketballLabel;
    @BindString(R.string.football_label) String footballLabel;
    @BindString(R.string.soccer_label) String soccerLabel;
    @BindString(R.string.tennis_label) String tennisLabel;
    @BindString(R.string.volleyball_label) String volleyballLabel;

    /**
     * create and configure the views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the view layout
        GameSyncAdapter.initializeSyncAdapter(this);
        setContentView(R.layout.activity_explore_games);
        ButterKnife.bind(this);

        //creates the toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(logo);
        getSupportActionBar().setTitle("");
        //handles tabs and fragments
        setupViewPage(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createGameIntent = new Intent(getApplicationContext(), CreateGameActivity.class);
                startActivity(createGameIntent);
            }
        });


    }

    public void updateGames(){
        GameSyncAdapter.syncImmediately(this);
    }

    /**
     * Inflates menus for this class
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.explore_menu, menu);
        return true;
    }

    /**
     * Adds logic to the menus for this class
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.pref_general.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_saved_games) {
            Intent intent = new Intent(this, YourGamesActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_profile){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_map){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.action_refresh){
            updateGames();
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup the tabs in the layout
     * @param viewPager
     */
    private void setupViewPage(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BasketballFragment(), basketballLabel);
        adapter.addFrag(new FootballFragment(), footballLabel);
        adapter.addFrag(new SoccerFragment(), soccerLabel);
        adapter.addFrag(new TennisFragment(), tennisLabel);
        adapter.addFrag(new VolleyballFragment(), volleyballLabel);
        viewPager.setAdapter(adapter);
    }


}
