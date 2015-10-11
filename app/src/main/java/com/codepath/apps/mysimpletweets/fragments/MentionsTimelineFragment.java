package com.codepath.apps.mysimpletweets.fragments;

import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.PersistTweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.Collections;
import java.util.List;

/**
 * Created by kavitha on 10/10/15.
 */
public class MentionsTimelineFragment extends TweetsListFragments {

    @Override
    public void getTimeline(TwitterClient client, boolean getNew, JsonHttpResponseHandler handler) {
        client.getMentionsHometime(getNew, handler);
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
