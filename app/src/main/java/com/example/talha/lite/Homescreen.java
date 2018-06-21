package com.example.talha.lite;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;


public class Homescreen extends AppCompatActivity {
    Toolbar toolbar;
    EditText et1;
    GridView gv;
    int[] simgs = {R.mipmap.google, R.mipmap.youtube, R.mipmap.facebook,
            R.mipmap.wikipediia, R.mipmap.amazon, R.mipmap.olx,
            R.mipmap.yahoo, R.mipmap.instagram, R.mipmap.dailymotion,
            R.mipmap.pinterest, R.mipmap.twitter, R.mipmap.linkedin, R.mipmap.urdupoint};
    int[] urls = {R.string.google, R.string.youtube, R.string.facebook, R.string.wikipedia,
            R.string.amazon, R.string.olx, R.string.yahoo, R.string.instragram, R.string.dailymotion,
            R.string.pinterest, R.string.twitter, R.string.linkedin, R.string.urdupoint};
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String validatehome(String url) {
        if ((url.contains("http://") || url.contains("https://"))) {
            if ((url.contains("www."))) {
                return url;
            }
        } else {
            if (url.contains("www.")) {
                url = "https://" + url;
                return url;
            } else {
                url = "https://www." + url;
                return url;
            }
        }
        return url;
    }

    public static String validateurl(String url) {
        url = url.replaceAll(" ", "+");
        if ((url.contains("http://") || url.contains("https://"))) {
            if ((url.contains("www."))) {
                return url;
            }
        } else {
            if (url.contains("www.")) {
                url = "https://" + url;
                return url;
            } else {
                url = "https://www.google.com.pk/search?q=" + url;
                return url;
            }
        }
        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = preferences.edit();
        gv = (GridView) findViewById(R.id.gv);
        et1 = (EditText) findViewById(R.id.et1);
        et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String url = et1.getText().toString();
                    if (!et1.getText().toString().isEmpty()) {
                        url = validateurl(url);
                        startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                    }
                }
                return false;
            }
        });

        if (!preferences.getBoolean("homekey", false)) {

            edit.putString("home", getString(R.string.google_search)).apply();
            edit.putBoolean("incognito_status", false).apply();
            edit.putBoolean("noimages_status", false).apply();
            edit.putBoolean("homekey", true).apply();
            getstoragepermission();

        }
        gv.setAdapter(new Myadapter(this, simgs));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", getString(urls[position])));
            }
        });
        et1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et1.setSelection(0, et1.length());
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        Boolean b = preferences.getBoolean("incognito_status", false);
        if (b) {
            menu.findItem(R.id.privatebrowsing).setChecked(true);
        }
        b = preferences.getBoolean("noimages_status", false);
        if (b) {
            menu.findItem(R.id.no_images).setChecked(true);
        }
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.privatebrowsing) {
            if (!item.isChecked()) {
                item.setChecked(true);
                edit.putBoolean("incognito_status", true).apply();
            } else {
                item.setChecked(false);
                edit.putBoolean("incognito_status", false).apply();
            }
        }
        if (item.getItemId() == R.id.no_images) {
            if (!item.isChecked()) {
                item.setChecked(true);
                edit.putBoolean("noimages_status", true).apply();
            } else {
                item.setChecked(false);
                edit.putBoolean("noimages_status", false).apply();
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
        if (item.getItemId() == R.id.go2) {
            String url = et1.getText().toString();
            if (!et1.getText().toString().isEmpty()) {
                url = validateurl(url);
                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
            }
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
                    edit.putString("home", validatehome(title)).apply();

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
        if (item.getItemId() == R.id.aboutus) {
            startActivity(new Intent(this, AboutUs.class));
        }
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, Settings.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean getstoragepermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

}
