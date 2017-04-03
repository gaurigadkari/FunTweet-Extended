package com.codepath.apps.simpletweet.DialogFragments;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;


import com.codepath.apps.simpletweet.Activities.TimelineActivity;
import com.codepath.apps.simpletweet.Adapters.TweetAdapter;
import com.codepath.apps.simpletweet.R;
import com.codepath.apps.simpletweet.TwitterClient;
import com.codepath.apps.simpletweet.databinding.TweetComposeBinding;
import com.codepath.apps.simpletweet.models.Tweet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


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
    boolean media = false;
    Long mediaId = Long.valueOf(1);
    static EditText composeTweet;
    Context context;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final Uri mLocationForPhotos = null;
    String mCurrentPhotoPath;
    String date;
    private ImageView cameraBttn;
    public interface ComposeTweetListener {
        public void tweetClickHandler(String tweetBody, String path, boolean  hasMedia);
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
                listener.tweetClickHandler(tweetBody, mCurrentPhotoPath, true);

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


    public void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // getPackageManager() needs context hence calling getActivity()
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bitmap thumbnail = data.("data");
            // Do other work with full size photo saved in mLocationForPhotos
            setPic();
        }
    }


    private void setPic() {
        // Get the dimensions of the View
        int targetW = cameraBttn.getWidth();
        int targetH = cameraBttn.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        cameraBttn.setImageBitmap(bitmap);
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
