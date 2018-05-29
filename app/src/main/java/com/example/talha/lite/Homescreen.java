package com.example.talha.lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;


public class Homescreen extends AppCompatActivity {
    Button go1;
    EditText et1;
    GridView gv;
    int[] simgs = {R.mipmap.google, R.mipmap.youtube, R.mipmap.facebook,
            R.mipmap.wikipediia, R.mipmap.amazon, R.mipmap.olx,
            R.mipmap.yahoo, R.mipmap.instagram, R.mipmap.dailymotion,
            R.mipmap.pinterest, R.mipmap.twitter};
    int[] urls = {R.string.google, R.string.youtube, R.string.facebook, R.string.wikipedia,
            R.string.amazon, R.string.olx, R.string.yahoo, R.string.instragram, R.string.dailymotion,
            R.string.pinterest, R.string.twitter};
    SharedPreferences preferences;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        go1 = (Button) findViewById(R.id.go2);
        et1 = (EditText) findViewById(R.id.et1);
        gv = (GridView) findViewById(R.id.gv);
        if (!preferences.getBoolean("homekey", false)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("home", getString(R.string.google_search)).apply();
            edit.putBoolean("homekey", true).apply();
        }

        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et1.getText().toString() != "") {
                    String url = et1.getText().toString();
                    url = url.replaceAll(" ", "+");
                    if ((url.contains("http://") || url.contains("https://"))) {
                        if ((url.contains("www."))) {
                            startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                        }
                    } else {
                        if (url.contains("www.")) {
                            url = "https://" + url;
                            startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                        } else {
                            url = getString(R.string.google_search) + url;
                            startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                        }
                    }

                }

            }
        });
        gv.setAdapter(new Myadapter(this, simgs));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", getString(urls[position])));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.no_images) {
            if (!item.isChecked()) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
        }
        if (item.getItemId() == R.id.bookmarks) {
            startActivity(new Intent(this, Bookmarks.class));
        }
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }
        if (item.getItemId() == R.id.home) {
            if (preferences.getString("home", null) != null)
                startActivity(new Intent(this, webActivity.class));
        }
        if (item.getItemId() == R.id.sethome) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.enter_home_url);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = input.getText().toString();
                    if ((title.contains("http://") || title.contains("https://"))) {
                        if ((title.contains("www."))) {
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("home", title).apply();
                        }
                    } else {
                        if (title.contains("www.")) {
                            title = "https://" + title;
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("home", title).apply();
                        } else {
                            title = "https://www." + title;
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("home", title).apply();
                        }
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
