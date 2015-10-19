package com.example.jnewel200.resume01;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                EditText tv = (EditText)findViewById(R.id.main_activity_search_text);
                String searchText = tv.getText().toString();
                //build the API call...
                final String BASE_URL = "https://api.flickr.com/services/rest?";
                final String METHOD_PARAM = "method";
                final String API_KEY_PARAM = "api_key";
                final String FORMAT_PARAM = "format";
                final String NOJSON_CALLBACK_PARAM = "nojsoncallback";
                final String SEARCH_TEXT_PARAM = "text";
                String restCallString = "https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=1c4a20853c41e65187fe1ac23eb85538&format=json&nojsoncallback=1";
                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(METHOD_PARAM, "flickr.photos.search")
                        .appendQueryParameter(API_KEY_PARAM,"1c4a20853c41e65187fe1ac23eb85538")
                        .appendQueryParameter(FORMAT_PARAM,"json")
                        .appendQueryParameter(NOJSON_CALLBACK_PARAM,"1")
                        .appendQueryParameter(SEARCH_TEXT_PARAM,searchText)
                        .build();
                //String searchJsonStr = null;
                TextView numPhotos = (TextView)findViewById(R.id.main_activity_num_photos);
                FlickrRequestTask apiRequestTask = new FlickrRequestTask(numPhotos);
                apiRequestTask.execute(new Uri[]{buildUri});
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}