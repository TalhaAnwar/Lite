package com.example.talha.lite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Talha on 27/06/2018.
 */

public class RssFeedListAdapter extends BaseAdapter {
    private List<Homescreen.RssFeedModel> mRssFeedModels;

    public RssFeedListAdapter(List<Homescreen.RssFeedModel> list) {
        mRssFeedModels = list;
    }

    @Override
    public int getCount() {
        return mRssFeedModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position % 2 == 0) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_adapter, null, false);
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_adapter2, null, false);
        }
        TextView t1 = (TextView) convertView.findViewById(R.id.titleText);
        t1.setText(mRssFeedModels.get(position).title);
        return convertView;
    }
}
