package com.codepath.apps.simpletweet.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gauri Gadkari on 4/2/17.
 */

public class DirectMessage {
    String imageUrl;
    String name;
    String screenName;
    String message;

    public String getCreatedAt() {
        return createdAt;
    }

    String createdAt;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getMessage() {
        return message;
    }
    public static DirectMessage fromJson(JSONObject jsonObject){
        DirectMessage directMessage = new DirectMessage();
        try {
            directMessage.name = jsonObject.getJSONObject("sender").getString("name");
            directMessage.screenName = jsonObject.getJSONObject("sender").getString("screen_name");
            directMessage.message = jsonObject.getString("text");
            directMessage.imageUrl =jsonObject.getJSONObject("sender").getString("profile_image_url");
            directMessage.createdAt = jsonObject.getString("created_at");
            //tweet.expandedMediaUrl =
            //Log.d("DEBUG", (jsonObject.getJSONObject("entities")).toString());
            //.getJSONArray("media")[0]);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return directMessage;
    }
    public static ArrayList<DirectMessage> fromJSONArray (JSONArray jsonArray){
        ArrayList<DirectMessage> directMessages = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                DirectMessage directMessage = DirectMessage.fromJson(tweetJson);
                if(directMessage != null) {
                    directMessages.add(directMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return directMessages;
    }

}
