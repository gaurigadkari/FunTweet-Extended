package com.codepath.apps.simpletweet.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Tweet> tweets  = new ArrayList<>();
    private TweetAdapter tweetAdapter;
    RecyclerView rvTimeline;
    private TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        rvTimeline = (RecyclerView) findViewById(R.id.searchTimeline);
        client = TwitterApplication.getRestClient();
        tweetAdapter = new TweetAdapter(this, tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTimeline.setLayoutManager(linearLayoutManager);
        showSearchResults(query);
        rvTimeline.setAdapter(tweetAdapter);
        //tweets = intent.getParcelableArrayListExtra("tweet");
    }
    void showSearchResults(String query){
    client.getSearchResults(query, false, Long.valueOf(1), new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
            Log.d("DEBUG", responseBody.toString());
            try {
                tweets.clear();
                //tweetAdapter.notifyDataSetChanged();
                tweets.addAll(Tweet.fromJSONArray(responseBody.getJSONArray("statuses")));
                Log.d("DEBUG",Tweet.fromJSONArray(responseBody.getJSONArray("statuses")).toString());
                tweetAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    });
    }
}
