package com.codepath.apps.simpletweet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.simpletweet.R.id.tweetBody;

/**
 * Created by Gauri Gadkari on 3/29/17.
 */

public class HomeTimelineFragment extends TweetListFragment {
    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();


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

        /*if (!(Utilities.isNetworkAvailable(getContext()) && Utilities.isOnline())) {
            ArrayList<Tweet> tweetList = (ArrayList<Tweet>) SQLite.select().
                    from(Tweet.class).queryList();

            //tweets.clear();
            tweetListFragment.addAllTweetsDB(tweetList);
            //tweets.addAll(tweetList);
            //Collections.reverse(tweets);
            //tweetAdapter.notifyDataSetChanged();
            //swipeContainer.setRefreshing(false);

        } */
        if (!true) {
        } else {
            client.getHomeTimeline(false, Long.valueOf(1), new JsonHttpResponseHandler() {
                //Success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline " + response.toString());
                    addAllTweets(Tweet.fromJSONArray(response));
//                    tweets.clear();
//                    tweetAdapter.notifyDataSetChanged();
//                    tweets.addAll(Tweet.fromJSONArray(response));
//                    tweetAdapter.notifyDataSetChanged();

                    //swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorObj) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline Error" + errorObj.toString());
                }
            });
        }
    }

    public void tweet(String tweetBody) {
        //client = TwitterApplication.getRestClient();
//        tweets = new ArrayList<>();
//        tweetAdapter = new TweetAdapter(this, tweets);
        client.postTweet(tweetBody, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TimelineActivity.class.getName(), "Post Tweet " + response.toString());
                //tweets.clear();
                //tweets.add(0, Tweet.fromJson(response));
                //tweetAdapter.notifyDataSetChanged();
                addTweet(Tweet.fromJson(response));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TimelineActivity.class.getName(), "Post Tweet Error " + errorResponse.toString());

            }
        });
    }
    public static HomeTimelineFragment newInstance(int page, String title) {
        HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        homeTimelineFragment.setArguments(args);
        return homeTimelineFragment;
    }
}

