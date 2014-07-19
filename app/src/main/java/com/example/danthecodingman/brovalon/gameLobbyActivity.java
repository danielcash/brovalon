package com.example.danthecodingman.brovalon;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.danthecodingman.brovalon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class gameLobbyActivity extends ActionBarActivity {

    TextView gameTitle;
    userInfo currentUser;

    String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        currentUser = new userInfo();
        Bundle extras = getIntent().getExtras();
        currentUser.id = (String) extras.get("userId");
        currentUser.gameId = (String) extras.get("gameId");
        gameTitle = (TextView)findViewById(R.id.gameTitle);

        new pollServer().execute();
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
        while (true) {
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

            SystemClock.sleep(5000);
        }
    }

}
