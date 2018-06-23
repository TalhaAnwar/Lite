package com.example.talha.lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.talha.lite.Homescreen.validatehome;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    TextView noimg, pri, pass, chpass, aboutus, sethome;
    android.support.v7.widget.AppCompatImageButton img;
    SwitchCompat noimgs, pris, passs;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        getSupportActionBar().setTitle(R.string.settings);
        sethome = (TextView) findViewById(R.id.sethome);
        sethome.setOnClickListener(this);
        noimg = (TextView) findViewById(R.id.no_images_label);
        noimg.setOnClickListener(this);
        pri = (TextView) findViewById(R.id.incognito_label);
        pri.setOnClickListener(this);
        aboutus = (TextView) findViewById(R.id.aboutus);
        aboutus.setOnClickListener(this);
        pass = (TextView) findViewById(R.id.security);
        pass.setOnClickListener(this);
        chpass = (TextView) findViewById(R.id.changepass);
        chpass.setOnClickListener(this);
        img = (AppCompatImageButton) findViewById(R.id.imageButton);
        img.setOnClickListener(this);
        noimgs = (SwitchCompat) findViewById(R.id.no_images);
        noimgs.setOnClickListener(this);
        pris = (SwitchCompat) findViewById(R.id.privatebrowsing);
        pris.setOnClickListener(this);
        passs = (SwitchCompat) findViewById(R.id.secure);
        passs.setOnClickListener(this);
        b = preferences.getBoolean("noimages_status", false);
        if (b) {
            noimgs.setChecked(true);
        } else {
            noimgs.setChecked(false);
        }
        b = preferences.getBoolean("incognito_status", false);
        if (b) {
            pris.setChecked(true);
        } else {
            pris.setChecked(false);
        }
        b = preferences.getBoolean("passwordenabled", false);
        if (b) {
            passs.setChecked(true);
            chpass.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
        } else {
            chpass.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            passs.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.no_images_label:
                if (noimgs.isChecked()) {
                    noimgs.setChecked(false);
                    editor.putBoolean("noimages_status", false).apply();
                } else {
                    noimgs.setChecked(true);
                    editor.putBoolean("noimages_status", true).apply();
                }
                break;
            case R.id.incognito_label:
                if (pris.isChecked()) {
                    pris.setChecked(false);
                    editor.putBoolean("incognito_status", false).apply();
                } else {
                    pris.setChecked(true);
                    editor.putBoolean("incognito_status", true).apply();
                }
                break;
            case R.id.security:
                if (passs.isChecked()) {
                    passs.setChecked(false);
                    chpass.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                    editor.putBoolean("passwordenabled", false).apply();
                } else {
                    passs.setChecked(true);
                    chpass.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                    editor.putBoolean("passwordenabled", true).apply();
                }

                break;
            case R.id.changepass:
                final String chk = preferences.getString("password", null);
                if (chk == null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(Settings.this);
                    builder2.setTitle(R.string.enter_password);
                    builder2.setCancelable(false);
                    final EditText input = new EditText(getBaseContext());
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder2.setView(input);
                    builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String password = input.getText().toString();
                            editor.putString("password", password).apply();
                        }
                    });
                    builder2.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.enter_current_pass);
                    builder.setCancelable(false);
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String password = input.getText().toString();
                            if (chk.equals(password)) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(Settings.this);
                                builder2.setTitle(R.string.enter_password);
                                builder2.setCancelable(false);
                                final EditText input = new EditText(getBaseContext());
                                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                builder2.setView(input);
                                builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String password = input.getText().toString();
                                        editor.putString("password", password).apply();
                                    }
                                });
                                builder2.show();
                            } else {
                                Toast.makeText(getBaseContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                                dialog.cancel();
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
                break;
            case R.id.aboutus:
                startActivity(new Intent(this, AboutUs.class));
                break;
            case R.id.no_images:
                if (noimgs.isChecked()) {
                    noimgs.setChecked(false);
                    editor.putBoolean("noimages_status", false).apply();
                } else {
                    noimgs.setChecked(true);
                    editor.putBoolean("noimages_status", true).apply();
                }
                break;
            case R.id.privatebrowsing:
                if (pris.isChecked()) {
                    pris.setChecked(false);
                    editor.putBoolean("incognito_status", false).apply();
                } else {
                    pris.setChecked(true);
                    editor.putBoolean("incognito_status", true).apply();
                }
                break;
            case R.id.secure:
                if (passs.isChecked()) {
                    passs.setChecked(false);
                    chpass.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                    editor.putBoolean("passwordenabled", false).apply();
                } else {
                    passs.setChecked(true);
                    chpass.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                    editor.putBoolean("passwordenabled", true).apply();
                }
                break;
            case R.id.sethome:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.enter_home_url);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString();
                        editor.putString("home", validatehome(title)).apply();

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                break;
        }
    }
}
