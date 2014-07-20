package com.example.danthecodingman.brovalon;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.*;

/**
 * Created by Daniel on 7/19/2014.
 */
public class brotilities {
    public static String brovalonServer = "http://192.168.0.101:3000";

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            //Log.w("test", writer.toString());
            return writer.toString();
        } else {
            return "";
        }
    }

    public static JSONArray getWebRequestArray(String requestURL)
    {
        HttpResponse response = null;
        try {
            // create connection
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(requestURL));
            response = client.execute(request);

            // create JSON object from content
            JSONArray jsonArray = new JSONArray(brotilities.convertStreamToString(response.getEntity().getContent()));
            return jsonArray;

        } catch (MalformedURLException e) {
            // URL is invalid
            Log.w("test", e.getMessage());
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            Log.w("test", e.getMessage());
        } catch (java.io.IOException e) {
            // could not read response body
            // (could not create input stream)
            Log.w("test", e.getMessage());
        } catch (JSONException e) {
            // response body is no valid JSON string
            Log.w("test", e.getMessage());
        } catch (Exception e) {
            // other
            Log.w("test", e.getMessage());
        }

        return null;
    }

    public static JSONObject getWebRequestOne(String requestURL, List<NameValuePair> searchTerms) throws Exception
    {
        HttpResponse response = null;
        // create connection
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost();
        request.setURI(new URI(requestURL));
        if (searchTerms != null) {
            request.setEntity(new UrlEncodedFormEntity(searchTerms));
        }
        response = client.execute(request);

        // create JSON object from content
        JSONObject jsonObj = new JSONObject(brotilities.convertStreamToString(response.getEntity().getContent()));
        return jsonObj;
    }

    public static JSONObject getWebRequestOneById(String requestURL) throws Exception
    {
        HttpResponse response = null;

        // create connection
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        request.setURI(new URI(requestURL));
        response = client.execute(request);

        // create JSON object from content
        JSONObject jsonObj = new JSONObject(brotilities.convertStreamToString(response.getEntity().getContent()));
        return jsonObj;
    }

    public static JSONObject postWebRequest(String requestURL, List<NameValuePair> data)
    {
        HttpResponse response = null;
        try {
            // create connection
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(requestURL));
            request.setEntity(new UrlEncodedFormEntity(data));
            response = client.execute(request);

            // create JSON object from content
            JSONObject jsonObj = new JSONObject(brotilities.convertStreamToString(response.getEntity().getContent()));
            return jsonObj;

        } catch (MalformedURLException e) {
            // URL is invalid
            Log.w("test", e.getMessage());
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            Log.w("test", e.getMessage());
        } catch (java.io.IOException e) {
            // could not read response body
            // (could not create input stream)
            Log.w("test", e.getMessage());
        } catch (JSONException e) {
            // response body is no valid JSON string
            Log.w("test", e.getMessage());
        } catch (Exception e) {
            // other
            Log.w("test", e.getMessage());
        }

        return null;
    }

    public static JSONObject putWebRequest(String requestURL, List<NameValuePair> data)
    {
        HttpResponse response = null;
        try {
            // create connection
            HttpClient client = new DefaultHttpClient();
            HttpPut request = new HttpPut();
            request.setURI(new URI(requestURL));
            request.setEntity(new UrlEncodedFormEntity(data));
            response = client.execute(request);

            // create JSON object from content
            JSONObject jsonObj = new JSONObject(brotilities.convertStreamToString(response.getEntity().getContent()));
            return jsonObj;

        } catch (MalformedURLException e) {
            // URL is invalid
            Log.w("test", e.getMessage());
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            Log.w("test", e.getMessage());
        } catch (java.io.IOException e) {
            // could not read response body
            // (could not create input stream)
            Log.w("test", e.getMessage());
        } catch (JSONException e) {
            // response body is no valid JSON string
            Log.w("test", e.getMessage());
        } catch (Exception e) {
            // other
            Log.w("test", e.getMessage());
        }

        return null;
    }
}
