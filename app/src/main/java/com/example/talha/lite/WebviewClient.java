package com.example.talha.lite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Talha on 17/04/2018.
 */

public class WebviewClient extends WebViewClient {
    SharedPreferences preferences;

    public WebviewClient(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @SuppressWarnings("deprication")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        SharedPreferences.Editor editor = preferences.edit();
        String add = preferences.getString("history", null);
        if (add != null) {
            editor.putString("history", url + "," + add);
            editor.apply();
        } else {
            editor.putString("history", url);
            editor.apply();
        }
    }
}
