package com.codepath.apps.simpletweet.Activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.Fragments.ComposeTweet;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.ActivityTimelineBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.EndlessRecyclerViewScrollListener;
import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.simpletweet.R.id.fab;


public class TimelineActivity extends AppCompatActivity {
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    ArrayList<Tweet> tweets;
    private ActivityTimelineBinding binding;
    private TweetAdapter tweetAdapter;
    private static final String STATE_ITEMS = "items";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        populateTimeline();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        Toolbar toolbar = binding.toolbar;
                //(Toolbar) findViewById(R.id.toolbar);
                //
                //

        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            tweets = (ArrayList<Tweet>) savedInstanceState.get(STATE_ITEMS);
        }
        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        RecyclerView rvTimeline = binding.timeline;
                //(RecyclerView) findViewById(R.id.timeline);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTimeline.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        rvTimeline.addOnScrollListener(scrollListener);

        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(this, tweets);
        rvTimeline.setAdapter(tweetAdapter);

        FloatingActionButton fab = binding.fab;
                //
                //(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();

                ComposeTweet composeTweetFragment = new ComposeTweet();
                composeTweetFragment.show(fm, "Tweet");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_ITEMS, tweets);

    }
    private void loadNextDataFromApi(int page) {


    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            //Success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                tweets.addAll(Tweet.fromJSONArray(response));
                tweetAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
            }
        });
    }

    public void tweet(String tweetBody){
//        client = TwitterApplication.getRestClient();
//        tweets = new ArrayList<>();
//        tweetAdapter = new TweetAdapter(this, tweets);
          client.postTweet(tweetBody, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                tweets.add(0, Tweet.fromJson(response));
                tweetAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Log.d("DEBUG", errorResponse.toString());

            }
        });
    }

}
