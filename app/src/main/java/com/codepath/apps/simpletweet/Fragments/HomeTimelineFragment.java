package com.codepath.apps.simpletweet.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

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

import static com.codepath.apps.simpletweet.R.id.tweetBody;

/**
 * Created by Gauri Gadkari on 3/29/17.
 */

public class HomeTimelineFragment extends TweetListFragment {
    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    CoordinatorLayout coordinatorLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        //binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }

        });

        populateTimeline();

    }
//    public void loadNextDataFromApi(int count, Long maxId) {
////        Long maxId = tweets.get(count - 1).getId();
//        client.getHomeTimeline(true, maxId, new JsonHttpResponseHandler() {
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d(TimelineActivity.class.getName(), "loadNextDataFromApi " + response.toString());
//                //addAllTweets(Tweet.fromJSONArray(response));
//                //tweetListFragment.reloadRecylerView();
////                tweets.addAll(Tweet.fromJSONArray(response));
////                tweetAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Log.d(TimelineActivity.class.getName(), "loadNextDataFromApiError " + responseString);
//            }
//
//        });
//
//    }

    public void populateTimeline() {

        if(!(Utilities.isNetworkAvailable(getContext()) && Utilities.isOnline())) {
            ArrayList<Tweet> tweetList = (ArrayList<Tweet>) SQLite.select().
                    from(Tweet.class).queryList();

            //tweets.clear();
            addAllTweetsDB(tweetList);
            Snackbar.make(coordinatorLayout, "No Network, please connect to the internet", Snackbar.LENGTH_LONG).show();

            //tweets.addAll(tweetList);
            //Collections.reverse(tweets);
            //tweetAdapter.notifyDataSetChanged();
            //swipeContainer.setRefreshing(false);

        } else {
            //timelineActivity.
            //showProgressBar();
            client.getHomeTimeline(false, Long.valueOf(1), new JsonHttpResponseHandler() {
                //Success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline " + response.toString());
                    addAllTweets(Tweet.fromJSONArray(response), false);
                    //hideProgressBar();
//                    tweets.clear();
//                    tweetAdapter.notifyDataSetChanged();
//                    tweets.addAll(Tweet.fromJSONArray(response));
//                    tweetAdapter.notifyDataSetChanged();

                    //swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorObj) {
                    //Log.d("DEBUG", "Populate timeline Error" + errorObj.toString());
                    //hideProgressBar();
                }
            });
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        progressBar =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

    }

    public void showProgressBar() {
        // Show progress item
        //miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        //miActionProgressItem.setVisible(false);
    }

    public void tweet(String tweetBody) {
        showProgressBar();
        //client = TwitterApplication.getRestClient();
//        tweets = new ArrayList<>();
//        tweetAdapter = new TweetAdapter(this, tweets);
//        client.postTweet(tweetBody, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d(TimelineActivity.class.getName(), "Post Tweet " + response.toString());
//                //tweets.clear();
//                //tweets.add(0, Tweet.fromJson(response));
//                //tweetAdapter.notifyDataSetChanged();
//                addTweet(Tweet.fromJson(response));
//                hideProgressBar();
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d(TimelineActivity.class.getName(), "Post Tweet Error " + errorResponse.toString());
//                hideProgressBar();
//
//            }
//        });
    }
    public static HomeTimelineFragment newInstance(int page, String title) {
        HomeTimelineFragment fragmentFirst = new HomeTimelineFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void loadNextDataFromApi(int count, Long maxId) {
        Log.d("HELLO","HOME");
        client.getHomeTimeline(true, maxId, new JsonHttpResponseHandler() {
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

