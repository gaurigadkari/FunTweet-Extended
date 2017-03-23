package com.codepath.apps.simpletweet.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static android.R.attr.id;
import static com.raizlabs.android.dbflow.config.FlowLog.Level.D;

/**
 * Created by Gauri Gadkari on 3/21/17.
 */

public class Tweet {
    DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    public Date date = new Date();
    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public String getCreatedAt() {
        Date created = null;
        String result;
        long diff = 0, diffSeconds = 0, diffMinutes = 0, diffHours = 0;
        try {
            created = dateFormat.parse(createdAt);
            diff = date.getTime() - created.getTime();
            diffSeconds = (diff / 1000) % 60;
            diffMinutes = (diff / (60 * 1000)) % 60;
            diffHours = (diff / (60 * 60 * 1000));
            Log.d("today", date.toString());
            //Log.d("Debud", diffSeconds + "secs" + diffMinutes + "mins" + diffHours + "hrs");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(diffHours == 0){
            if (diffMinutes == 0){
                if (diffSeconds ==0){
                    return "0 s";
                }
                else return diffSeconds + " s";
            }
            else return diffMinutes + " m";
        }
        else return diffHours + " h";
        //dateFormat.format(date);

    }

    private String body;
    private long id;
    private User user;
    private String createdAt;
    public User getUser() {
        return user;
    }

    public static ArrayList<Tweet> fromJSONArray (JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(tweetJson);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static Tweet fromJson(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.id = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }
}

