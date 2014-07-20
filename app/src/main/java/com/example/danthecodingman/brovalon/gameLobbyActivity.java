package com.example.danthecodingman.brovalon;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danthecodingman.brovalon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class gameLobbyActivity extends Activity {

    TextView gameTitle;
    ListView playersList;
    userInfo currentUser;
    String gameName;
    AsyncTask pollserv;
    boolean cancelThread = false;

    ArrayList<userInfo> usersInfo = new ArrayList<userInfo>();
    ArrayAdapter<userInfo> adapter;

    @Override
    public void onStop() {
        super.onStop();
        cancelThread = true;
        Log.w("test", "Activity stopped!");
        this.finish();
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

        currentUser = new userInfo();
        Bundle extras = getIntent().getExtras();
        currentUser.id = (String) extras.get("userId");
        currentUser.gameId = (String) extras.get("gameId");
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

    public void updateGameInfo()
    {
        while (!cancelThread) {
            JSONObject gameInfoObj = brotilities.getWebRequestOneById("http://danthecodingman.com:3000/games/" + currentUser.gameId);

            if (gameInfoObj != null) {
                try {
                    gameName = new String(gameInfoObj.getString("name"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameTitle.setText(gameName);
                        }
                    });

                } catch (JSONException e) {
                    // handle exception
                }
            }

            JSONArray userInfoObj = brotilities.getWebRequestArray("http://danthecodingman.com:3000/games/users/" + currentUser.gameId);

            if (userInfoObj != null)
            {
                try {
                    usersInfo.clear();
                    for (int i = 0; i < userInfoObj.length(); i++) {
                        JSONObject obj = userInfoObj.getJSONObject(i);
                        userInfo tmpUser = new userInfo();
                        tmpUser.id = obj.getString("_id");
                        tmpUser.name = obj.getString("name");
                        //tmpUser.ready = obj.getBoolean("ready");
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

            SystemClock.sleep(5000);
        }
    }

}
