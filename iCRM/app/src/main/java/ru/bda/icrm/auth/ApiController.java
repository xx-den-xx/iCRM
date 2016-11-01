package ru.bda.icrm.auth;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.json.ResponseParser;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.Score;

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

    public String auth(String hexLogin, String hexPassword) {
        String urlString = mBaseUrl + "?action=auth&login=" + hexLogin + "&pass=" + hexPassword;
        //Log.d("myLog", urlString);
        try{
            JSONObject jsonObj = new JSONObject();
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
            return ResponseParser.getInstance().parseToken(connection.getInputStream());
        } catch (Exception e) {
            return "error";
        }
    }

    public List<Contragent> getContragentList (String token, int start, int count){
        String urlString = mBaseUrl + "?action=getContragentList";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("start", start);
            root.put("count", count);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parseContragentList(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public List<Contragent> getContragentList (String token){
        String urlString = mBaseUrl + "?action=getContragentList";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            //root.put("start", start);
            //root.put("count", count);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parseContragentList(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public List<Contragent> searchContragent (String token, String string){
        String urlString = mBaseUrl + "?action=getContragentList";
        Log.d("myLog", urlString);
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("string", string);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parseContragentList(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public Contragent getContragent (String token, String uid) {
        String urlString = mBaseUrl + "?action=getContragent&token=" + token + "&contragent=" + uid;
        Log.d("myLog", mBaseUrl);
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
            Log.d("myLog", urlString);
            return ResponseParser.getInstance().parseContragent(connection.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }

    public Contragent addContragent(String token, String id, Contragent contragent) {
        String urlString = mBaseUrl + "?action=updateContragent";
        try{
            JSONObject root = new JSONObject();
            JSONObject jsonObj = new JSONObject();
            JSONArray phonesArray = new JSONArray(contragent.getPhones());
            JSONArray contactsArray = new JSONArray(contragent.getContacts());

            root.put("token", token);
            root.put("contragent", id);
            jsonObj.put("code", contragent.getCode());
            jsonObj.put("inn", contragent.getInn());
            jsonObj.put("title", contragent.getNameContragent());
            jsonObj.put("PoOKPO", contragent.getCodePoOkpo());
            jsonObj.put("jur", contragent.getUrFace());
            jsonObj.put("relations", contragent.getRelations());
            jsonObj.put("contact_info", contragent.getContactInfo());
            jsonObj.put("email", contragent.getEmail());
            jsonObj.put("jur_address", contragent.getJurAddress());
            jsonObj.put("site", contragent.getSite());
            jsonObj.put("phones", phonesArray);
            root.put("data", jsonObj);
            root.put("contacts", contactsArray);
            if (contragent.getLat() != 0 && contragent.getLon() != 0) {
                root.put("lng", contragent.getLon());
                root.put("lat", contragent.getLat());
            }

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parseSaveContragent(connection.getInputStream(), contragent);
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return null;
        }
    }

    public List<Price> getNomenclature(String token, int start, int count) {
        String urlString = mBaseUrl + "?action=getNomenclature";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("start", start);
            root.put("count", count);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parsePrice(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return null;
        }
    }

    public List<PriceSum> getNomenclatureSum(String token, int start, int count) {
        String urlString = mBaseUrl + "?action=getNomenclature";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("start", start);
            root.put("count", count);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parsePriceSum(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return null;
        }
    }

    public List<Price> searchNomenclature(String token, String search) {
        String urlString = mBaseUrl + "?action=searchNomenclature";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("string", search);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parsePrice(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return null;
        }
    }

    public List<PriceSum> searchNomenclatureSum(String token, String search) {
        String urlString = mBaseUrl + "?action=searchNomenclature";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("string", search);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parsePriceSum(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return null;
        }
    }

    public String saveScore (String token, String idContragent) {
        String urlString = mBaseUrl + "?action=makeInvoice";
        Log.d("myLog", "token = " + token + "; id contragent = " + idContragent);
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("contragent", idContragent);

            Log.d("myLog", root.toString());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            String s = root.toString();
            byte[] outputInBytes = s.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write( outputInBytes );
            os.close();
            connection.connect();
            connection.getInputStream();
            Log.d("myLog", "запрос выполнен успешно");
            return ResponseParser.getInstance().parseNumberScore(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return null;
        }
    }

}
