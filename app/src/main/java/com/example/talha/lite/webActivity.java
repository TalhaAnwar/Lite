package com.example.talha.lite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

public class webActivity extends AppCompatActivity {
    CheckBox cb;
    Button go,back,forward,refresh;
    EditText et;
    WebView wb;
    SeekBar sk;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        String url = null;
        if(bundle!=null){
            url=bundle.getString("load");
        }
        sk= (SeekBar) findViewById(R.id.seekBar);
        go= (Button) findViewById(R.id.go);
        back= (Button) findViewById(R.id.back);
        forward= (Button) findViewById(R.id.forward);
        refresh= (Button) findViewById(R.id.refresh);
        wb= (WebView) findViewById(R.id.webview);
        wb.setWebViewClient(new WebviewClient(preferences));
        wb.getSettings().setJavaScriptEnabled(true);
        et= (EditText) findViewById(R.id.editText);
        cb= (CheckBox) findViewById(R.id.checkBox);
        wb.setWebChromeClient(new WebchromeClient(sk,false,preferences));
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        wb.loadUrl("http://www.google.com.pk");
        if(url!=null){
            wb.loadUrl(url);
        }
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wb.loadUrl(et.getText().toString());
                InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                key.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wb.canGoBack())
                wb.goBack();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wb.canGoForward())
                    wb.goForward();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wb.reload();
            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    new WebchromeClient(sk,true,preferences);
                }else{

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.history){
            startActivity(new Intent(this,HistoryActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
