package com.codepath.apps.simpletweet.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.Fragments.TweetListFragment;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.ActivityUserProfileBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.loopj.android.http.AsyncHttpClient.log;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;


public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    private TwitterClient client;
    String screenName;
    TweetListFragment fragment;
    ArrayList<Tweet> tweets  = new ArrayList<>();
    private TweetAdapter tweetAdapter;
    RecyclerView rvTimeline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        Intent intent = getIntent();
        screenName = intent.getStringExtra("screenName");
        getUserInfo(screenName);
        tweetAdapter = new TweetAdapter(this, tweets);
        rvTimeline = (RecyclerView) findViewById(R.id.userTimeline);
        rvTimeline.setAdapter(tweetAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvTimeline.setLayoutManager(linearLayoutManager);

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.your_placeholder, new TweetListFragment());
//        ft.commit();
        populateUserTimeline(screenName);
    }

    public void getUserInfo(String screenName){
        client.getUserDetails(false, screenName, new JsonHttpResponseHandler() {

            //Success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "UserInfo " + response.toString());
                ImageView background = binding.profileBackground;
                ImageView profilePic = binding.profilePic;
                TextView name = binding.name;
                TextView screenName = binding.screenName;
                TextView bio = binding.bio;
                TextView location = binding.location;
                TextView birthday = binding.birthday;
                TextView following = binding.following;
                TextView followers = binding.followers;

                try {
                    String profilePicUrl = response.getString("profile_image_url");
                    background.setBackgroundColor((Color.parseColor("#"+response.getString("profile_background_color"))));
                    name.setText(response.getString("name"));
                    screenName.setText("@"+response.getString("screen_name"));
                    bio.setText(response.getString("description"));
                    bio.setVisibility(View.VISIBLE);
                    location.setText(response.getString("location"));
                    following.setText(response.getString("friends_count"));
                    followers.setText(response.getString("followers_count"));

                    //birthday.setText();
                    Glide.with(UserProfileActivity.this).load(profilePicUrl).error(R.drawable.ic_launcher).placeholder(R.drawable.ic_launcher)
                            .bitmapTransform(new RoundedCornersTransformation(UserProfileActivity.this, 10, 10)).into(profilePic);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                    tweets.clear();
//                    tweetAdapter.notifyDataSetChanged();
//                    tweets.addAll(Tweet.fromJSONArray(response));
//                    tweetAdapter.notifyDataSetChanged();

                //swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorObj) {
                Log.d("DEBUG", "UserInfo Error" + errorObj.toString());
            }
        });
    }
//    public void addAllTweets(ArrayList<Tweet> tweetList) {
//        tweets.clear();
//        tweetAdapter.notifyDataSetChanged();
//        tweets.addAll(tweetList);
//        log.d("DEBUG", tweets.toString());
//        tweetAdapter.notifyDataSetChanged();
//        swipeRefresh(false);
//    }
    public void populateUserTimeline(String screenName) {

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
//        if (!true) {
//        } else {
            client.getUserTimeline(false, Long.valueOf(1), screenName, new JsonHttpResponseHandler() {
                //Success

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline " + response.toString());
                    //addAllTweets(Tweet.fromJSONArray(response));
                    tweets.clear();
                    tweetAdapter.notifyDataSetChanged();
                    tweets.addAll(Tweet.fromJSONArray(response));
                    tweetAdapter.notifyDataSetChanged();

                    //swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorObj) {
                    Log.d(TimelineActivity.class.getName(), "Populate timeline Error" + errorObj.toString());
                }
            });

    }
}
