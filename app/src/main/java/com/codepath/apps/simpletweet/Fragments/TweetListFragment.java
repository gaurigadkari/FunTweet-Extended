package com.codepath.apps.simpletweet.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.Utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.loopj.android.http.AsyncHttpClient.log;

//import com.codepath.apps.simpletweet.databinding.ActivityTimelineBinding;

/**
 * Created by Gauri Gadkari on 3/27/17.
 */

public class TweetListFragment extends Fragment {
    private EndlessRecyclerViewScrollListener scrollListener;
    TweetListListener listener;
    private SwipeRefreshLayout swipeContainer;
    ArrayList<Tweet> tweets  = new ArrayList<>();
    private TweetAdapter tweetAdapter;
    private static final String STATE_ITEMS = "items";
    RecyclerView rvTimeline;
    private TwitterClient client;
    Activity activity;
    TimelineActivity timelineActivity;
    MenuItem miActionProgressItem;
    ProgressBar progressBar;
    public void addAllTweetsDB(ArrayList<Tweet> tweetsDB) {
        tweets.clear();
        tweets.addAll(tweetsDB);
        Collections.reverse(tweets);
        tweetAdapter.notifyDataSetChanged();
        swipeRefresh(false);
    }

    public void swipeRefresh(Boolean refresh) {
        //swipeContainer.setRefreshing(refresh);
    }

    public void addAllTweets(ArrayList<Tweet> tweetList, Boolean isMoreLoaded) {
        if(!isMoreLoaded){
            tweets.clear();
        }
        tweetAdapter.notifyDataSetChanged();
        tweets.addAll(tweetList);
        log.d("DEBUG", tweets.toString());
        tweetAdapter.notifyDataSetChanged();
        swipeRefresh(false);
    }

    public void loadNextDataFromApi(int count, Long maxId) {
//        Long maxId = tweets.get(count - 1).getId();
//        client.getHomeTimeline(true, maxId, new JsonHttpResponseHandler() {
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d(TimelineActivity.class.getName(), "loadNextDataFromApi " + response.toString());
//                addAllTweets(Tweet.fromJSONArray(response), true);
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

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        progressBar =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //final TimelineActivity activity = (TimelineActivity) getActivity();
        activity = getActivity();

        //timelineActivity = (TimelineActivity) getActivity();


        tweetAdapter = new TweetAdapter(activity, tweets);
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//        //binding.swipeContainer;
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                activity.populateTimeline();
//            }
//
//        });
        rvTimeline = (RecyclerView) view.findViewById(R.id.timeline);
        //= binding.timeline;
        //(RecyclerView) findViewById(R.id.timeline);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTimeline.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Long maxId = (tweets.get(totalItemsCount - 1).getId()) - 1;
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(totalItemsCount, maxId);
            }
        };
        rvTimeline.addOnScrollListener(scrollListener);


        rvTimeline.setAdapter(tweetAdapter);
        //activity.populateTimeline();
        tweetAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        if (savedInstanceState != null) {
            tweets = Parcels.unwrap(savedInstanceState.getParcelable(STATE_ITEMS));
            tweetAdapter = new TweetAdapter(getActivity(), tweets);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_ITEMS, Parcels.wrap(tweets));

    }

    public static TweetListFragment newInstance(TweetListListener listener) {
        TweetListFragment tweetListFragment = new TweetListFragment();
        tweetListFragment.listener = listener;
        return tweetListFragment;
    }


    public void addTweet(Tweet tweet) {
        tweets.add(0, tweet);
        tweetAdapter.notifyDataSetChanged();
    }
    public void reloadRecylerView() {
    }

    public interface TweetListListener {
        public void tweetClickHandler(String tweetBody);
    }

}
