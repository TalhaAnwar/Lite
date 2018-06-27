package com.example.talha.lite;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class Homescreen extends AppCompatActivity {
    Toolbar toolbar;
    ListViewCompat listView;
    EditText et1;
    GridView gv;
    int[] simgs = {R.mipmap.google, R.mipmap.youtube, R.mipmap.facebook,
            R.mipmap.amazon, R.mipmap.instagram, R.mipmap.dailymotion,
            R.mipmap.twitter, R.mipmap.urdupoint};
    int[] urls = {R.string.google, R.string.youtube, R.string.facebook,
            R.string.amazon, R.string.instragram, R.string.dailymotion,
            R.string.twitter, R.string.urdupoint};
    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    private List<RssFeedModel> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String validatehome(String url) {
        if ((url.contains("http://") || url.contains("https://"))) {
            if ((url.contains("www."))) {
                return url;
            }
        } else {
            if (url.contains("www.")) {
                url = "https://" + url;
                return url;
            } else {
                url = "https://www." + url;
                return url;
            }
        }
        return url;
    }

    public static String validateurl(String url) {
        url = url.replaceAll(" ", "+");
        if ((url.contains("http://") || url.contains("https://"))) {
            if ((url.contains("www."))) {
                return url;
            }
        } else {
            if (url.contains("www.")) {
                url = "https://" + url;
                return url;
            } else {
                url = "https://www.google.com.pk/search?q=" + url;
                return url;
            }
        }
        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = preferences.edit();
        gv = (GridView) findViewById(R.id.gv);
        listView = (ListViewCompat) findViewById(R.id.newslistview);
        new FetchFeedTask().execute((Void) null);
        et1 = (EditText) findViewById(R.id.et1);
        et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String url = et1.getText().toString();
                    if (!et1.getText().toString().isEmpty()) {
                        url = validateurl(url);
                        startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
                    }
                }
                return false;
            }
        });

        if (!preferences.getBoolean("homekey", false)) {

            edit.putString("home", getString(R.string.google_search)).apply();
            edit.putBoolean("incognito_status", false).apply();
            edit.putBoolean("noimages_status", false).apply();
            edit.putBoolean("homekey", true).apply();
            getstoragepermission();

        }
        gv.setAdapter(new Myadapter(this, simgs));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", getString(urls[position])));
            }
        });
        et1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et1.setSelection(0, et1.length());
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", mFeedModelList.get(position).link));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bookmarks) {
            startActivity(new Intent(this, Bookmarks.class));
        }
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        }
        if (item.getItemId() == R.id.home) {
            if (preferences.getString("home", null) != null)
                startActivity(new Intent(this, webActivity.class));
        }
        if (item.getItemId() == R.id.go2) {
            String url = et1.getText().toString();
            if (!et1.getText().toString().isEmpty()) {
                url = validateurl(url);
                startActivity(new Intent(getBaseContext(), webActivity.class).putExtra("url", url));
            }
        }
        if (item.getItemId() == R.id.aboutus) {
            startActivity(new Intent(this, AboutUs.class));
        }
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, Settings.class));
        }
        if (item.getItemId() == R.id.downloadslocation) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri mydir = Uri.parse("Root/Phone Storage/Download");
            intent.setDataAndType(mydir, "*/*");    // or use */*
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchFeedTask().execute((Void) null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(data.getData(), data.getType()));
        }
    }

    public boolean getstoragepermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;

        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                }

                if (title != null && link != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link);
                        items.add(item);
                    } else {
                        mFeedTitle = title;
                        mFeedLink = link;

                    }

                    title = null;
                    link = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            urlLink = "https://www.urdupoint.com/rss/urdupoint-imp.rss";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                // Fill RecyclerView
                listView.setAdapter(new RssFeedListAdapter(mFeedModelList));
            } else {
                Toast.makeText(Homescreen.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class RssFeedModel {

        public String title;
        public String link;

        public RssFeedModel(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }

}


