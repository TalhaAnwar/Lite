package com.example.talha.lite;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


/**
 * Created by Talha on 17/04/2018.
 */

public class WebviewClient extends WebViewClient {
    private SharedPreferences preferences;
    private EditText et;


    public WebviewClient(SharedPreferences preferences, EditText editText) {
        this.preferences = preferences;
        et = editText;

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        et.setText(url);
        Boolean b = preferences.getBoolean("incognito_status", false);
        if (!b) {
            SharedPreferences.Editor editor = preferences.edit();
            String add = preferences.getString("history", "");
            if (add != "") {
                editor.putString("history", url + "," + add).apply();
            } else {
                editor.putString("history", url).apply();
            }
        }

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);


    }
}
