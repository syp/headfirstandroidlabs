package com.example.headfirstlabs7timetracker;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class AddTimeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_time);
    }

    public void onSave(View view){
        TextView timeView = (TextView) findViewById(R.id.time_editview);
        TextView notesView = (TextView) findViewById(R.id.notes_editview);
        Intent intent = getIntent();
        intent.putExtra("Time", timeView.getText().toString());
        intent.putExtra("Notes", notesView.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onCancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
    
}
