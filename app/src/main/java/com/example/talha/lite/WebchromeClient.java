package com.example.talha.lite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;

public class WebchromeClient extends WebChromeClient {
    SeekBar sk;
    Boolean flag = false;
    SharedPreferences preferences;
    Bitmap map;
    String url = null;
    String title=null;
    Context ctx;

    public WebchromeClient(SeekBar sk, SharedPreferences preferences, Boolean flag,Context ctx) {
        this.ctx=ctx;
        this.sk = sk;
        this.preferences = preferences;
        this.flag = flag;

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        super.onProgressChanged(view, newProgress);
        sk.setProgress(newProgress);
        if (sk.getProgress() == 100) {
            sk.setVisibility(View.GONE);
        } else {
            sk.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
        map = icon;
        url = view.getUrl();
        savetitle();

    }

    public void saveimage() {
        SharedPreferences.Editor edit = preferences.edit();
        String s = preferences.getString("bookmarkimage", null);
        if (s != null) {
            String toadd = s + ",";
            edit.putString("bookmarkimage", toadd + encodeTobase64(map)).apply();
        } else {
            edit.putString("bookmarkimage", encodeTobase64(map)).apply();
        }
    }

    public void saveurl() {
        SharedPreferences.Editor edit = preferences.edit();
        String s = preferences.getString("imageurl", null);
        if (s != null) {
            String toadd = s + ",";
            edit.putString("imageurl", toadd + url).apply();
        } else {
            edit.putString("imageurl", url).apply();
        }

    }

    public void savetitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Enter Title");
        final EditText input = new EditText(ctx);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = input.getText().toString();
                SharedPreferences.Editor edit = preferences.edit();
                String s = preferences.getString("title", null);
                if (s != null) {
                    String toadd = s + ",";
                    edit.putString("title", toadd + title).apply();
                } else {
                    edit.putString("title", title).apply();
                }
                saveimage();
                saveurl();
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

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

}
