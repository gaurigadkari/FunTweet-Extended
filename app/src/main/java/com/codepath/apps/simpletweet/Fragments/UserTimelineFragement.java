package com.codepath.apps.simpletweet.Fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.Utils.Utilities;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gauri Gadkari on 4/2/17.
 */

public class UserTimelineFragement extends TweetListFragment {
    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    String screenName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        int SomeInt = getArguments().getInt("someInt", 0);
        String title = getArguments().getString("someTitle", "");
        screenName = getArguments().getString("screenName", "");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        //binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }

        });
        populateTimeline();

    }

    public void populateTimeline() {

        if (!(Utilities.isNetworkAvailable(getContext()) && Utilities.isOnline())) {
            ArrayList<Tweet> tweetList = (ArrayList<Tweet>) SQLite.select().
                    from(Tweet.class).queryList();

            //tweets.clear();
            addAllTweetsDB(tweetList);
            //tweets.addAll(tweetList);
            //Collections.reverse(tweets);
            //tweetAdapter.notifyDataSetChanged();
            //swipeContainer.setRefreshing(false);

        } else {
            client.getUserTimeline(false, Long.valueOf(1), screenName, new JsonHttpResponseHandler() {
                //Success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline " + response.toString());
                    addAllTweets(Tweet.fromJSONArray(response), false);
//                    tweets.clear();
//                    tweetAdapter.notifyDataSetChanged();
//                    tweets.addAll(Tweet.fromJSONArray(response));
//                    tweetAdapter.notifyDataSetChanged();

                    //swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorObj) {
                    //Log.d(TimelineActivity.class.getName(), "Populate timeline Error" + errorObj.toString());
                }
            });
        }
    }



    public static UserTimelineFragement newInstance(String screenName, int page, String title) {
        UserTimelineFragement userTimelineFragement = new UserTimelineFragement();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        userTimelineFragement.setArguments(args);

        return userTimelineFragement;
    }

    @Override
    public void loadNextDataFromApi(int count, Long maxId) {
        Log.d("Hello", "UserTimeline");
        client.getUserTimeline(true, maxId, screenName, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TimelineActivity.class.getName(), "loadNextDataFromApi " + response.toString());
                addAllTweets(Tweet.fromJSONArray(response), true);
                //tweetListFragment.reloadRecylerView();
//                tweets.addAll(Tweet.fromJSONArray(response));
//                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TimelineActivity.class.getName(), "loadNextDataFromApiError " + responseString);
            }

        });
    }

}



