package com.codepath.apps.simpletweet.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gauri Gadkari on 3/21/17.
 */

public class User {
    private String name;
    private long uid;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    private String screenName;
    private String profileImageUrl;

    public static User fromJson(JSONObject jsonObject){
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.uid = jsonObject.getLong("id");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
