package com.example.talha.lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class BookmarkActivity extends AppCompatActivity {
    Button go1;
    EditText et1;
    GridView gv;
    String url[];
    String s1[];
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        go1 = (Button) findViewById(R.id.go2);
        et1 = (EditText) findViewById(R.id.et1);
        gv = (GridView) findViewById(R.id.gv);
        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", et1.getText().toString()));
            }
        });
        String s = preferences.getString("bookmarkimage", null);
        if (s != null) {
            s1= s.split(",");
            Bitmap m[] = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m));

        }
        String urls = preferences.getString("imageurl", null);
        if (urls != null)
            url = urls.split(",");
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url[position]));
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletebookmark(position);
                return false;
            }
        });
    }

    public void deletebookmark(int position) {
        String imgs = preferences.getString("bookmarkimage", null);
        String urls = preferences.getString("imageurl", null);
        s1 = new String[0];
        url = new String[0];
        if (imgs != null) {
            s1 = imgs.split(",");
        }
        if (urls != null) {
            url = urls.split(",");
        }
        s1[position] = null;
        url[position] = null;
        imgs = null;
        urls = null;
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] != null) {
                imgs += s1[i]+"," ;
                urls += url[i]+"," ;
            }
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("bookmarkimage", imgs).apply();
        edit.putString("imageurl", urls).apply();
        String s = preferences.getString("bookmarkimage", null);
        if (s != null) {
            s1= s.split(",");
            Bitmap m[] = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m));

        }
        urls = preferences.getString("imageurl", null);
        if (urls != null)
            url = urls.split(",");

    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String s = preferences.getString("bookmarkimage", null);
        if (s != null) {
            s1 = s.split(",");
            Bitmap m[] = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m));
        }
        String urls = preferences.getString("imageurl", null);
        if (urls != null)
            url = urls.split(",");
    }
}
