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

class WebchromeClient extends WebChromeClient {
    static private SeekBar sk;
    static private SharedPreferences preferences;
    static private Bitmap map;
    static private String url = null;
    String title=null;
    static private Context ctx;

     WebchromeClient(SeekBar sk, SharedPreferences preferences, Boolean flag,Context ctx) {
        WebchromeClient.ctx =ctx;
        WebchromeClient.sk = sk;
        WebchromeClient.preferences = preferences;

    }
    WebchromeClient(){

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
        }


    private void saveimage() {
        SharedPreferences.Editor edit = preferences.edit();
        String s = preferences.getString("bookmarkimage", null);
        if (s != null) {
            String toadd = s + ",";
            edit.putString("bookmarkimage", toadd + encodeTobase64(map)).apply();
        } else {
            edit.putString("bookmarkimage", encodeTobase64(map)).apply();
        }
    }

    private void saveurl() {
        SharedPreferences.Editor edit = preferences.edit();
        String s = preferences.getString("imageurl", null);
        if (s != null) {
            String toadd = s + ",";
            edit.putString("imageurl", toadd + url).apply();
        } else {
            edit.putString("imageurl", url).apply();
        }

    }

    private void savetitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.enter_title);
        final EditText input = new EditText(ctx);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    private static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(image !=null)
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

}
