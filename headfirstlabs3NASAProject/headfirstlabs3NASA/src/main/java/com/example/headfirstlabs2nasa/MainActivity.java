package com.example.headfirstlabs2nasa;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity {

    private Handler activityHandler = new Handler(); // AsyncTask is better than Handler
    private Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshFromFeed();
    }

    public void refreshFromFeed(){
        final IotdHandler handler = new IotdHandler();

        //need to run the network in a background task
        // otherwise NetworkonMainThreadException will be thown
        new AsyncTask<Void, Void, Void>(){
            private ProgressDialog dialog = null;

            @Override
            protected void onPreExecute() {
                //in UI Thread
                super.onPreExecute();
                dialog = ProgressDialog.show(MainActivity.this, "Loading...", "Loading. Please wait for a moment.");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //in UI Thread
                super.onPostExecute(aVoid);
                TextView imageTitle = (TextView)findViewById(R.id.imageTitle);
                imageTitle.setText(handler.getTitle());

                TextView date = (TextView)findViewById(R.id.imageDate);
                date.setText(handler.getDate());

                TextView desc = (TextView)findViewById(R.id.imageDescription);
                desc.setText(handler.getDesc());

                ImageView imageView = (ImageView)findViewById(R.id.imageDisplay);
                image = handler.getImage();
                imageView.setImageBitmap(image);
                dialog.dismiss();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                //in background thread
                handler.proceedFeed();
                return null;
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onRefresh(View view){
        refreshFromFeed();
    }

    public void onSetWallpaper(View view){
        new Thread(){
            @Override
            public void run() {
                try{
                    WallpaperManager manager = WallpaperManager.getInstance(MainActivity.this);
                    manager.setBitmap(image);
                    activityHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Wallpaper set.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    activityHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Wall paper failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }.start();


    }

}
