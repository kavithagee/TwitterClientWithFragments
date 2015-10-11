package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;

import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.PersistTweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.Collections;
import java.util.List;

/**
 * Created by kavitha on 10/10/15.
 */
public class UserTimelineFragment extends TweetsListFragments {

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserTimelineFragment newInstance(Long userId) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("user_id", userId);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public void getTimeline(TwitterClient client, boolean getNew, JsonHttpResponseHandler handler) {
        Long userId = getArguments().getLong("user_id");
        client.getUserTimeline(getNew, userId, handler);
    }

    @Override
    public List<PersistTweet> getTimelineFromDB() {
        return Collections.emptyList();
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
