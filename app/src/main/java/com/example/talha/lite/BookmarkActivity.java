package com.example.talha.lite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

public class BookmarkActivity extends AppCompatActivity {
    Button go1;
    EditText et1;
    GridView gv;
    ImageView imig;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        go1= (Button) findViewById(R.id.go2);
        et1= (EditText) findViewById(R.id.et1);
        gv= (GridView) findViewById(R.id.gv);
        imig= (ImageView) findViewById(R.id.imig);
        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),webActivity.class).putExtra("load",et1.getText().toString()));
            }
        });
        String s=preferences.getString("bookmarkimage",null);
        if(s!=null){
            imig.setImageBitmap(decodeBase64(s));
        }

//        if(s!=null){
//            String s1[]=s.split(",");
//            Bitmap m[]=new Bitmap[s1.length];
//            for(int i=0;i<s1.length;i++){
//                m[i]=decodeBase64(s1[i]);
//            }
//            gv.setAdapter(new Myadapter(this,m));

//        }else{
//            Log.d("my","bookmarknull");
//        }

    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
