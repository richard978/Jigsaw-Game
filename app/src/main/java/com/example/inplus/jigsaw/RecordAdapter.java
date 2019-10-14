package com.example.inplus.jigsaw;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecordAdapter extends ArrayAdapter<Item> {
    private int layoutId;
    public RecordAdapter(Context context, int layoutId, List<Item> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        ((TextView)view.findViewById(R.id.rank)).setText("" + (position + 1));
        ((TextView)view.findViewById(R.id.user)).setText(item.user);
        ((TextView)view.findViewById(R.id.timeCost)).setText(item.timeCost);
        ((TextView)view.findViewById(R.id.date)).setText(item.date);
        return view;
    }
}