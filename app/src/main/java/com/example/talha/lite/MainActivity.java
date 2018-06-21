package com.example.talha.lite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    Boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        edit = preferences.edit();
        b = preferences.getBoolean("firsttime", true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (b) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.password_protect);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setTitle(R.string.enter_password);
                    builder2.setCancelable(false);
                    final EditText input = new EditText(getBaseContext());
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    builder2.setView(input);
                    builder2.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String password = input.getText().toString();
                            edit.putString("password", password).apply();
                            edit.putBoolean("firsttime", false).apply();
                            edit.putBoolean("passwordenabled", true).apply();
                            if (String.valueOf(getIntent().getData()) != "null") {
                                String url = String.valueOf(getIntent().getData());
                                startActivity(new Intent(MainActivity.this, webActivity.class).putExtra("url", url));
                                finish();
                            } else {
                                startActivity(new Intent(MainActivity.this, Homescreen.class));
                                finish();
                            }

                        }
                    });
                    builder2.show();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    edit.putBoolean("firsttime", false).apply();
                    edit.putBoolean("passwordenabled", false).apply();
                    dialog.cancel();
                    if (String.valueOf(getIntent().getData()) != "null") {
                        String url = String.valueOf(getIntent().getData());
                        startActivity(new Intent(MainActivity.this, webActivity.class).putExtra("url", url));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, Homescreen.class));
                        finish();
                    }

                }
            });
            builder.show();

        } else {
            b = preferences.getBoolean("passwordenabled", true);
            if (b) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.enter_password);
                builder.setCancelable(false);
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        String chk = preferences.getString("password", null);
                        if (chk.equals(password)) {
                            if (String.valueOf(getIntent().getData()) != "null") {
                                String url = String.valueOf(getIntent().getData());
                                startActivity(new Intent(MainActivity.this, webActivity.class).putExtra("url", url));
                                finish();
                            } else {
                                startActivity(new Intent(MainActivity.this, Homescreen.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                builder.show();
            } else {
                if (String.valueOf(getIntent().getData()) != "null") {
                    String url = String.valueOf(getIntent().getData());
                    startActivity(new Intent(MainActivity.this, webActivity.class).putExtra("url", url));
                    finish();
                } else {
                    startActivity(new Intent(MainActivity.this, Homescreen.class));
                    finish();
                }
            }
        }

    }
}