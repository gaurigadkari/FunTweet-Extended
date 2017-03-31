package com.codepath.apps.simpletweet.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

//import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweet.DialogFragments.ComposeDialogFragment;

import com.codepath.apps.simpletweet.Fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweet.Fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweet.Fragments.TweetListFragment;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
//import com.codepath.apps.simpletweet.databinding.ActivityTimelineBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.simpletweet.R.id.fab;


public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeTweetListener, TweetListFragment.TweetListListener {
    //private ActivityTimelineBinding binding;
    HomeTimelineFragment homeTimelineFragment;
    FragmentPagerAdapter adapterViewPager;
    PagerTabStrip slidingTabStrip;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // Make sure to check whether returned data will be null.
        //String titleOfPage = intent.getStringExtra("title");
        String urlOfPage = intent.getStringExtra("url");
        //Uri imageUriOfPage = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (!(urlOfPage.equals("")))
            showComposeDialog(urlOfPage);
        setContentView(R.layout.activity_timeline);
        //Log.d(TimelineActivity.class.getName(), tweetList.size()+"");
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                //binding.toolbar;
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                //binding.fab;
        //(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();

                ComposeDialogFragment composeTweetFragment = ComposeDialogFragment.newInstance(TimelineActivity.this);
                composeTweetFragment.show(fm, "Tweet");
            }
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.your_placeholder, new HomeTimelineFragment(), "HomeTimelineFragment");
//        ft.commitNow();
        homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag("HomeTimelineFragment");
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        //slidingTabStrip = (PagerTabStrip) findViewById(R.id.tabs);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
        //slidingTabStrip.setViewPager(vpPager);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.your_placeholder, new TweetListFragment(), "TweetListFragment");
//        ft.commitNow();
//        tweetListFragment = (TweetListFragment) getSupportFragmentManager().findFragmentByTag("TweetListFragment");
        //tweetListFragment = (TweetListFragment) binding.fragment1;
        //TweetListFragment.newInstance(TimelineActivity.this);
    }

    private void showComposeDialog(String tweetBody) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(TimelineActivity.this, tweetBody);
        composeDialogFragment.setComposeText();
        composeDialogFragment.show(fm, "fragment_edit_name");
    }


    @Override
    public void tweetClickHandler(String tweetBody) {
        //TODO get this working
        //tweet(tweetBody);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return HomeTimelineFragment.newInstance(1, "Home");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return MentionsTimelineFragment.newInstance(1, "Mentions");
//                case 2: // Fragment # 1 - This will show SecondFragment
//                    return SecondFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Mentions";
            }
            return "Page " + position;
        }


    }

}
