package com.example.danthecodingman.brovalon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Daniel on 7/19/2014.
 */
public class userInfo {
    public String id;
    public String name;

    public String gameId;
    public boolean ready;

    public userInfo() {}

    public userInfo(JSONObject obj)
    {
        parseJSON(obj);
    }

    public void parseJSON(JSONObject obj)
    {
        try {
            id = new String(obj.getString("_id"));
            name = new String(obj.getString("name"));
            gameId = new String(obj.getString("gameId"));
            ready = new Boolean(obj.getBoolean("ready"));
        } catch (JSONException e){
            // handle exception
        }
    }

    public static String serializeObject(userInfo ui)
    {
        String serializedObj = null;

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(ui);
            so.flush();
            serializedObj = bo.toString();
        } catch (Exception e) {
            // handle exception
        }

        return serializedObj;
    }

    public static userInfo deserializeObject(String ui)
    {
        userInfo tmp = null;

        try {
            byte b[] = ui.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            tmp = (userInfo) si.readObject();
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
