package com.codepath.apps.simpletweet.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.simpletweet.Adapters.PersonAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.models.PersonModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowersActivity extends AppCompatActivity {
    private TwitterClient client;
    private ArrayList<PersonModel> personModels = new ArrayList<>();
    RecyclerView followersList;
    PersonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        client = TwitterApplication.getRestClient();
        client.getFollowersIds(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Follower Object", response.toString());
                String userIds="";
                try {
                    userIds = response.getString("ids").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                client.getUserLookup(userIds, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("Followers Array", response.toString());
                        personModels.addAll(PersonModel.fromJSONArray(response));
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        followersList = (RecyclerView) findViewById(R.id.followersList);
        adapter = new PersonAdapter(this, personModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        followersList.setLayoutManager(linearLayoutManager);
        followersList.setAdapter(adapter);
    }
}
