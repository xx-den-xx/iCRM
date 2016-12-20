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
import ru.bda.icrm.model.Contact;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Phone;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.Score;

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
                contragent.setId(obj.getString("id"));
                contragent.setNameContragent(obj.getString("title"));
                contragent.setLat(Double.parseDouble(obj.getString("lat")));
                contragent.setLon(Double.parseDouble(obj.getString("lng")));
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
        Log.d("myLog", response);
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
                contragent.setLon(Double.valueOf(jsonData.getString("lng")));
                contragent.setLat(Double.valueOf(jsonData.getString("lat")));

                JSONArray phones = jsonData.getJSONArray("phones");
                List<Phone> phoneList = new ArrayList<>();
                for (int i = 0; i < phones.length(); i++) {
                    Phone phone = new Phone();
                    phone.setContactsId(contragent.getId());
                    phone.setmNumber(phones.getJSONObject(i).getString("phone"));
                    phone.setType(Integer.parseInt(phones.getJSONObject(i).getString("type")));
                    phoneList.add(phone);
                }

                contragent.setPhones(phoneList);
                JSONArray jContacts = jsonData.getJSONArray("contacts");
                if (jContacts.length() > 0) {
                    contragent.setIdContact(jContacts.getJSONObject(0).getString("id"));
                    contragent.setContacts(jContacts.getJSONObject(0).getString("fio"));
                }

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
            Log.d("myLog", "state save = " + state);
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
        Log.d("Log-Price", response);
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            Log.d("Log-Price", response);
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                JSONArray nomenclature = json.getJSONArray("nomenclature");
                for (int i = 0; i < nomenclature.length(); i++) {
                    JSONObject item = nomenclature.getJSONObject(i);
                    PriceSum price = new PriceSum();
                    price.setId(item.getInt("id"));
                    price.setCode(item.getString("code"));
                    price.setParent(item.getString("parent"));
                    price.setTitle(item.getString("nomenclature").replace("&quot;", "\""));
                    price.setGroup(item.getInt("isgroup") == 1 ? true : false);
                    price.setUnit(item.getString("unit"));
                    String priceString = item.getString("price").replace(",", ".");
                    String convertPrice = priceString.replace(" ", "");
                    if (!convertPrice.equals("")) price.setPrice(Double.parseDouble(convertPrice));
                    Log.d("Log-Price", price.toString());
                    list.add(price);
                }
                return list;
            } else {
                Log.d("Log-Price", "ERROR ANSWER SERVER");
                return null;
            }
        } catch(JSONException e) {
            e.printStackTrace();
            Log.d("Log-Price", "ERROR PARSE");
            return null;
        }
    }

    public String parseNumberScore(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        String numberScore = "";
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            numberScore = json.getString("invoiceId");
            if (AnswerServer.getInstance().isAnswerServer(state)) {
                return numberScore;
            } else {
                return null;
            }


        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean parseUpdateScore(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            return AnswerServer.getInstance().isAnswerServer(state);

        } catch(JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Score> parseScoreList(InputStream stream){
        String response = convertStreamToString(stream);
        JSONObject json;
        List<Score> scoreList = new ArrayList<>();
        try {
            json = new JSONObject(response);
            JSONArray array = json.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Contragent client = new Contragent();
                client.setId(obj.getString("contragent"));
                client.setNameContragent(obj.getString("contragent_name"));
                Score score = new Score();
                score.setNumberAccount(obj.getString("id"));
                score.setClient(client);
                score.setDateAccount(obj.getLong("date"));
                score.setSumScore(obj.getDouble("sum"));
                score.setStatus(Integer.parseInt(obj.getString("state")));
                score.setPriority(Integer.parseInt(obj.getString("prio")));
                scoreList.add(score);
            }
            return scoreList;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Score parseScore(InputStream stream){
        String response = convertStreamToString(stream);
        JSONObject json;
        Score score = new Score();
        Contragent contragent = new Contragent();
        List<PriceSum> price = new ArrayList<>();
        try {
            json = new JSONObject(response);
            JSONObject dataObj = json.getJSONObject("data");
            JSONObject contrObj = dataObj.getJSONObject("contragent");

            contragent.setId(contrObj.getString("id"));
            contragent.setUid(contrObj.getString("code"));
            contragent.setInn(contrObj.getString("inn"));
            contragent.setNameContragent(contrObj.getString("title"));
            contragent.setCodePoOkpo(contrObj.getString("PoOKPO"));
            contragent.setUrFace(contrObj.getString("jur"));
            contragent.setRelations(contrObj.getString("relations"));
            contragent.setContactInfo(contrObj.getString("contact_info"));
            contragent.setEmail(contrObj.getString("email"));
            contragent.setJurAddress(contrObj.getString("jur_address"));
            contragent.setSite(contrObj.getString("site"));
            contragent.setLat(Double.parseDouble(contrObj.getString("lat")));
            contragent.setLon(Double.parseDouble(contrObj.getString("lng")));

            score.setNumberAccount(dataObj.getString("id"));
            score.setClient(contragent);
            score.setDateAccount(dataObj.getLong("date"));
            score.setPriority(Integer.parseInt(dataObj.getString("prio")));
            score.setStatus(Integer.parseInt(dataObj.getString("state")));
            score.setContactFace(dataObj.getString("contactFace"));
            score.setContract(dataObj.getString("contract"));
            score.setInitialConditions(dataObj.getString("initialConditions"));
            score.setResponsible(dataObj.getString("responsible"));
            score.setOrderWorks(dataObj.getString("orderWorks"));
            score.setAnnotation(dataObj.getString("annotation"));

            JSONArray prodArray = dataObj.getJSONArray("products");
            double totalSumScore = 0;
            for (int i = 0; i < prodArray.length(); i++) {
                JSONObject prodObj = prodArray.getJSONObject(i);
                PriceSum prod = new PriceSum();
                prod.setId(Integer.parseInt(prodObj.getString("id")));
                prod.setTitle(prodObj.getString("title").replace("&quot;", "\""));
                String priceString = prodObj.getString("price").replace(",", ".");
                String convertPrice = priceString.replace(" ", "");
                prod.setPrice(Double.parseDouble(convertPrice));
                prod.setSum(Integer.parseInt(prodObj.getString("count")));
                prod.setTotlalCoast(prod.getPrice()*(double)prod.getSum());
                totalSumScore += prod.getTotalCoast();
                price.add(prod);
            }
            score.setProductList(price);
            score.setSumScore(totalSumScore);
            score.setLinkUrl(dataObj.getString("link"));
            return score;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Contact parseContact(InputStream stream){
        String response = convertStreamToString(stream);
        JSONObject json;
        Log.d("myLog", "parseContact = " + response);
        Contact contact = new Contact();
        try {
            json = new JSONObject(response);
            JSONObject data = json.getJSONObject("data");
            contact.setId(data.getString("id"));
            contact.setContragentId(data.getString("contragent"));
            contact.setName(data.getString("fio"));
            contact.setComments(data.getString("comment"));
            contact.setWorkEmail(data.getString("email"));
            contact.setWorkPhone(data.getString("workPhone"));
            contact.setMobilePhone(data.getString("mobPhone"));
            contact.setRole(data.getString("role"));

            return contact;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public boolean parseUpdateContact(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        Log.d("myLog", response);
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            return AnswerServer.getInstance().isAnswerServer(state);

        } catch(JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean parseAddPhone(InputStream stream) {
        String response = convertStreamToString(stream);
        JSONObject json;
        Log.d("myLog", response);
        try {
            json = new JSONObject(response);
            String state = json.getString("state");
            return AnswerServer.getInstance().isAnswerServer(state);

        } catch(JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
