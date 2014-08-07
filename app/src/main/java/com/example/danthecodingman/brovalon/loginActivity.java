package com.example.danthecodingman.brovalon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

public class loginActivity extends ActionBarActivity {

    EditText mEdit;
    userInfo currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEdit = (EditText)findViewById(R.id.textUser);

        mEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    in.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    // Must return true here to consume event
                    return true;

                }
                return false;
            }
        });
    }

    private class handleUser extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            findOrCreateUser();
            return null;
        }
    }

    public void findOrCreateUser()
    {
        String username = mEdit.getText().toString();
        String userId = null;

        if (username == null || username.length() == 0)
        {
            return;
        }

        // look up user in database
        ArrayList<NameValuePair> searchTerms = new ArrayList<NameValuePair>(1);
        searchTerms.add(new BasicNameValuePair("name", username));

        try {
            JSONObject user = brotilities.getWebRequestOne(brotilities.brovalonServer + "/users/", searchTerms);
            if (user == null)
            {
                // no user found in database already, so we need to create one
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("name", username));

                JSONObject newUser = brotilities.postWebRequest(brotilities.brovalonServer + "/users/", nameValuePairs);
                if (newUser == null)
                {
                    // no server connection
                    return;
                }
                userId = newUser.getString("_id");
            }
            else
            {
                // user was found in database, so just load their info
                userId = user.getString("_id");
            }

            Intent myIntent = new Intent(loginActivity.this, mainActivity.class);
            
            myIntent.putExtra("userId", userId);
            myIntent.putExtra("name", username);
            loginActivity.this.startActivity(myIntent);
            this.finish();

        } catch (Exception e) {
            // handle exception
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showLoginAlert();
                }
            });
        }
    }

    public void showLoginAlert()
    {
        new AlertDialog.Builder(this)
                .setTitle("Login Error")
                .setMessage("The Brovalon server could not be contacted. Try again later.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // things
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void loginUser(View view)
    {
        new handleUser().execute();
    }

}
