package com.example.headfirstlabs6timetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stephen on 13-6-30.
 */
public class TimeTrackerAdapter extends BaseAdapter {
    private ArrayList<TimeRecord> dataList = new ArrayList<TimeRecord>();

    public TimeTrackerAdapter(){
        dataList.add(new TimeRecord("38:23", "Feeling good!"));
        dataList.add(new TimeRecord("49:01", "Tired. Need more caffeine."));
        dataList.add(new TimeRecord("26:21", "Rocking!"));
        dataList.add(new TimeRecord("29:42", "Lost some time on hills. But pretty good!"));
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.time_record, viewGroup, false);
        }
        TimeRecord dataItem = dataList.get(i);
        TextView timeView = (TextView)view.findViewById(R.id.time_textview);
        TextView notesView = (TextView)view.findViewById(R.id.notes_textview);
        timeView.setText(dataItem.getTime());
        notesView.setText(dataItem.getNotes());

        return view;
    }
}
