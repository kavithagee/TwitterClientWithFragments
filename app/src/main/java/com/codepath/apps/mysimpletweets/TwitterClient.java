package com.codepath.apps.mysimpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "6BDL84zGIOL896af7PavZ2EOh";       // Change this
	public static final String REST_CONSUMER_SECRET = "qbkXYfd4InEmeaws035fXn55OSYmmEPRJQCvO6xsLfbt63M900"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cp3simpletweets"; // Change this (here and in manifest)
	public static long beginSinceId = 1l;
	public static long endSinceId = 0l;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

//hometimeline
//	GET statuses/home_timeline.json
//			count=25, since_id=1

	public void getHometime(boolean bGetNew, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (!!bGetNew) {
			;
//			params.put("since_id", beginSinceId);
		} else {
			params.put("max_id", endSinceId);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String status, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		RequestHandle rh = getClient().post(apiUrl, params, handler);
	}

	public void getMentionsHometime(boolean bGetNew, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (!!bGetNew) {
			;
//			params.put("since_id", beginSinceId);
		} else {
			params.put("max_id", endSinceId);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(boolean bGetNew, Long userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (!!bGetNew) {
			;
//			params.put("since_id", beginSinceId);
		} else {
			params.put("max_id", endSinceId);
		}
		if (userId > 0) {
			params.put("user_id", userId);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getCurrentUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, null, handler);
	}

	public void getUserInfo(Long userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", userId);
		getClient().get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}