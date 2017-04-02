package com.codepath.apps.simpletweet.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.SearchView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.DialogFragments.ComposeDialogFragment;
import com.codepath.apps.simpletweet.Fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweet.Fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweet.Fragments.TweetListFragment;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterApplication;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.ActivityTimelineBinding;
import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.gravity;
import static com.raizlabs.android.dbflow.config.FlowLog.Level.I;

//import com.astuetz.PagerSlidingTabStrip;


public class TimelineActivity extends BaseActivity implements ComposeDialogFragment.ComposeTweetListener {
    private ActivityTimelineBinding binding;
    HomeTimelineFragment homeTimelineFragment;
    FragmentPagerAdapter adapterViewPager;
    Toolbar toolbar;
    private TwitterClient client;
    TabLayout tabLayout;
    private DrawerLayout drawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        client = TwitterApplication.getRestClient();
        // Make sure to check whether returned data will be null.
        //String titleOfPage = intent.getStringExtra("title");
        String urlOfPage = intent.getStringExtra("url");
        //Uri imageUriOfPage = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (!(urlOfPage.equals("")))
            showComposeDialog(urlOfPage);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setUpDrawerContent(nvDrawer);

        drawerToggle = setUpDrawerToggle();

        drawer.addDrawerListener(drawerToggle);
        //drawer.closeDrawer(Gravity.NO_GRAVITY);
        //nvDrawer.getMenu().getItem(0).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.viewpager, new HomeTimelineFragment()).commit();
        setTitle(R.string.home);


        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();

                ComposeDialogFragment composeTweetFragment = ComposeDialogFragment.newInstance(TimelineActivity.this);
                composeTweetFragment.show(fm, "Tweet");
            }
        });
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.your_placeholder, new HomeTimelineFragment(), "HomeTimelineFragment");
//        ft.commitNow();
        homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag("HomeTimelineFragment");
        ViewPager vpPager = binding.viewpager;
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        tabLayout = binding.slidingTabs;
        tabLayout.setupWithViewPager(vpPager);

        getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.action_settings);
        //final SearchView filterview = (SearchView) MenuItemCompat.getActionView(filterItem);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView.SearchAutoComplete searchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //List<String> items = Lists.newArrayList(suggestions);
//        items = new ArrayList<String>();
//        stringSuggestionAdapter = new SuggestionAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
//        searchSrcTextView.setThreshold(0);
//        searchSrcTextView.setAdapter(stringSuggestionAdapter);
        //SearchView.SearchAutoComplete();
        //searchView.setSuggestionsAdapter(cursorAdapter);
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchQuery = query;
                searchView.clearFocus();
                searchView.setQuery("", false);
                search(query);

                //items.add(query);
                //stringSuggestionAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void search(String query) {
        Intent intent = new Intent(TimelineActivity.this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }

    private void getCurrentUser() {
        client.getAccountInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                user = User.fromJson(responseBody);

                Log.d("DEBUG", "current user "+ responseBody);
                final ImageView headerImage = (ImageView) findViewById(R.id.ivHeader);
                RelativeLayout headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
                TextView name = (TextView) findViewById(R.id.headerUserName);
                TextView screenName = (TextView) findViewById(R.id.headerScreenName);
                try {
                    //headerImage.setBackgroundColor(responseBody.getInt("profile_background_color"));
                    //name.setText(responseBody.getString("name"));
                    headerLayout.setBackgroundColor(Color.parseColor("#"+responseBody.getString("profile_background_color")));
                    name.setText(user.getName());

                    //screenName.setText(responseBody.getString("screen_name"));
                    screenName.setText(responseBody.getString("screen_name"));
                    //Glide.with(TimelineActivity.this).load(user.getProfileImageUrl()).into(headerImage);
                    Glide.with(TimelineActivity.this).load(user.getProfileImageUrl()).asBitmap().into(new BitmapImageViewTarget(headerImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(TimelineActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            headerImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setUpDrawerContent(NavigationView nvDrawer) {
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        drawer.closeDrawers();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private void selectDrawerItem(MenuItem item) {
        Class fragmentClass;
        switch (item.getItemId()){
            case R.id.profile:
                Intent i = new Intent(TimelineActivity.this, UserProfileActivity.class);
                i.putExtra("screenName", user.getScreenName());
                startActivity(i);
                drawer.closeDrawers();
                break;

        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.viewpager, fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawers();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
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
