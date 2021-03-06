package com.ternakkita;

import android.util.Log;
import android.util.Pair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class JSONParser {
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    int response = -1;

    public JSONParser(){

    }

    public JSONObject makeHttpRequest(String url, String method, List<Pair<String, String>> params)
            throws IOException{ // throws start
        URL urls = new URL(url);
        URLConnection connection = urls.openConnection();
        if(!(connection instanceof HttpURLConnection))
            throw new IOException("Not An Http Connection");
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            outputStream.close();
            httpURLConnection.connect();
            response = httpURLConnection.getResponseCode();
            if(response == httpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
            }
        }catch(Exception e){
            Log.d("Networking",e.getLocalizedMessage());
            throw new IOException("Error Connecting");
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();
        }catch(Exception e){

        }

        try {
            Log.e("Buffer Error",json);
            jsonObject = new JSONObject(json);
        }catch (Exception e){

        }
        return jsonObject;
    } // throws end

    private String getQuery(List<Pair<String,String>> params)
            throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair<String, String> pair : params){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(pair.first,"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second,"UTF-8"));
        }
        return result.toString();
    }
}
