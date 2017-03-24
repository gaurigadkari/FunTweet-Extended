package com.codepath.apps.simpletweet.Activities;

import android.databinding.DataBindingUtil;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.databinding.ActivityDetailBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.User;

import org.parceler.Parcels;




public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        //User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("tweet"))
        ImageView profileImage = binding.profilePic;
        TextView name = binding.name;
        TextView screenName = binding.screenName;
        TextView tweetBody = binding.tweetBody;
        name.setText(tweet.getUser().getName());
        screenName.setText(tweet.getUser().getScreenName());
        tweetBody.setText(tweet.getBody());
        Glide.clear(profileImage);
        profileImage.setImageResource(0);
        String profilePicUrl = tweet.getUser().getProfileImageUrl();

        Glide.with(DetailActivity.this).load(profilePicUrl).error(R.drawable.ic_launcher).placeholder(R.drawable.ic_launcher).into(profileImage);

    }
}
