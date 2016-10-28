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

import ru.bda.icrm.auth.AnswerServer;
import ru.bda.icrm.auth.ApiController;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;

/**
 * Created by User on 28.06.2016.
 */
public class ResponseParser {
    private static volatile ResponseParser instance;

    private ResponseParser(){}

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

    public List<Contragent> parseContragentList(InputStream stream){
        String response = convertStreamToString(stream);
        JSONObject json;

        try {
            json = new JSONObject(response);
            JSONArray dataJSONArray = json.getJSONArray("data");
            List<Contragent> contragentList = new ArrayList<>();
            for(int i = 0; i < dataJSONArray.length(); i++) {
                Contragent contragent = new Contragent();
                JSONObject obj = dataJSONArray.getJSONObject(i);
                contragent.setUid(obj.getString("uid"));
                contragent.setNameContragent(obj.getString("title"));
                contragentList.add(contragent);
            }
            return contragentList;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String parseToken(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        String request = "";

        try {
            json = new JSONObject(response);
            String state = json.get("state").toString();
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                String token = json.get("token").toString();
                request = token;
            } else {
                request = "error";
            }
            Log.d("myLog", state);

        } catch(JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    public Contragent parseContragent(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        Contragent contragent = new Contragent();
        try {
            json = new JSONObject(response);
            JSONObject jsonData = json.getJSONObject("data");
            String state = json.get("state").toString();
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                contragent.setId(jsonData.getString("id"));
                contragent.setUid(jsonData.getString("uid"));
                contragent.setInn(jsonData.getString("inn"));
                contragent.setNameContragent(jsonData.getString("title"));
                contragent.setCodePoOkpo(jsonData.getString("PoOKPO"));
                contragent.setUrFace(jsonData.getString("jur"));
                contragent.setRelations(jsonData.getString("relations"));
                contragent.setContactInfo(jsonData.getString("contact_info"));
                contragent.setEmail(jsonData.getString("email"));
                contragent.setJurAddress(jsonData.getString("jur_address"));
                contragent.setSite(jsonData.getString("site"));

                List<String> phones = new ArrayList<String>();
                JSONArray jPhones = jsonData.getJSONArray("phones");
                if (jPhones != null) {
                    for (int i = 0; i < jPhones.length(); i++) {
                        phones.add(jPhones.get(i).toString());
                    }
                } else {
                    phones = null;
                }
                contragent.setPhones(phones);

                List<String> contacts = new ArrayList<String>();
                JSONArray jContacts = jsonData.getJSONArray("phones");
                if (jContacts != null) {
                    for (int i = 0; i < jContacts.length(); i++) {
                        contacts.add(jPhones.get(i).toString());
                    }
                } else {
                    contacts = null;
                }
                contragent.setContacts(contacts);

            } else {
                contragent = null;
            }
            Log.d("myLog", state);

        } catch(JSONException e) {
            e.printStackTrace();
            contragent = null;
        }
        return contragent;
    }

    public Contragent parseSaveContragent(InputStream stream, Contragent contragent) {
        String response = convertStreamToString(stream);
        JSONObject json;
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                contragent.setId(json.getString("contragent"));
            } else {
                contragent = null;
            }
            Log.d("myLog", state);
        } catch(JSONException e) {
            e.printStackTrace();
            contragent = null;
        }
        return contragent;
    }

    public List<Price> parsePrice(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        List<Price> list = new ArrayList<>();
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                JSONArray nomenclature = json.getJSONArray("nomenclature");
                for (int i = 0; i < nomenclature.length(); i++) {
                    JSONObject item = nomenclature.getJSONObject(i);
                    Price price = new Price();
                    price.setId(item.getInt("id"));
                    price.setCode(item.getString("code"));
                    price.setParent(item.getString("parent"));
                    price.setTitle(item.getString("title").replace("&quot;", "\""));
                    String priceString = item.getString("price").replace(",", ".");
                    String convertPrice = priceString.replace(" ", "");
                    price.setPrice(Double.parseDouble(convertPrice));
                    list.add(price);
                }
                return list;
            } else {
                Log.d("myLog", state);
                return null;
            }


        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PriceSum> parsePriceSum(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        List<PriceSum> list = new ArrayList<>();
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                JSONArray nomenclature = json.getJSONArray("nomenclature");
                for (int i = 0; i < nomenclature.length(); i++) {
                    JSONObject item = nomenclature.getJSONObject(i);
                    PriceSum price = new PriceSum();
                    price.setId(item.getInt("id"));
                    price.setCode(item.getString("code"));
                    price.setParent(item.getString("parent"));
                    price.setTitle(item.getString("title").replace("&quot;", "\""));
                    String priceString = item.getString("price").replace(",", ".");
                    String convertPrice = priceString.replace(" ", "");
                    price.setPrice(Double.parseDouble(convertPrice));
                    list.add(price);
                }
                return list;
            } else {
                Log.d("myLog", state);
                return null;
            }


        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
