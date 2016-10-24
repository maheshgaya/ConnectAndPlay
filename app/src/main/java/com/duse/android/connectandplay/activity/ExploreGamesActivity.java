package com.duse.android.connectandplay.activity;

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

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.adapter.ViewPagerAdapter;
import com.duse.android.connectandplay.fragment.BasketballFragment;
import com.duse.android.connectandplay.fragment.FootballFragment;
import com.duse.android.connectandplay.fragment.SoccerFragment;
import com.duse.android.connectandplay.fragment.TennisFragment;
import com.duse.android.connectandplay.fragment.VolleyballFragment;

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
        setContentView(R.layout.activity_explore_games);
        ButterKnife.bind(this);

        //creates the toolbar
        setSupportActionBar(mToolbar);

        //adds the close button
        getSupportActionBar().setHomeAsUpIndicator(closeIcon);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //handles tabs and fragments
        setupViewPage(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
