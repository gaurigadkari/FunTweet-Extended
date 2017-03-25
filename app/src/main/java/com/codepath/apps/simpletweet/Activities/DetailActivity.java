package com.codepath.apps.simpletweet.Activities;

import android.databinding.DataBindingUtil;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.ActivityDetailBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    Long tweetID;
    private TwitterClient client;
    Tweet tweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        //User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("tweet"))
        ImageView profileImage = binding.profilePic;
        TextView name = binding.name;
        TextView screenName = binding.screenName;
        TextView tweetBody = binding.tweetBody;
        ImageView tweetImage = binding.tweetImage;
        VideoView tweetVideo = binding.tweetVideo;
        Button btnReply = binding.btnReply;
        final EditText replyTweet = binding.replyText;
        Button btnSendReply = binding.btnSendReply;
        final RelativeLayout replyContainer = binding.replyContainer;
        ImageView profileImageReply = binding.profilePicReply;
        TextView nameReply = binding.nameReply;
        TextView screenNameReply = binding.screenNameReply;
        TextView tweetBodyReply = binding.tweetBodyReply;
        ImageView tweetImageReply = binding.tweetImageReply;
        VideoView tweetVideoReply = binding.tweetVideoReply;
        RelativeLayout tweetReplyContainer = binding.tweetReplyContainer;
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyContainer.setVisibility(View.VISIBLE);


            }
        });
        btnSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply = replyTweet.getText().toString();
                replyToTweet(reply);
            }
        });
        name.setText(tweet.getUser().getName());
        screenName.setText(tweet.getUser().getScreenName());
        tweetBody.setText(tweet.getBody());
        Glide.clear(profileImage);
        profileImage.setImageResource(0);
        String profilePicUrl = tweet.getUser().getProfileImageUrl();

        Glide.with(DetailActivity.this).load(profilePicUrl).error(R.drawable.ic_launcher).placeholder(R.drawable.ic_launcher).into(profileImage);

        if(!(tweet.getImageUrl().equals(null))){
            Glide.with(DetailActivity.this).load(tweet.getImageUrl()).into(tweetImage);
        }

        tweetID = tweet.getId();
    }

    private void replyToTweet(String reply) {
        client.postReplyTweet(tweet.getUser().getScreenName(), tweetID, reply, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

}
