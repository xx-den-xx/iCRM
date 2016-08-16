package ru.bda.icrm.auth;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.json.ResponseParser;

/**
 * Created by User on 28.06.2016.
 */
public class ApiController {

    private final String mBaseUrl = Constants.TEST_API_LINK;
    private static volatile ApiController instance;
    private Context context;

    private ApiController(){}

    public static ApiController getInstance() {
        if (instance == null) {
            synchronized (ApiController.class) {
                if (instance == null) {
                    instance = new ApiController();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public String addContragent(String token){
        String urlString = mBaseUrl + "?token=" + token + "&action=getContragentList";
        try{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("action", "getContragentList");
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
            Log.d("myLog", urlString);
            return ResponseParser.getInstance().parseContragent(connection.getInputStream());
        }catch(Exception e){
            return "error";
        }
    }

    public String auth(String hexLogin, String hexPassword) {
        String urlString = mBaseUrl + "?action=auth&login=" + hexLogin + "&pass=" + hexPassword;
        try{
            JSONObject jsonObj = new JSONObject();
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
            Log.d("myLog", urlString);
            return ResponseParser.getInstance().parseToken(connection.getInputStream());
        } catch (Exception e) {
            return "error";
        }
    }
}
