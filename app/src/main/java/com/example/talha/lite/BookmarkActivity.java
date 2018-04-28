package com.example.talha.lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class BookmarkActivity extends AppCompatActivity {
    Button go1;
    EditText et1;
    GridView gv;
    String url[], s1[], title[], s, t, urls;
    Bitmap m[];

    SharedPreferences preferences;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        go1 = (Button) findViewById(R.id.go2);
        et1 = (EditText) findViewById(R.id.et1);
        gv = (GridView) findViewById(R.id.gv);
        if (!preferences.getBoolean("homekey", false)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("home", getString(R.string.google_search)).apply();
            edit.putBoolean("homekey", true).apply();
        }
        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et1.getText() != null) {
                    String url = et1.getText().toString();
                    url = url.replaceAll(" ", "+");
                    if ((url.contains("http://") || url.contains("https://"))) {
                        if ((url.contains("www."))) {
                            startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                        }
                    } else {
                        if (url.contains("www.")) {
                            startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                        } else {
                            url = getString(R.string.google_search) + url;
                            startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                        }
                    }

                }

            }
        });

        s = preferences.getString("bookmarkimage", "");
        t = preferences.getString("title", "");
        urls = preferences.getString("imageurl", "");
        if (s != "" && t != "" && urls != "") {
            s1 = s.split(",");
            title = t.split(",");
            url = urls.split(",");
            m = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m, title));

        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (url[position] != "")
                    startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url[position]));
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setTitle(R.string.delete_bookmark);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletebookmark(p);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                return false;

            }
        });
    }

    public void deletebookmark(int position) {
        s = preferences.getString("bookmarkimage", "");
        urls = preferences.getString("imageurl", "");
        t = preferences.getString("title", "");
        s1 = new String[0];
        url = new String[0];
        title = new String[0];
        if (s != "" && urls != "" && t != "") {
            s1 = s.split(",");
            url = urls.split(",");
            title = t.split(",");
        }
        s1[position] = "";
        url[position] = "";
        title[position] = "";
        s = "";
        urls = "";
        t = "";
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] != "" && url[i] != "" && title[i] != "") {
                s += s1[i] + ",";
                urls += url[i] + ",";
                t += title[i] + ",";
            }
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("bookmarkimage", s).apply();
        edit.putString("imageurl", urls).apply();
        edit.putString("title", t).apply();
        s = preferences.getString("bookmarkimage", "");
        t = preferences.getString("title", "");
        urls = preferences.getString("imageurl", "");
        if (s != "" && t != "" && urls != "") {
            s1 = s.split(",");
            title = t.split(",");
            url = urls.split(",");
            m = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m, title));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }
        if (item.getItemId() == R.id.home) {
            if (preferences.getString("home", "") != "")
                startActivity(new Intent(this, webActivity.class));
        }
        if (item.getItemId() == R.id.sethome) {
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
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        s = preferences.getString("bookmarkimage", "");
        t = preferences.getString("title", "");
        urls = preferences.getString("imageurl", "");
        if (s != "" && t != "" && urls != "") {
            s1 = s.split(",");
            title = t.split(",");
            url = urls.split(",");
            m = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m, title));
        }
    }
}
