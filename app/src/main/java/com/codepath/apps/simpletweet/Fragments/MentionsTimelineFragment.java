package com.codepath.apps.simpletweet.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import static com.codepath.apps.simpletweet.R.id.coordinatorLayout;

/**
 * Created by Gauri Gadkari on 3/30/17.
 */

public class MentionsTimelineFragment extends TweetListFragment {

    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        String title = getArguments().getString("someTitle","");

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

            if(!(Utilities.isNetworkAvailable(getContext()) && Utilities.isOnline())) {
            ArrayList<Tweet> tweetList = (ArrayList<Tweet>) SQLite.select().
                    from(Tweet.class).queryList();
                Snackbar.make(rvTimeline, "No Network, please connect to the internet", Snackbar.LENGTH_LONG).show();
            //tweets.clear();
            //addAllTweetsDB(tweetList);
            //tweets.addAll(tweetList);
            //Collections.reverse(tweets);
            //tweetAdapter.notifyDataSetChanged();
            //swipeContainer.setRefreshing(false);

        } else {
            client.getMentionsTimeline(false, Long.valueOf(1), new JsonHttpResponseHandler() {
                //Success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline " + response.toString());
                    addAllTweets(Tweet.fromJSONArray(response),false);
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

    public void tweet(String tweetBody) {
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
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d(TimelineActivity.class.getName(), "Post Tweet Error " + errorResponse.toString());
//
//            }
//        });
    }
    public static MentionsTimelineFragment newInstance(int page, String title) {
        MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        mentionsTimelineFragment.setArguments(args);
        return mentionsTimelineFragment;
    }

    @Override
    public void loadNextDataFromApi(int count, Long maxId) {
        Log.d("Hello", "Mentions");
        client.getMentionsTimeline(true, maxId, new JsonHttpResponseHandler() {
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
