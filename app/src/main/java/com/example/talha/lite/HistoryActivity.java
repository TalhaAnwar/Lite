package com.example.talha.lite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {
    ListView lv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lv = (ListView) findViewById(R.id.historylist);
        String s = preferences.getString("history", null);
        String[] s1 = new String[0];
        if (s != null) {
            s1 = s.split(",");
            lv.setAdapter(new ArrayAdapter<>(this, R.layout.listviewadapter, s1));
        }
        final String[] s2=s1;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("load",s2[position]));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            preferences.edit().putString("history", null).apply();
            String s = preferences.getString("history", null);
            String[] s1 = new String[0];
            if (s != null) {
                s1 = s.split(",");
                lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s1));
            } else {
                lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s1));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
