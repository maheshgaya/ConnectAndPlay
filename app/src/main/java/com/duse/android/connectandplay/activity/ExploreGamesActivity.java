package com.duse.android.connectandplay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.duse.android.connectandplay.R;
import com.duse.android.connectandplay.fragment.BasketballFragment;
import com.duse.android.connectandplay.fragment.FootballFragment;
import com.duse.android.connectandplay.fragment.SoccerFragment;
import com.duse.android.connectandplay.fragment.TennisFragment;
import com.duse.android.connectandplay.fragment.VolleyballFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh Gaya on 10/17/16.
 * Tutorial: <a href="http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/">Android Hive</a>
 */

public class ExploreGamesActivity extends AppCompatActivity{
    private Toolbar mToolbar; //action bar
    private TabLayout mTabLayout; //tabs
    private ViewPager mViewPager; //page content

    /**
     * create and configure the views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the view layout
        setContentView(R.layout.activity_explore_games);

        //creates the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //adds the close button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //handles tabs and fragments
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPage(mViewPager);

        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * Setup the tabs in the layout
     * @param viewPager
     */
    private void setupViewPage(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BasketballFragment(), getResources().getString(R.string.basketball_label));
        adapter.addFrag(new FootballFragment(), getResources().getString(R.string.football_label));
        adapter.addFrag(new SoccerFragment(), getResources().getString(R.string.soccer_label));
        adapter.addFrag(new TennisFragment(), getResources().getString(R.string.tennis_label));
        adapter.addFrag(new VolleyballFragment(), getResources().getString(R.string.volleyball_label));
        viewPager.setAdapter(adapter);
    }

    /**
     * Class to handle the ViewPagers(tabs)
     */
    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
