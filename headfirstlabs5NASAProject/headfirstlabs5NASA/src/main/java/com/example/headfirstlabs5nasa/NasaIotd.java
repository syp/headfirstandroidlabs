package com.example.headfirstlabs5nasa;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

public class NasaIotd extends Fragment {

    private Handler activityHandler = new Handler(); // AsyncTask is better than Handler
    private Bitmap image = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
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
                dialog = ProgressDialog.show(NasaIotd.this.getActivity(), "Loading...", "Loading. Please wait for a moment.");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //in UI Thread
                super.onPostExecute(aVoid);
                TextView imageTitle = (TextView)getActivity().findViewById(R.id.imageTitle);
                imageTitle.setText(handler.getTitle());

                TextView date = (TextView)getActivity().findViewById(R.id.imageDate);
                date.setText(handler.getDate());

                TextView desc = (TextView)getActivity().findViewById(R.id.imageDescription);
                desc.setText(handler.getDesc());

                ImageView imageView = (ImageView)getActivity().findViewById(R.id.imageDisplay);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
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
                    WallpaperManager manager = WallpaperManager.getInstance(NasaIotd.this.getActivity());
                    manager.setBitmap(image);
                    activityHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NasaIotd.this.getActivity(), "Wallpaper set.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (IOException ioe){
                    ioe.printStackTrace();
                    activityHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NasaIotd.this.getActivity(), "Wall paper failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }.start();


    }

}
