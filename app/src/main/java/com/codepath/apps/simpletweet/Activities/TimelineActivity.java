package com.codepath.apps.simpletweet.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;

import com.codepath.apps.simpletweet.DialogFragments.ComposeDialogFragment;

import com.codepath.apps.simpletweet.Fragments.TweetListFragment;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.ActivityTimelineBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeTweetListener, TweetListFragment.TweetListListener {
    private ActivityTimelineBinding binding;
    private TwitterClient client;
    TweetListFragment tweetListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        // Make sure to check whether returned data will be null.
        //String titleOfPage = intent.getStringExtra("title");
        String urlOfPage = intent.getStringExtra("url");
        //Uri imageUriOfPage = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (!(urlOfPage.equals("")))
            showComposeDialog(urlOfPage);


        //setContentView(R.layout.activity_timeline);


        //Log.d(TimelineActivity.class.getName(), tweetList.size()+"");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

// Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder, new TweetListFragment(), "TweetListFragment");
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commitNow();
        tweetListFragment = (TweetListFragment) getSupportFragmentManager().findFragmentByTag("TweetListFragment");

        //tweetListFragment = (TweetListFragment) binding.fragment1;
        //TweetListFragment.newInstance(TimelineActivity.this);

        populateTimeline();

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
                tweetListFragment.addTweet(Tweet.fromJson(response));


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TimelineActivity.class.getName(), "Post Tweet Error " + errorResponse.toString());

            }
        });
    }


    public void loadNextDataFromApi(int count, Long maxId) {
//        Long maxId = tweets.get(count - 1).getId();
        client.getHomeTimeline(true, maxId, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TimelineActivity.class.getName(), "loadNextDataFromApi " + response.toString());
                tweetListFragment.addAllTweets(Tweet.fromJSONArray(response));
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
                    tweetListFragment.addAllTweets(Tweet.fromJSONArray(response));
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

    private void showComposeDialog(String tweetBody) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(TimelineActivity.this, tweetBody);
        composeDialogFragment.setComposeText();
        composeDialogFragment.show(fm, "fragment_edit_name");
    }


    @Override
    public void tweetClickHandler(String tweetBody) {
        //TODO get this working
        //tweet(tweetBody);
    }
}
