package com.example.jnewel200.resume01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by jnewel200 on 10/19/2015.
 */
public class FlickrRequestTask extends AsyncTask<Uri, Void, Integer> {
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    TextView mResultTextView = null;
    ImageView mResultImageView = null;
    Bitmap mImageBitmap = null;

    public FlickrRequestTask(TextView resultReference, ImageView imageViewRef){
        mResultTextView = resultReference;
        mResultImageView = imageViewRef;
    }
    @Override
    protected void onPostExecute(Integer integer) {
        if (mResultTextView != null) {
            mResultTextView.setText(integer.toString());
        }
        if(mResultImageView != null && mImageBitmap != null){
            mResultImageView.setImageBitmap(mImageBitmap);
        }
    }

    @Override
    protected Integer doInBackground(Uri... params) {
        Uri buildUri = params[0];
        String searchJsonStr = null;
        int totalPhotosFound = -1;
        try {
            URL url = new URL(buildUri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.v(LOG_TAG, "no input stream found for API Request");
                return  totalPhotosFound;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            while( (ln = reader.readLine()) != null){
                buffer.append(ln + "\n");
            }
            if(buffer.length() == 0) {
                Log.v(LOG_TAG,"empty buffer read from request stream");
                return totalPhotosFound;
            }
            searchJsonStr = buffer.toString();

        }catch(IOException e){
            Log.v("TAG_TODO", e.getLocalizedMessage());
        }
        try {
            JSONObject searchJson = new JSONObject(searchJsonStr);
            JSONObject photosDictionary = searchJson.getJSONObject("photos");
            totalPhotosFound = photosDictionary.getInt("total");
            JSONArray photoArray = photosDictionary.getJSONArray("photo");
            int idx = new Random().nextInt(photoArray.length());
            JSONObject chosenPhotoInfo = photoArray.getJSONObject(idx);
            String photoUrl = photoArray.getJSONObject(idx).getString("url_m");
            try {
                InputStream inputStream = new URL(photoUrl).openStream();
                mImageBitmap = BitmapFactory.decodeStream(inputStream);
            }catch(IOException e){
                Log.v(LOG_TAG, e.getLocalizedMessage());
            }


        }catch(JSONException e){
            Log.v(LOG_TAG, e.getLocalizedMessage());
            return totalPhotosFound;
        }
        Log.v(LOG_TAG, "Total photos: " + totalPhotosFound);
        return totalPhotosFound;
    }
}
