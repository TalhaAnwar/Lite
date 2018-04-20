package com.example.talha.lite;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;

/**
 * Created by Talha on 17/04/2018.
 */

public class WebchromeClient extends WebChromeClient {
    SeekBar sk;
    Boolean flag;
    SharedPreferences preferences;
    public WebchromeClient(SeekBar sk,Boolean flag,SharedPreferences preferences) {
        this.flag=flag;
        this.sk = sk;
        this.preferences=preferences;
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
        if(flag==true){
            savebookmark(preferences,icon);
            flag=false;
        }else{

        }
    }
    public void savebookmark(SharedPreferences preferences,Bitmap icon){
        SharedPreferences.Editor editor= preferences.edit();
//        String s=preferences.getString("bookmarkimage",null);
//        if(s!=null){
//            s+=","+encodeTobase64(icon);
//            editor.putString("bookmarkimage",s).apply();
//
//        }else{
            editor.putString("bookmarkimage",encodeTobase64(icon)).apply();
        Log.d("my","addid");
//        }
    }
    public void deletebookmark(){

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
