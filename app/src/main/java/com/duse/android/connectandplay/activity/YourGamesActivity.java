package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.adapter.ViewPagerAdapter;
import com.duse.android.connectandplay.fragment.HostGamesFragment;
import com.duse.android.connectandplay.fragment.SavedGamesFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahesh Gaya on 10/23/16.
 */

public class YourGamesActivity extends AppCompatActivity{
    private static final String TAG = ExploreGamesActivity.class.getSimpleName();

    //Binding views
    @BindView(R.id.fab_add) FloatingActionButton mFab;
    @BindView(R.id.toolbar) Toolbar mToolbar; //action bar
    @BindView(R.id.tabs) TabLayout mTabLayout; //tabs
    @BindView(R.id.viewpager) ViewPager mViewPager; //page content

    //Binding strings
    @BindString(R.string.saved_games_label) String savedGamesLabel;
    @BindString(R.string.hosted_games_label) String hostedGamesLabel;



    /**
     * create and configure the views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the view layout
        setContentView(R.layout.activity_your_games);
        ButterKnife.bind(this);
        //creates the toolbar
        setSupportActionBar(mToolbar);
        //adds the close button
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //handles tabs and fragments
        setupViewPage(mViewPager);


        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:{
                        //if saved games, remove fab
                        mFab.hide();
                        mFab.setVisibility(View.GONE);
                        break;
                    }
                    case 1:{
                        mFab.show();
                        mFab.setVisibility(View.VISIBLE);
                        break;
                    }
                }
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
        adapter.addFrag(new SavedGamesFragment(), savedGamesLabel);
        adapter.addFrag(new HostGamesFragment(), hostedGamesLabel);
        viewPager.setAdapter(adapter);
    }
}
