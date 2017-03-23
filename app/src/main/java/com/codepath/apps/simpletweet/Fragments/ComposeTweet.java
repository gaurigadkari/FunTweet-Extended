package com.codepath.apps.simpletweet.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.TweetComposeBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Gauri Gadkari on 3/21/17.
 */

public class ComposeTweet extends DialogFragment {
    TweetComposeBinding binding;
    TwitterClient twitterClient;
    ArrayList<Tweet> tweets;
    TimelineActivity timelineActivity;
    private TweetAdapter tweetAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

        getDialog().getWindow().setAttributes(p);
        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        binding = DataBindingUtil.inflate(inflater, R.layout.tweet_compose, container, false);
        View view = binding.getRoot();
        return view;

    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        twitterClient = new TwitterClient(getContext());
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getContext(), tweets);
        final EditText composeTweet = binding.composeTweet;
        Button btnTweet = binding.btnTweet;
        final TextView charactersRemaining = binding.charactersRemaining;
        composeTweet.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                int charRemaining = 140 - s.length();
                charactersRemaining.setText(charRemaining + "");
                //charactersRemaining.setText(140 - s.toString().length());
            }
        });
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timelineActivity = new TimelineActivity();
                String tweetBody = composeTweet.getText().toString();
                timelineActivity.tweet(tweetBody);

                getDialog().dismiss();
            }
        });
    }
}
