package com.example.danthecodingman.brovalon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danthecodingman.brovalon.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class createGameActivity extends ActionBarActivity {

    TextView textMaxPlayers;
    SeekBar seekMaxPlayers;
    EditText textGameName;
    int maxPlayers;
    userInfo currentUser;

    private class spawnGame extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0)
        {
            sendGameCreateRequest();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        seekMaxPlayers = (SeekBar)findViewById(R.id.seekBar);
        textMaxPlayers = (TextView)findViewById(R.id.textMaxPlayers);

        textGameName = (EditText)findViewById(R.id.editGameName);

        currentUser = new userInfo();
        Bundle extras = getIntent().getExtras();
        currentUser.id = (String) extras.get("userId");
        currentUser.gameId = (String) extras.get("gameId");
        currentUser.name = (String) extras.get("name");

        textGameName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    in.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    // Must return true here to consume event
                    return true;

                }
                return false;
            }
        });

        seekMaxPlayers.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                maxPlayers = seekMaxPlayers.getProgress() + 5;
                textMaxPlayers.setText(new Integer(maxPlayers).toString());
            }
        });
    }

    public void createGame(View v)
    {
        new spawnGame().execute();
    }

    public void sendGameCreateRequest()
    {
        String gameName = textGameName.getText().toString();
        if (gameName.length() == 0)
            return;

        try
        {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", gameName));
            nameValuePairs.add(new BasicNameValuePair("maxplayers", (new Integer(maxPlayers).toString())));

            JSONObject response = brotilities.postWebRequest(brotilities.brovalonServer + "/games/", nameValuePairs);
            if (response == null)
            {
                // no server connection
                return;
            }

            currentUser.gameId = response.getString("_id");

            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", currentUser.name));
            nameValuePairs.add(new BasicNameValuePair("gameId", currentUser.gameId));

            response = brotilities.putWebRequest(brotilities.brovalonServer + "/users/" + currentUser.id, nameValuePairs);
            if (response == null)
            {
                // error
                Log.w("test", "failed to put request");
                return;
            }
        } catch(Exception e)
        {
            // probably couldn't connect to the server or something
        }

        Intent myIntent = new Intent(createGameActivity.this, gameLobbyActivity.class);
        myIntent.putExtra("userId", currentUser.id);
        myIntent.putExtra("gameId", currentUser.gameId);
        myIntent.putExtra("name", currentUser.name);
        createGameActivity.this.startActivity(myIntent);
        this.finish();
    }

}
