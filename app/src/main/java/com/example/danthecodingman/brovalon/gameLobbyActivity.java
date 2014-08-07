package com.example.danthecodingman.brovalon;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danthecodingman.brovalon.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class gameLobbyActivity extends Activity {

    final int SLEEP_TIME = 1000;
    TextView gameTitle;
    ListView playersList;

    String gameName;
    AsyncTask pollserv;
    boolean cancelThread = false;

    int maxPlayers = 5;
    int currentPlayers = 0;
    String hostId;

    userInfo user;
    gameInfo game;

    ArrayList<userInfo> usersInfo = new ArrayList<userInfo>();
    ArrayAdapter<userInfo> adapter;

    @Override
    public void onBackPressed() {
        //super.onStop();
        cancelThread = true;
        new userDisconnect().execute();
        Log.w("test", "Activity stopped!");
        this.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //super.onConfigurationChanged(newConfig);
        // This overrides default action
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        adapter=new ArrayAdapter<userInfo>(this,
                android.R.layout.simple_list_item_1,
                usersInfo);

        playersList = (ListView)findViewById(R.id.listView);
        playersList.setAdapter(adapter);

        user = new userInfo();
        Bundle extras = getIntent().getExtras();
        user.id = (String) extras.get("userId");
        user.gameId = (String) extras.get("gameId");
        user.name = (String) extras.get("name");
        gameTitle = (TextView)findViewById(R.id.gameTitle);

        pollserv = new pollServer();
        pollserv.execute();
    }

    private class pollServer extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            updateGameInfo();
            return null;
        }
    }

    private class userDisconnect extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            disconnectUser();
            return null;
        }
    }

    public void disconnectUser()
    {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("name", user.name));

        JSONObject response = brotilities.putWebRequest(brotilities.brovalonServer + "/users/" + user.id, nameValuePairs);
        if (response == null)
        {
            // error
            Log.w("test", "failed to put request");
        }

        nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("amount", "-1"));
        response = brotilities.postWebRequest(brotilities.brovalonServer + "/games/count/" + user.gameId, nameValuePairs);
        if (response == null)
        {
            Log.w("test", "failed to put request");
        }
    }

    public void updateGameInfo()
    {
        while (!cancelThread) {
            try {
                JSONObject gameInfoObj = brotilities.getWebRequestOneById(brotilities.brovalonServer + "/games/" + user.gameId);

                if (gameInfoObj != null) {
                    game.parseJSON(gameInfoObj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameTitle.setText(game.name);
                        }
                    });
                }
            }
            catch (Exception e)
            {

            }

            JSONArray userInfoObj = brotilities.getWebRequestArray(brotilities.brovalonServer + "/games/users/" + user.gameId);

            if (userInfoObj != null)
            {
                try {
                    usersInfo.clear();
                    for (int i = 0; i < userInfoObj.length(); i++) {
                        JSONObject obj = userInfoObj.getJSONObject(i);
                        userInfo tmpUser = new userInfo(obj);
                        usersInfo.add(tmpUser);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.w("test", e.getMessage());
                }
            }

            SystemClock.sleep(SLEEP_TIME);
        }
    }

    public void playGame(View view)
    {
        if (game.currentPlayers == game.maxPlayers && user.id.equals(game.hostId))
        {
            Intent myIntent = new Intent(gameLobbyActivity.this, gameActivity.class);
            myIntent.putExtra("userId", user.id);
            myIntent.putExtra("name", user.name);
            gameLobbyActivity.this.startActivity(myIntent);
            this.finish();
        }
    }

}
