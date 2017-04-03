package com.codepath.apps.simpletweet.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.models.PersonModel;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by gaurig on 4/2/17.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    Context context;
    ArrayList<PersonModel> personModels = new ArrayList<>();

    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PersonAdapter.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View personView = inflater.inflate(R.layout.person_item, parent, false);
        viewHolder = new PersonAdapter.ViewHolder(personView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PersonAdapter.ViewHolder holder, int position) {
        final PersonModel personModel = personModels.get(position);
        holder.name.setText(personModel.getName());
        holder.screenName.setText(personModel.getScreenName());
        holder.bio.setText(personModel.getBio());
        String profilePicUrl = personModel.getProfileImageUrl();
        Glide.with(context).load(profilePicUrl).error(R.drawable.ic_launcher)
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 5))
                .placeholder(R.drawable.ic_launcher)
                .into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return personModels.size();
    }

    public PersonAdapter(Context context, ArrayList<PersonModel> personModels) {
        this.context = context;
        this.personModels = personModels;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bio, name, screenName;
        public ImageView profilePic;
        public ViewHolder(View itemView) {
            super(itemView);
            bio = (TextView) itemView.findViewById(R.id.bio);
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screenName);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
        }
    }
}
