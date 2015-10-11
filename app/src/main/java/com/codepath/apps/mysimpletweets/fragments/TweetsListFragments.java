package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.PersistTweet;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kavitha on 10/6/15.
 */
abstract public class TweetsListFragments extends Fragment {
    private ListView lvTweets;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private SwipeRefreshLayout swipeContainer;

    private TwitterClient client;
    private boolean loading = false;
    private boolean reachedEnd = false;
    private int visibleThreshold = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweet_list, null);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                ;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (reachedEnd || !isNetworkAvailable() || totalItemCount <= 0) {
                    return;
                }

                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!loading && ((totalItemCount - lastVisibleItem) <= visibleThreshold)) {
                    populateTimeline(false);
                }
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //        send API req
                resetScreen();
                populateTimeline(true);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter( getActivity(), tweets);

        resetScreen();
        populateTimeline(true);
    }

    public void addAll(List <Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    private void resetScreen() {
        aTweets.clear();
        this.loading = false;
    }

    private void populateTimeline(boolean getNew) {
        if (loading) {
            return;
        }
        loading = true;
        if (!isNetworkAvailable()) {
            loadFromDB();
            return;
        }
        final boolean isNew = getNew;
        getTimeline(client, getNew, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json, isNew);
                addAll(tweets);
                Log.v("DEBUG", json.toString());
                reachedEnd = tweets.size() <= 0;
                loading = false;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.v("DEBUG", "Failure", throwable);
                loading = false;
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void updateTimeline() {
        aTweets.clear();
        resetScreen();
        populateTimeline(true);
    }

    private void loadFromDB() {
        List<PersistTweet> queryResults = getTimelineFromDB();
        if (queryResults != null) {
            for(int i =0; i < queryResults.size();i++) {
                aTweets.add(Tweet.fromDB(queryResults.get(i)));
            }
        }
        loading = false;
        if (swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    abstract public void getTimeline(TwitterClient client, boolean getNew, JsonHttpResponseHandler handler);

    abstract public List<PersistTweet> getTimelineFromDB();
}
