package com.codepath.apps.simpletweet.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweet.Adapters.DirectMessageAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.models.DirectMessage;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.simpletweet.R.id.container;

public class DirectMessageFragment extends Fragment {
    private ArrayList<DirectMessage> directMessages;
    private DirectMessageAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private TwitterClient client;
    private RecyclerView rvDirectMessage;

    public DirectMessageFragment() {
        // Required empty public constructor
    }
    public static DirectMessageFragment newInstance(int page, String title) {
        DirectMessageFragment fragmentThird = new DirectMessageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentThird.setArguments(args);
        return fragmentThird;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        directMessages = new ArrayList<>();
        adapter = new DirectMessageAdapter(getContext(), directMessages);


    }

    private void getDirectMessagesList() {

        client.getDirectMessages(false, Long.valueOf(1), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Direct Message", response.toString());
                directMessages.clear();
                directMessages.addAll(DirectMessage.fromJSONArray(response));
                adapter.notifyDataSetChanged();
//                int size = response.length();
//                for(int i = 0 ; i < size; i++) {
//                    try {
//                        directMessages.add(response.getJSONObject(i));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("Direct Message", errorResponse.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_direct_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvDirectMessage = (RecyclerView) view.findViewById(R.id.directMessageRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvDirectMessage.setLayoutManager(linearLayoutManager);
        rvDirectMessage.setAdapter(adapter);

        getDirectMessagesList();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
