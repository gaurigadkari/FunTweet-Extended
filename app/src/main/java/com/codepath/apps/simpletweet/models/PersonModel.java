package com.codepath.apps.simpletweet.models;

import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gaurig on 4/2/17.
 */

public class PersonModel extends BaseModel {


    public String name;

    public String screenName;

    public String profileImageUrl;

    public String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public static ArrayList<PersonModel> fromJSONArray (JSONArray jsonArray){
        ArrayList<PersonModel> personModels = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject personJson = jsonArray.getJSONObject(i);
                PersonModel personModel = PersonModel.fromJson(personJson);
                if(personModel != null) {
                    personModels.add(personModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return personModels;
    }

    public static PersonModel fromJson(JSONObject jsonObject){
        PersonModel personModel = new PersonModel();
        try {
            personModel.setName(jsonObject.getString("name"));
            personModel.setScreenName(jsonObject.getString("screen_name"));
            personModel.setBio(jsonObject.getString("description"));
            personModel.setProfileImageUrl(jsonObject.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return personModel;
    }
}
