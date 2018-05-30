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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

public class webActivity extends AppCompatActivity {
    ImageView cb;
    Toolbar toolbar;
    Button go, back, forward, refresh;
    EditText et;
    WebView wb;
    SeekBar sk;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        String url = null;
        if (bundle != null) {
            url = bundle.getString("url");
        }
        sk = (SeekBar) findViewById(R.id.seekBar);
        go = (Button) findViewById(R.id.go);
        back = (Button) findViewById(R.id.back);
        forward = (Button) findViewById(R.id.forward);
        refresh = (Button) findViewById(R.id.refresh);
        wb = (WebView) findViewById(R.id.webview);
        wb.setWebViewClient(new WebviewClient(preferences));
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setBuiltInZoomControls(true);
        et = (EditText) findViewById(R.id.editText);
        cb = (ImageView) findViewById(R.id.checkBox);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebchromeClient().savetitle();
            }
        });
        wb.setWebChromeClient(new WebchromeClient(sk, preferences, false,this));
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        String home=preferences.getString("home",null);
        if (home != null)
        wb.loadUrl(home);
        et.setText(wb.getUrl());
        if (url != null) {
            wb.loadUrl(url);
            et.setText(url);
        }
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = et.getText().toString();
                url=url.replaceAll(" ", "+");
                if ((url.contains("http://") || url.contains("https://"))) {
                    if ((url.contains("www."))) {
                        wb.loadUrl(url);
                        et.setText(url);
                        InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        key.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }
                } else {
                    if (url.contains("www.")) {
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
        getMenuInflater().inflate(R.menu.menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.no_images) {
            if (!item.isChecked()) {
                item.setChecked(true);
                wb.getSettings().setLoadsImagesAutomatically(false);
            } else {
                item.setChecked(false);
                wb.getSettings().setLoadsImagesAutomatically(true);
            }
        }
        if (item.getItemId() == R.id.bookmarks) {
            startActivity(new Intent(this, Bookmarks.class));
        }
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }
        if (item.getItemId() == R.id.home) {
            String url = preferences.getString("home", null);
            if (url != null)
                wb.loadUrl(url);
        }
        if(item.getItemId()==R.id.sethome){
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
