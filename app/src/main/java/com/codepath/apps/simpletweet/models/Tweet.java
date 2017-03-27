package com.codepath.apps.simpletweet.models;

import android.util.Log;

import com.codepath.apps.simpletweet.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static android.media.CamcorderProfile.get;

/**
 * Created by Gauri Gadkari on 3/21/17.
 */
@Table(database = MyDatabase.class)
@Parcel(analyze={Tweet.class})
public class Tweet extends BaseModel {
    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    @Column
    public String videoImageUrl = "";
    @Column
    public String imageUrl = "";
    @Column
    public String videoUrl = "";
    @Column
    public String body;
    @Column
    @PrimaryKey
    public long id;
    @Column
    @ForeignKey
    @ForeignKeyReference(columnName = "uid", foreignKeyColumnName = "uid")
    public User user;
    @Column
    public String createdAt;
    @Column
    public String expandedMediaUrl = "";


    public String getType() {
        return type;
    }

    public String type = "simple";


    public String getVideoUrl() {
        return videoUrl;
    }
    public String getBody() {
        return body;
    }
    public long getId() {
        return id;
    }
    public Tweet(){
    }
    public String getCreatedAt() {
        Date created = null;
        String result;
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date = new Date();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getExpandedMediaUrl() {
        return expandedMediaUrl;
    }

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
            //tweet.expandedMediaUrl =
            //Log.d("DEBUG", (jsonObject.getJSONObject("entities")).toString());
                    //.getJSONArray("media")[0]);
            if((jsonObject.getJSONObject("entities").has("media"))){
                //Log.d("Debug",((jsonObject.getJSONObject("entities").getJSONArray("media").get(0)).toString()));
                JSONArray media =jsonObject.getJSONObject("extended_entities").getJSONArray("media");
                tweet.expandedMediaUrl = media.getJSONObject(0).getString("expanded_url");
                String mediaUrl = media.getJSONObject(0).getString("media_url");
                tweet.type = media.getJSONObject(0).getString("type");
                //Log.d("Debug",url.toString());

                if(tweet.type.equals("photo")) {
                    tweet.imageUrl = mediaUrl;
                }
                if(tweet.type.equals("video")){
                    String url = media.getJSONObject(0).getJSONObject("video_info").getJSONArray("variants").getJSONObject(0).getString("url");
                    tweet.videoUrl = url;
                    tweet.videoImageUrl = mediaUrl;
                }
            }
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }
}

