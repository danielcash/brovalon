package com.example.danthecodingman.brovalon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.app.ListActivity;
import android.util.Log;

import java.net.*;
import java.io.*;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import android.widget.ListView;
import android.widget.TextView;

public class mainActivity extends Activity {

    ArrayList<gameListItem> gameStrs = new ArrayList<gameListItem>();
    ArrayAdapter<gameListItem> adapter;
    ListView gameList;
    TextView mText;
    Bundle extras;
    String userId;
    userInfo currentUser;

    private class gameListItem
    {
        String id;
        String name;

        @Override
        public String toString(){
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new ArrayAdapter<gameListItem>(this,
                android.R.layout.simple_list_item_1,
                gameStrs);

        new pollServer().execute();

        mText = (TextView)findViewById(R.id.textView);
        gameList = (ListView)findViewById(R.id.listView);
        gameList.setAdapter(adapter);

        gameList.setClickable(true);
        gameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                gameListItem o = (gameListItem)gameList.getItemAtPosition(position);
                currentUser.gameId = o.id;
                new setUserInfo().execute();

                Intent myIntent = new Intent(mainActivity.this, gameLobbyActivity.class);
                myIntent.putExtra("userId", currentUser.id);
                myIntent.putExtra("gameId", currentUser.gameId);
                myIntent.putExtra("name", currentUser.name);
                mainActivity.this.startActivity(myIntent);
            }
        });

        extras = getIntent().getExtras();
        if (extras != null) {
            userId = (String) extras.get("userId");
        }

        currentUser = new userInfo();
        currentUser.id = userId;
        currentUser.name = (String) extras.get("name");
        new getUserInfo().execute();
    }

    private class pollServer extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            updateGameList();
            return null;
        }
    }

    private class getUserInfo extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            grabUserInfo();
            return null;
        }
    }

    private class setUserInfo extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            updateUserInfo();
            return null;
        }
    }

    public void grabUserInfo()
    {
        try {
            JSONObject user = brotilities.getWebRequestOneById(brotilities.brovalonServer + "/users/" + userId);

            if (user != null)
            {
                try {
                    currentUser.name = user.getString("name");
                    currentUser.gameId = user.getString("gameId");
                }
                catch (Exception e)
                {
                    Log.w("test", e.getMessage());
                }
            }
        }
        catch (Exception e)
        {

        }

    }

    public void updateUserInfo()
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("name", currentUser.name));
        nameValuePairs.add(new BasicNameValuePair("gameId", currentUser.gameId));

        JSONObject response = brotilities.putWebRequest(brotilities.brovalonServer + "/users/" + userId, nameValuePairs);
        if (response == null)
        {
            // error
            Log.w("test", "failed to put request");
        }
    }

    public void updateGameList()
    {
        JSONArray games = brotilities.getWebRequestArray(brotilities.brovalonServer + "/games");

        if (games != null) {
            try {
                gameStrs.clear();
                for (int i = 0; i < games.length(); i++) {
                    JSONObject obj = games.getJSONObject(i);
                    gameListItem gli = new gameListItem();
                    gli.id = obj.getString("_id");
                    gli.name = obj.getString("name");
                    gameStrs.add(gli);
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
