package com.example.talha.lite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Talha on 17/04/2018.
 */

public class Myadapter extends BaseAdapter {
    int[] pics;
    private LayoutInflater inflator;
    private Context ct;

    public Myadapter(Context ctx, int[] pics) {
        ct = ctx;
        this.pics = pics;
    }

    @Override
    public int getCount() {
        return pics.length;
    }

    @Override
    public Object getItem(int position) {
        return pics[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            inflator = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.myadapter, parent, false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
        img.setImageResource(pics[position]);
        return convertView;
    }

}

