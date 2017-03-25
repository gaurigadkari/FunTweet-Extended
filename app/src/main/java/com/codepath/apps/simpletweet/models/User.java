package com.codepath.apps.simpletweet.models;

import com.codepath.apps.simpletweet.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Gauri Gadkari on 3/21/17.
 */
@Table(database = MyDatabase.class)
@Parcel(analyze={User.class})
public class User extends BaseModel {
    @Column
    public String name;
    @Column
    @PrimaryKey
    public long uid;
    @Column
    public String screenName;
    @Column
    public String profileImageUrl;

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

    User(){
    }

    public static User fromJson(JSONObject jsonObject){
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.uid = jsonObject.getLong("id");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
