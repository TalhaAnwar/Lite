package com.example.talha.lite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class webActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button back, forward, refresh, homebtn;
    EditText et;
    WebView wb;
    SeekBar sk;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    Boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = preferences.edit();
        Bundle bundle = getIntent().getExtras();
        String url = null;
        if (bundle != null) {
            url = bundle.getString("url");
        }
        sk = (SeekBar) findViewById(R.id.seekBar);
        homebtn = (Button) findViewById(R.id.home);
        back = (Button) findViewById(R.id.back);
        forward = (Button) findViewById(R.id.forward);
        refresh = (Button) findViewById(R.id.refresh);
        wb = (WebView) findViewById(R.id.webview);
        wb.setWebViewClient(new WebviewClient(preferences));
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setBuiltInZoomControls(true);
        et = (EditText) findViewById(R.id.editText);
        wb.setWebChromeClient(new WebchromeClient(sk, preferences, false, this));
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        String home = preferences.getString("home", null);
        b = preferences.getBoolean("noimages_status", false);
        if (b) {
            wb.getSettings().setLoadsImagesAutomatically(false);
        }
        if (home != null)
            wb.loadUrl(home);
        et.setText(wb.getUrl());
        if (url != null) {
            wb.loadUrl(url);
            et.setText(url);
        }
        et.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et.setSelection(0, et.length());
                return true;
            }
        });
        sk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = preferences.getString("home", null);
                if (url != null)
                    wb.loadUrl(url);
                et.setText(wb.getUrl());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wb.canGoBack())
                    wb.goBack();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wb.canGoForward())
                    wb.goForward();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wb.reload();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        b = preferences.getBoolean("incognito_status", false);
        if (b) {
            menu.findItem(R.id.privatebrowsing).setChecked(true);
        }
        b = preferences.getBoolean("noimages_status", false);
        if (b) {
            menu.findItem(R.id.no_images).setChecked(true);
        }
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.checkBox) {
            new WebchromeClient().savetitle();
        }
        if (item.getItemId() == R.id.privatebrowsing) {
            if (item.isChecked()) {
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
                wb.getSettings().setLoadsImagesAutomatically(false);
                edit.putBoolean("noimages_status", true).apply();
            } else {
                item.setChecked(false);
                wb.getSettings().setLoadsImagesAutomatically(true);
                edit.putBoolean("noimages_status", false).apply();
            }
        }
        if (item.getItemId() == R.id.go2) {
            if (!et.getText().toString().isEmpty()) {
                String url = et.getText().toString();
                if ((url.contains("http://") || url.contains("https://"))) {
                    if ((url.contains("www."))) {
                        wb.loadUrl(url);
                        et.setText(url);
                        InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        key.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }
                } else {
                    if (url.contains("www.")) {
                        url = "https://" + url;
                        wb.loadUrl(url);
                        et.setText(url);
                        InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        key.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    } else {
                        url = R.string.google_search + url;
                        wb.loadUrl(url);
                        et.setText(url);
                        InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        key.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }
                }


            }

        }
        if (item.getItemId() == R.id.bookmarks) {
            startActivity(new Intent(this, Bookmarks.class));
        }
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
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
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
