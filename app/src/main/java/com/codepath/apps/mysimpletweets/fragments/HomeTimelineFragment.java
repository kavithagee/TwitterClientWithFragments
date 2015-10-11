package com.codepath.apps.mysimpletweets.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.PersistTweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

/**
 * Created by kavitha on 10/10/15.
 */
public class HomeTimelineFragment extends TweetsListFragments {

    private BroadcastReceiver timelineUpdateReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timelineUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TimelineActivity.ACTION_TIMELINE_NEEDS_UPDATE.equals(intent.getAction())) {
                    updateTimeline();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(timelineUpdateReceiver, new IntentFilter(TimelineActivity.ACTION_TIMELINE_NEEDS_UPDATE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.unregisterReceiver(timelineUpdateReceiver);
    }

    @Override
    public void getTimeline(TwitterClient client, boolean getNew, JsonHttpResponseHandler handler) {
        client.getHometime(getNew, handler);
    }

    @Override
    public List<PersistTweet> getTimelineFromDB() {
        return new Select().from(PersistTweet.class).orderBy("createdat DESC").limit(25).execute();
    }

    //    public void loadFromDB() {
//        List<PersistTweet> queryResults = new Select().from(PersistTweet.class)
//                .orderBy("createdat DESC").limit(25).execute();
//        for(int i =0; i < queryResults.size();i++) {
//            aTweets.add(Tweet.fromDB(queryResults.get(i)));
//        }
//        loading = false;
////        swipeContainer.setRefreshing(false);
//    }
}
