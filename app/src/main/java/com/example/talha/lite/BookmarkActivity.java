package com.example.talha.lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class BookmarkActivity extends AppCompatActivity {
    Button go1;
    EditText et1;
    GridView gv;
    String url[], s1[], title[];
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        go1 = (Button) findViewById(R.id.go2);
        et1 = (EditText) findViewById(R.id.et1);
        gv = (GridView) findViewById(R.id.gv);
        go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et1.getText() != null) {
                    String url = et1.getText().toString();
                    url=url.replaceAll(" ", "+");
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
        String s = preferences.getString("bookmarkimage", null);
        String t = preferences.getString("title", null);
        if (s != null && t != null) {
            s1 = s.split(",");
            title = t.split(",");
            Bitmap m[] = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m, title));

        }
        String urls = preferences.getString("imageurl", null);
        if (urls != null)
            url = urls.split(",");
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (url != null)
                    startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url[position]));
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletebookmark(position);
                return false;

            }
        });
    }

    public void deletebookmark(int position) {
        String imgs = preferences.getString("bookmarkimage", null);
        String urls = preferences.getString("imageurl", null);
        String titles = preferences.getString("title", null);
        s1 = new String[0];
        url = new String[0];
        title = new String[0];
        if (imgs != null) {
            s1 = imgs.split(",");
        }
        if (urls != null) {
            url = urls.split(",");
        }
        if (titles != null) {
            title = titles.split(",");
        }
        s1[position] = null;
        url[position] = null;
        title[position] = null;
        imgs = "";
        urls = "";
        titles = "";
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] != null && url[i] != null && title[i] != null) {
                imgs += s1[i] + ",";
                urls += url[i] + ",";
                titles += title[i] + ",";
            }
        }
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("bookmarkimage", imgs).apply();
        edit.putString("imageurl", urls).apply();
        edit.putString("title", titles).apply();
        String s = preferences.getString("bookmarkimage", null);
        String t = preferences.getString("title", null);
        if (s != null && s != "" && t != null && t != "") {
            s1 = s.split(",");
            title = t.split(",");
            Bitmap m[] = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m, title));
        }
        urls = preferences.getString("imageurl", null);
        if (urls != null)
            url = urls.split(",");

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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
        if(item.getItemId()==R.id.home){
            startActivity(new Intent(this,webActivity.class));
        }
        if(item.getItemId()==R.id.sethome){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.enter_home_url);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = input.getText().toString();
                    SharedPreferences.Editor edit=preferences.edit();
                    edit.putString("home",title).apply();
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
        String s = preferences.getString("bookmarkimage", null);
        String t = preferences.getString("title", null);
        if (s != null && s != "" && t != null && t != "") {
            s1 = s.split(",");
            title = t.split(",");
            Bitmap m[] = new Bitmap[s1.length];
            for (int i = 0; i < s1.length; i++) {
                m[i] = decodeBase64(s1[i]);
            }
            gv.setAdapter(new Myadapter(this, m, title));
        }
        String urls = preferences.getString("imageurl", null);
        if (urls != null && urls != "")
            url = urls.split(",");
    }
}
