package com.codepath.apps.simpletweet.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DirectMessageFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TwitterClient client;

    public DirectMessageFragment() {
        // Required empty public constructor
    }
    public static DirectMessageFragment newInstance(String param1, String param2) {
        DirectMessageFragment fragment = new DirectMessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        getDirectMessagesList();
    }

    private void getDirectMessagesList() {
        client.getDirectMessages(false, Long.valueOf(1), new JsonHttpResponseHandler(){

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_direct_message, container, false);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
