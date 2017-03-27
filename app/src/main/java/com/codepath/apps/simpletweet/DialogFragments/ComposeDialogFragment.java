package com.codepath.apps.simpletweet.DialogFragments;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.TweetComposeBinding;
import com.codepath.apps.simpletweet.models.Tweet;

import java.util.ArrayList;


/**
 * Created by Gauri Gadkari on 3/21/17.
 */

public class ComposeDialogFragment extends DialogFragment {
    TweetComposeBinding binding;
    ComposeTweetListener listener;
    TwitterClient twitterClient;
    ArrayList<Tweet> tweets;
    TimelineActivity timelineActivity;
    private TweetAdapter tweetAdapter;
    String tweetBody = "";
    static EditText composeTweet;
    Context context;
    public interface ComposeTweetListener {
        public void tweetClickHandler(String tweetBody);
    }

    @Override
    public void onStart() {
        super.onStart();
        //getDialog().getWindow().setWindowAnimations(
               // R.style.dialog_slide_animation);
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        tweetBody = composeTweet.getText().toString();
        if(tweetBody.equals("")){
            dialog.dismiss();
        } else {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage("Save Draft?");
        alertDialog.setCancelable(false);
        //Toast.makeText(getContext(), "Save", Toast.LENGTH_LONG).show();

        alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tweet", "");
                editor.commit();
                dismiss();

            }
        });

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tweet", tweetBody);
                editor.commit();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }}


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
        composeTweet = binding.composeTweet;
        Button btnTweet = binding.btnTweet;

        final TextView charactersRemaining = binding.charactersRemaining;
        context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String draftTweet = sharedPreferences.getString("tweet", "");
        composeTweet.setText(tweetBody);
        if(!(draftTweet.equals(""))){
            composeTweet.setText(draftTweet);
            int charRemaining = 140 - composeTweet.getText().length();
            charactersRemaining.setText(charRemaining + "");
        }

        composeTweet.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(composeTweet, 0);
            }
        });
        composeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                int charRemaining = 140 - s.length();
                charactersRemaining.setText(charRemaining + "");
                //charactersRemaining.setText(140 - s.toString().length());
            }
        });
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tweetBody = composeTweet.getText().toString();
                listener.tweetClickHandler(tweetBody);

                getDialog().dismiss();
            }
        });
    }

    public void setComposeText(){

    }

    public static ComposeDialogFragment newInstance(ComposeTweetListener listener) {
        ComposeDialogFragment composeDialogFragment = new ComposeDialogFragment();
        composeDialogFragment.listener = listener;
        return composeDialogFragment;
    }
    public static ComposeDialogFragment newInstance(ComposeTweetListener listener, String tweetBody) {
        ComposeDialogFragment composeDialogFragment = new ComposeDialogFragment();
        composeDialogFragment.listener = listener;
        composeDialogFragment.tweetBody = tweetBody;
        return composeDialogFragment;
    }
}
