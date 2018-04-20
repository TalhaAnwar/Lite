package com.example.talha.lite;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by Talha on 17/04/2018.
 */

public class Myadapter extends ArrayAdapter {
    Context ct;
    Bitmap[] maps;
    LayoutInflater inflator;

    public Myadapter(Context ctx, Bitmap[] map) {
        super(ctx, R.layout.myadapter, R.id.imageView, map);
        ct = ctx;
        maps = map;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflator = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.myadapter, parent, false);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageBitmap(maps[position]);
        return view;
    }

}
