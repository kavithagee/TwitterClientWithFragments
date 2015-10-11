package com.codepath.apps.mysimpletweets.activities;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.ComposeDialog;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragments;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.Listener {

    public static final String ACTION_TIMELINE_NEEDS_UPDATE = "timeline.needs.update";

    private TweetsListFragments tweetsListFragments;

    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = false;
    // Sets the starting page index
    private int startingPageIndex = 0;
    private int itemCount = 0;
    private int itemsPerPage = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setCustomView(R.layout.view_logo);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        slidingTabStrip.setViewPager(viewPager);

        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("tweets.db").create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onComposeAction(MenuItem mi) {
        FragmentManager fm = TimelineActivity.this.getFragmentManager();
        ComposeDialog composeDialog = new ComposeDialog();
        composeDialog.setListener(TimelineActivity.this);
        composeDialog.show(fm, "settings_dialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentStatePagerAdapter {

        private final String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    @Override
    public void updateTimeline() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(new Intent(ACTION_TIMELINE_NEEDS_UPDATE));
    }
}
