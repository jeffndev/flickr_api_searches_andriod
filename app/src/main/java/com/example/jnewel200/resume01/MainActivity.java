package com.example.jnewel200.resume01;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String IMAGE_BITMAP_SAVE_KEY = "MAIN_IMAGE_BITMAP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState != null){
            ImageView im = (ImageView)findViewById(R.id.main_activity_selected_image_view);
            Bitmap bmp = (Bitmap)savedInstanceState.getParcelable(IMAGE_BITMAP_SAVE_KEY);
            im.setImageBitmap(bmp);
        }
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
                final String EXTRAS_PARAM = "extras";
                final String EXTRAS_IMAGE_URL_SETTING = "url_m";
                //String restCallString = "https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=1c4a20853c41e65187fe1ac23eb85538&format=json&nojsoncallback=1";
                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(METHOD_PARAM, "flickr.photos.search")
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.flicker_api_key))
                        .appendQueryParameter(FORMAT_PARAM,"json")
                        .appendQueryParameter(NOJSON_CALLBACK_PARAM,"1")
                        .appendQueryParameter(SEARCH_TEXT_PARAM,searchText)
                        .appendQueryParameter(EXTRAS_PARAM,EXTRAS_IMAGE_URL_SETTING)
                        .build();
                //String searchJsonStr = null;
                TextView numPhotos = (TextView)findViewById(R.id.main_activity_num_photos);
                ImageView mainImage = (ImageView)findViewById(R.id.main_activity_selected_image_view);
                FlickrRequestTask apiRequestTask = new FlickrRequestTask(numPhotos, mainImage);
                apiRequestTask.execute(new Uri[]{buildUri});
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ImageView im = (ImageView)findViewById(R.id.main_activity_selected_image_view);
        Bitmap bmp = ((BitmapDrawable)im.getDrawable()).getBitmap();
        outState.putParcelable(IMAGE_BITMAP_SAVE_KEY, bmp);
        super.onSaveInstanceState(outState);
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
