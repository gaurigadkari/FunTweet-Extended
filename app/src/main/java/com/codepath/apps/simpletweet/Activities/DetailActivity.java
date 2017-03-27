package com.codepath.apps.simpletweet.Activities;

import android.databinding.DataBindingUtil;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.ActivityDetailBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.simpletweet.R.id.charactersRemaining;


public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    Long tweetID;
    private TwitterClient client;
    Tweet tweet;
    ArrayList<Tweet> replyTweets;
    private TweetAdapter replyTweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        //User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("tweet"))
        ImageView profileImage = binding.profilePic;
        TextView name = binding.name;
        TextView screenName = binding.screenName;
        TextView tweetBody = binding.tweetBody;
        ImageView tweetImage = binding.tweetImage;
        VideoView tweetVideo = binding.tweetVideo;
        ImageView btnReply = binding.btnReply;
        final EditText replyTweet = binding.replyText;
        final TextView charactersRemaining = binding.charactersRemaining;
        Button btnSendReply = binding.btnSendReply;
        final RelativeLayout replyContainer = binding.replyContainer;
//        ImageView profileImageReply = binding.profilePicReply;
//        TextView nameReply = binding.nameReply;
//        TextView screenNameReply = binding.screenNameReply;
//        TextView tweetBodyReply = binding.tweetBodyReply;
//        ImageView tweetImageReply = binding.tweetImageReply;
//        VideoView tweetVideoReply = binding.tweetVideoReply;
//        RelativeLayout tweetReplyContainer = binding.tweetReplyContainer;
        final RecyclerView recyclerView = binding.replies;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        replyTweets = new ArrayList<>();
        replyTweetAdapter = new TweetAdapter(this, replyTweets);
        recyclerView.setAdapter(replyTweetAdapter);
        replyTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                int charRemaining = 140 - s.length();
                charactersRemaining.setText(charRemaining + "");
                //charactersRemaining.setText(140 - s.toString().length());
            }
        });

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyContainer.setVisibility(View.VISIBLE);
                Boolean enable = replyTweet.isEnabled();
                replyTweet.setText("@" + tweet.getUser().getScreenName() +" ");
                Log.d("Debug", enable.toString());

            }
        });
        btnSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply = replyTweet.getText().toString();
                recyclerView.setVisibility(View.VISIBLE);
                replyToTweet(reply);
                replyTweet.setText("@" + tweet.getUser().getScreenName()+" ");
            }
        });
        name.setText(tweet.getUser().getName());
        screenName.setText("@"+tweet.getUser().getScreenName());
        tweetBody.setText(tweet.getBody());
        Glide.clear(profileImage);
        profileImage.setImageResource(0);
        String profilePicUrl = tweet.getUser().getProfileImageUrl();

        Glide.with(DetailActivity.this).load(profilePicUrl).error(R.drawable.ic_launcher).placeholder(R.drawable.ic_launcher)
                .bitmapTransform(new RoundedCornersTransformation(DetailActivity.this, 5, 5)).into(profileImage);

        if(!(tweet.getImageUrl().equals(""))){
            tweetImage.setVisibility(View.VISIBLE);
            Glide.with(DetailActivity.this).load(tweet.getImageUrl())
                    .into(tweetImage);
        }


        tweetID = tweet.getId();
    }

    private void replyToTweet(String reply) {
        client.postReplyTweet(tweet.getUser().getScreenName(), tweetID, reply, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                //tweets.clear();
                replyTweets.add(Tweet.fromJson(response));
                replyTweetAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Log.d("DEBUG", errorResponse.toString());

            }
        });

    }

}
