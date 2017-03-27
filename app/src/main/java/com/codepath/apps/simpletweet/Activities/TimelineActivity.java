package com.codepath.apps.simpletweet.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.Fragments.ComposeDialogFragment;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweet.Utils.Utilities;
import com.codepath.apps.simpletweet.databinding.ActivityTimelineBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import cz.msebera.android.httpclient.Header;

import static android.media.CamcorderProfile.get;


public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeTweetListener{
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
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        intent.getStringExtra("Hello");


                // Make sure to check whether returned data will be null.
                //String titleOfPage = intent.getStringExtra("title");
                String urlOfPage = intent.getStringExtra("url");
                //Uri imageUriOfPage = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if(!(urlOfPage.equals("")))
                showComposeDialog(urlOfPage);



            //setContentView(R.layout.activity_timeline);
         client = TwitterApplication.getRestClient();

        //Log.d(TimelineActivity.class.getName(), tweetList.size()+"");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        Toolbar toolbar = binding.toolbar;
                //(Toolbar) findViewById(R.id.toolbar);
                //
                //

        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            tweets = Parcels.unwrap(savedInstanceState.getParcelable(STATE_ITEMS));
        }
        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //Toast.makeText(TimelineActivity.this, "hello", Toast.LENGTH_LONG).show();
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline();
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
                loadNextDataFromApi(totalItemsCount);
            }
        };
        rvTimeline.addOnScrollListener(scrollListener);

        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(this, tweets);
        rvTimeline.setAdapter(tweetAdapter);
        populateTimeline();
        FloatingActionButton fab = binding.fab;
                //
                //(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();

                ComposeDialogFragment composeTweetFragment = ComposeDialogFragment.newInstance(TimelineActivity.this);
                composeTweetFragment.show(fm, "Tweet");
            }
        });


    }
    private void showComposeDialog(String tweetBody) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(TimelineActivity.this, tweetBody);
        composeDialogFragment.setComposeText();
        composeDialogFragment.show(fm, "fragment_edit_name");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_ITEMS, Parcels.wrap(tweets));

    }
    private void loadNextDataFromApi(int count) {
        Long maxId = tweets.get(count-1).getId();
        client.getHomeTimeline(true, maxId, new JsonHttpResponseHandler(){
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

    private void populateTimeline() {

        if (!(Utilities.isNetworkAvailable(this) && Utilities.isOnline())) {
            List<Tweet> tweetList = SQLite.select().
                    from(Tweet.class).queryList();

            //tweets.clear();
            tweets.addAll(tweetList);
            Collections.reverse(tweets);
            tweetAdapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);

        } else {
            client.getHomeTimeline(false, Long.valueOf(1), new JsonHttpResponseHandler() {
            //Success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                tweets.clear();
                tweetAdapter.notifyDataSetChanged();
                tweets.addAll(Tweet.fromJSONArray(response));
                tweetAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorObj) {
                //Log.d("DEBUG", errorObj.toString());
            }
        });
    }
    }

    public void tweet(String tweetBody){
        //client = TwitterApplication.getRestClient();
//        tweets = new ArrayList<>();
//        tweetAdapter = new TweetAdapter(this, tweets);
          client.postTweet(tweetBody, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                //tweets.clear();
                tweets.add(0, Tweet.fromJson(response));
                tweetAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Log.d("DEBUG", errorResponse.toString());

            }
        });
    }

    @Override
    public void tweetClickHandler(String tweetBody) {
        tweet(tweetBody);
    }
}
