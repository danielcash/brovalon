package com.example.danthecodingman.brovalon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Daniel on 8/2/2014.
 */
public class gameInfo {
    public String id;
    public String name;

    public String hostId;
    public int maxPlayers;
    public int currentPlayers;

    public gameInfo(JSONObject obj)
    {
        parseJSON(obj);
    }

    public void parseJSON(JSONObject obj)
    {
        try {
            id = new String(obj.getString("_id"));
            name = new String(obj.getString("name"));
            maxPlayers = new Integer(obj.getInt("maxplayers"));
            currentPlayers = new Integer(obj.getInt("currentplayers"));
            hostId = new String(obj.getString("hostId"));
        } catch (JSONException e){
            // handle exception
        }
    }

    public static String serializeObject(gameInfo gi)
    {
        String serializedObj = null;

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(gi);
            so.flush();
            serializedObj = bo.toString();
        } catch (Exception e) {
            // handle exception
        }

        return serializedObj;
    }

    public static gameInfo deserializeObject(String gi)
    {
        gameInfo tmp = null;

        try {
            byte b[] = gi.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            tmp = (gameInfo) si.readObject();
        } catch (Exception e) {
            // handle exception
        }

        return tmp;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
