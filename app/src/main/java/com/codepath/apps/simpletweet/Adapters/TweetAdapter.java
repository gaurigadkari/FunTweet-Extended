package com.codepath.apps.simpletweet.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweet.Activities.DetailActivity;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.Utils.PatternEditableBuilder;
import com.codepath.apps.simpletweet.models.Tweet;
import android.widget.MediaController;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by Gauri Gadkari on 3/21/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int simpleTweet= 0, tweetWithImage = 1, tweetWithVideo = 2;
    Context context;
    private ArrayList<Tweet> tweets;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case simpleTweet:
                View view1 = inflater.inflate(R.layout.simple_tweet, parent, false);
                viewHolder = new SimpleTweetViewHolder(view1);

                break;
            case tweetWithImage:
                View view2 = inflater.inflate(R.layout.tweet_image, parent, false);
                viewHolder = new TweetImageViewHolder(view2);
                break;
            case tweetWithVideo:
                View view3 = inflater.inflate(R.layout.tweet_video, parent, false);
                viewHolder = new TweetVideoViewHolder(view3);


        }
        return viewHolder;

//        View itemView = LayoutInflater.from(context).inflate(R.layout.article, parent, false);
//        return new ArticleViewHolder(itemView);
    }

    public TweetAdapter(Context context, ArrayList<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Tweet currentTweet = tweets.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("tweet", Parcels.wrap(currentTweet));
                context.startActivity(intent);

            }});

        switch (holder.getItemViewType()) {
            case simpleTweet:
                SimpleTweetViewHolder simpleHolder = (SimpleTweetViewHolder) holder;
                simpleHolder.name.setText(currentTweet.getUser().getName());
                simpleHolder.screenName.setText("@"+currentTweet.getUser().getScreenName());
                simpleHolder.timeCreated.setText(currentTweet.getCreatedAt());
                simpleHolder.tweet.setText(currentTweet.getBody());
                Glide.clear(simpleHolder.profilePic);
                simpleHolder.profilePic.setImageResource(0);
                String profilePicUrl = currentTweet.getUser().getProfileImageUrl();


                Glide.with(context).load(profilePicUrl).error(R.drawable.ic_launcher)
                        .bitmapTransform(new RoundedCornersTransformation(context, 5, 5))
                        .placeholder(R.drawable.ic_launcher)
                        .into(simpleHolder.profilePic);

                new PatternEditableBuilder().
                        addPattern(Pattern.compile("\\@(\\w+)"), R.color.twitterBlue,
                                new PatternEditableBuilder.SpannableClickedListener() {
                                    @Override
                                    public void onSpanClicked(String text) {
                                        Toast.makeText(context, "Clicked username: " + text,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).into(simpleHolder.tweet);

                new PatternEditableBuilder().
                        addPattern(Pattern.compile("\\#(\\w+)"), R.color.twitterBlue,
                                new PatternEditableBuilder.SpannableClickedListener() {
                                    @Override
                                    public void onSpanClicked(String text) {
                                        Toast.makeText(context, "Clicked username: " + text,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).into(simpleHolder.tweet);

                break;
            case tweetWithImage:
                TweetImageViewHolder imageHolder = (TweetImageViewHolder) holder;
                imageHolder.name.setText(currentTweet.getUser().getName());
                imageHolder.screenName.setText("@"+currentTweet.getUser().getScreenName());
                imageHolder.timeCreated.setText(currentTweet.getCreatedAt());
                imageHolder.tweet.setText(currentTweet.getBody());
                Log.d("DEBUG",currentTweet.getBody().toString());
                Glide.clear(imageHolder.profilePic);
                imageHolder.profilePic.setImageResource(0);
                String profilePicUrl1 = currentTweet.getUser().getProfileImageUrl();

                Glide.with(context).load(profilePicUrl1).error(R.drawable.ic_launcher)
                        .bitmapTransform(new RoundedCornersTransformation(context, 5, 5))
                        .placeholder(R.drawable.ic_launcher).into(imageHolder.profilePic);
                imageHolder.tweetImage.setImageResource(0);
                Glide.with(context).load(currentTweet.imageUrl)
                        //.bitmapTransform(new RoundedCornersTransformation(context, 10, 10))
                        .error(R.drawable.ic_launcher).into(imageHolder.tweetImage);
                break;
            //.placeholder(R.drawable.ic_launcher)
            case tweetWithVideo:
                TweetVideoViewHolder videoHolder = (TweetVideoViewHolder) holder;
                videoHolder.name.setText(currentTweet.getUser().getName());
                videoHolder.screenName.setText("@"+currentTweet.getUser().getScreenName());
                videoHolder.timeCreated.setText(currentTweet.getCreatedAt());
                videoHolder.tweet.setText(currentTweet.getBody());
                Glide.clear(videoHolder.profilePic);
                videoHolder.profilePic.setImageResource(0);
                String profilePicUrl2 = currentTweet.getUser().getProfileImageUrl();
                Glide.with(context).load(profilePicUrl2).error(R.drawable.ic_launcher)
                        .bitmapTransform(new RoundedCornersTransformation(context, 5, 5))
                        .placeholder(R.drawable.ic_launcher).into(videoHolder.profilePic);
                Uri uri = Uri.parse(currentTweet.getVideoUrl()); //Declare your url here.

                //for VideoView
//                videoHolder.tweetVideo.setMediaController(new MediaController(context));
//                videoHolder.tweetVideo.setVideoURI(uri);
//                videoHolder.tweetVideo.requestFocus();
//                videoHolder.tweetVideo.start();
                //Glide.with(context).load(currentTweet.videoUrl).into(videoHolder.tweetVideo);
                //videoHolder.tweetVideo.setVideoPath(currentTweet.getVideoUrl());
                Glide.with(context).load(currentTweet.getVideoImageUrl()).error(R.drawable.ic_launcher)
                        .bitmapTransform(new RoundedCornersTransformation(context, 5, 5))
                        .placeholder(R.drawable.ic_launcher).into(videoHolder.tweetVideo);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    @Override
    public int getItemViewType(int position) {

        // for now let all be of the simpletweet type, need to get back here later

        Tweet tweet = tweets.get(position);
        //Log.d("DEBUG",tweet.expandedMediaUrl.toString());
        if(tweet.expandedMediaUrl.contains("photo")){
            return 1;
        } else if(tweet.expandedMediaUrl.contains("video")){
            return 2;
        } else if(tweet.expandedMediaUrl.equals("")){
            return 0;
        }


        return -1;
    }

    public static class SimpleTweetViewHolder extends RecyclerView.ViewHolder {
        protected TextView tweet, name, screenName, timeCreated;
        //protected TextView webUrl;
        protected ImageView profilePic;

        public SimpleTweetViewHolder(View itemView) {
            super(itemView);
            tweet = (TextView) itemView.findViewById(R.id.tweet);
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screenName);
            timeCreated = (TextView) itemView.findViewById(R.id.time);

            //webUrl = (TextView) itemView.findViewById(R.id.webUrl);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);

        }
    }

    public static class TweetImageViewHolder extends RecyclerView.ViewHolder {
        protected TextView tweet, name, screenName, timeCreated;
        protected ImageView profilePic, tweetImage;

        public TweetImageViewHolder(View itemView) {
            super(itemView);
            tweet = (TextView) itemView.findViewById(R.id.tweet);
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screenName);
            timeCreated = (TextView) itemView.findViewById(R.id.time);

            //webUrl = (TextView) itemView.findViewById(R.id.webUrl);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            tweetImage = (ImageView) itemView.findViewById(R.id.image);

        }
    }
    public static class TweetVideoViewHolder extends RecyclerView.ViewHolder {
        protected TextView tweet, name, screenName, timeCreated;
        protected ImageView profilePic;
        protected ImageView tweetVideo;

        public TweetVideoViewHolder(View itemView) {
            super(itemView);
            tweet = (TextView) itemView.findViewById(R.id.tweet);
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screenName);
            timeCreated = (TextView) itemView.findViewById(R.id.time);

            //webUrl = (TextView) itemView.findViewById(R.id.webUrl);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            tweetVideo = (ImageView) itemView.findViewById(R.id.video);


        }
    }
}
