package com.example.talha.lite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import static com.example.talha.lite.Homescreen.decodeBase64;

public class Bookmarks extends AppCompatActivity {
    ListView lv;
    SharedPreferences preferences;
    String url[], s1[], title[], s, t, urls;
    Bitmap m[];
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lv = (ListView) findViewById(R.id.lv);
        s = preferences.getString("bookmarkimage", null);
        t = preferences.getString("title", null);
        urls = preferences.getString("imageurl", null);
        if (s != null && t != null && urls != null) {
            s1 = s.split(",");
            title = t.split(",");
            url = urls.split(",");
            m = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            if (s1.length >= 1) {
                lv.setAdapter(new Bookmarksadapter(this, m, title));
            } else
                lv.setAdapter(new Bookmarksadapter(this, null, null));
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (url[position] != "")
                    startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url[position]));
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(R.string.delete_bookmark);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletebookmark(p);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    private void deletebookmark(int position) {
        s = preferences.getString("bookmarkimage", null);
        urls = preferences.getString("imageurl", null);
        t = preferences.getString("title", null);
        s1 = new String[0];
        url = new String[0];
        title = new String[0];
        if (s != null && urls != null && t != null) {
            s1 = s.split(",");
            url = urls.split(",");
            title = t.split(",");
        }
        s1[position] = null;
        url[position] = null;
        title[position] = null;
        s = "";
        urls = "";
        t = "";
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] != null && url[i] != null && title[i] != null) {
                s += s1[i] + ",";
                urls += url[i] + ",";
                t += title[i] + ",";
            }
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("bookmarkimage", s).apply();
        edit.putString("imageurl", urls).apply();
        edit.putString("title", t).apply();
        s = preferences.getString("bookmarkimage", null);
        t = preferences.getString("title", null);
        urls = preferences.getString("imageurl", null);
        if (s != null && t != null && urls != null) {
            s1 = s.split(",");
            title = t.split(",");
            url = urls.split(",");
            m = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            if (s1.length >= 1)
                lv.setAdapter(new Bookmarksadapter(this, m, title));
            else
                lv.setAdapter(new Bookmarksadapter(this, null, null));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        s = preferences.getString("bookmarkimage", null);
        t = preferences.getString("title", null);
        urls = preferences.getString("imageurl", null);
        if (s != null && t != null && urls != null) {
            s1 = s.split(",");
            title = t.split(",");
            url = urls.split(",");
            m = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            if (s1.length >= 1)
                lv.setAdapter(new Bookmarksadapter(this, m, title));
            else
                lv.setAdapter(new Bookmarksadapter(this, null, null));
        }
    }
}

