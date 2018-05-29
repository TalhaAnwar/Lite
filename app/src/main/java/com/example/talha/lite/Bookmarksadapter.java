package com.example.talha.lite;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Talha on 27/05/2018.
 */

public class Bookmarksadapter extends BaseAdapter {
    private LayoutInflater inflator;
    private Context ct;
    private ArrayList<adapt> list = new ArrayList<adapt>();

    public Bookmarksadapter(Context ctx, Bitmap[] map, String[] title) {
        ct = ctx;
        if (title != null)
            for (int i = 0; i < title.length; i++) {
                list.add(new adapt(map[i], title[i]));
            }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = inflator.inflate(R.layout.bookmarksadapter, parent, false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView2);
        TextView t = (TextView) convertView.findViewById(R.id.textView2);
        adapt row = list.get(position);
        if (row.maps != null)
            img.setImageBitmap(row.maps);
        if (row.title != "")
            t.setText(row.title);
        return convertView;
    }

}

class adapt {
    Bitmap maps;
    String title;

    public adapt(Bitmap map, String title) {
        maps = map;
        this.title = title;
    }
}

