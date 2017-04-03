package com.codepath.apps.simpletweet.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.Fragments.DirectMessageFragment;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.models.DirectMessage;
import com.codepath.apps.simpletweet.models.Tweet;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Gauri Gadkari on 4/2/17.
 */

public class DirectMessageAdapter extends RecyclerView.Adapter<DirectMessageAdapter.ViewHolder>{
    private ArrayList<DirectMessage> directMessages;
    Context context;

    public DirectMessageAdapter(Context context, ArrayList<DirectMessage> directMessages) {
        this.context = context;
        this.directMessages = directMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.direct_message_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DirectMessage directMessage = directMessages.get(position);
        holder.name.setText(directMessage.getName());
        holder.screenName.setText("@"+directMessage.getScreenName());
        //holder..setText(directMessage.getCreatedAt());
        holder.message.setText(directMessage.getMessage());
        Glide.clear(holder.profilePic);
        holder.profilePic.setImageResource(0);
        String profilePicUrl = directMessage.getImageUrl();
        Glide.with(context).load(profilePicUrl).error(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher)
                .into(holder.profilePic);
                //for round image fix error
//                        (new BitmapImageViewTarget(holder.profilePic) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(DirectMessage.this.getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        holder.profilePic.setImageDrawable(circularBitmapDrawable);
//                    }
//                });

    }

    @Override
    public int getItemCount() {
        return directMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView name;
        TextView screenName;
        TextView message;

        ViewHolder(View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            name = (TextView)itemView.findViewById(R.id.name);
            screenName = (TextView)itemView.findViewById(R.id.screenName);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

}
