package com.example.talha.lite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {
    Button clear;
    ListView lv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        clear = (Button) findViewById(R.id.clear);
        lv = (ListView) findViewById(R.id.historylist);
        String s = preferences.getString("history", null);
        String[] s1 = new String[0];
        ;
        if (s != null) {
            s1 = s.split(",");
            lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s1));
        }
        final String[] s2=s1;
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences.edit().putString("history", null).apply();
                finish();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("load",s2[position]));
            }
        });
    }
}
