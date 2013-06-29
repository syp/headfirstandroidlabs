package com.example.headfirstlabs5nasa;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class NasaApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onRefresh(View view){
        NasaIotd iotdFragment = (NasaIotd)getFragmentManager().findFragmentById(R.id.fragment_iotd);
        iotdFragment.onRefresh(view);
    }

    public void onSetWallpaper(View view){
        NasaIotd iotdFragment = (NasaIotd)getFragmentManager().findFragmentById(R.id.fragment_iotd);
        iotdFragment.onSetWallpaper(view);
    }
    
}
