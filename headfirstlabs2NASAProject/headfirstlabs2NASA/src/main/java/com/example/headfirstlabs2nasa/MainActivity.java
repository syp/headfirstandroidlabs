package com.example.headfirstlabs2nasa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final IotdHandler handler = new IotdHandler();

        //need to run the network in a background task
        // otherwise NetworkonMainThreadException will be thown
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                TextView imageTitle = (TextView)findViewById(R.id.imageTitle);
                imageTitle.setText(handler.getTitle());

                TextView date = (TextView)findViewById(R.id.imageDate);
                date.setText(handler.getDate());

                TextView desc = (TextView)findViewById(R.id.imageDescription);
                desc.setText(handler.getDesc());

                ImageView image = (ImageView)findViewById(R.id.imageDisplay);
                image.setImageBitmap(handler.getImage());
            }

            @Override
            protected Void doInBackground(Void... voids) {
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

}
