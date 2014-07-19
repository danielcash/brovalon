package com.example.danthecodingman.brovalon;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.app.ListActivity;
import android.util.Log;

import java.net.*;
import java.io.*;
import java.util.*;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.json.*;
import android.widget.TextView;

public class mainActivity extends ListActivity {

    ArrayList<String> gameStrs = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    TextView mText;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                gameStrs);
        setListAdapter(adapter);
        new pollServer().execute();
        mText = (TextView)findViewById(R.id.textView);
        extras = getIntent().getExtras();
        mText.setText("User: " + (String)extras.get("name"));
    }

    private class pollServer extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            updateGameList();
            return null;
        }
    }

    public void updateGameList()
    {
        JSONArray games = brotilities.getWebRequestArray("http://danthecodingman.com:3000/games");

        if (games != null) {
            try {
                gameStrs.clear();
                for (int i = 0; i < games.length(); i++) {
                    JSONObject obj = games.getJSONObject(i);
                    gameStrs.add(obj.getString("name"));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (JSONException e) {
                // handle exception
            }
        }
    }

    public void refreshGames(View view)
    {
        new pollServer().execute();
    }

    public void createGame(View view)
    {
        Intent myIntent = new Intent(mainActivity.this, createGameActivity.class);
        //myIntent.putExtra();
        mainActivity.this.startActivity(myIntent);
    }
}
