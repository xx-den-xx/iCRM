package ru.bda.icrm.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.model.Contragent;

/**
 * Created by User on 28.06.2016.
 */
public class ResponseParser {
    private static volatile ResponseParser instance;

    private ResponseParser(){}
    public static List<Contragent> sContragentList;

    public static ResponseParser getInstance(){
        if(instance == null){
            synchronized (ApiController.class) {
                if(instance == null){
                    instance = new ResponseParser();
                }
            }
        }
        return instance;
    }


    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String parseContragent(InputStream stream){
        String response = convertStreamToString(stream);
        JSONObject json;
        Log.d("myLog", response);
        String request = "";

        try {
            json = new JSONObject(response);
            JSONArray dataJSONArray = json.getJSONArray("data");
            sContragentList = new ArrayList<>();
            for(int i = 0; i < dataJSONArray.length(); i++) {
                Contragent contragent = new Contragent();
                JSONObject obj = dataJSONArray.getJSONObject(i);
                contragent.setUid(obj.getString("uid"));
                contragent.setNameContragent("title");
                sContragentList.add(contragent);
            }
            request = json.toString();
            Log.d("myLog", request);
            return request;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return request;

    }
}
