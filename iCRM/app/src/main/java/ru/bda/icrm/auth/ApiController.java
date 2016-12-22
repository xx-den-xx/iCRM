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
import ru.bda.icrm.model.Call;
import ru.bda.icrm.model.Contact;
import ru.bda.icrm.model.Contragent;
import ru.bda.icrm.model.Phone;
import ru.bda.icrm.model.Price;
import ru.bda.icrm.model.PriceSum;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 28.06.2016.
 */
public class ApiController {

    private final String mBaseUrl = Constants.API_LINK_2;//API_LINK;
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
        Log.d("myLog", urlString);
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
            return ResponseParser.getInstance().parseContragentList(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public List<Contragent> searchContragent (String token, String string){
        String urlString = mBaseUrl + "?action=getContragentList";
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
            return ResponseParser.getInstance().parseContragentList(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public Contragent getContragent (String token, String uid) {
        String urlString = mBaseUrl + "?action=getContragent&token=" + token + "&contragent=" + uid;
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
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
            Log.d("myLog", "id = " + id + "; contragent = " + contragent.getNameContragent());
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
            //jsonObj.put("phones", phonesArray);
            root.put("data", jsonObj);
            //root.put("contacts", contactsArray);
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
            Log.d("myLog", "Контрагент сохранён успешно");
            return ResponseParser.getInstance().parseSaveContragent(connection.getInputStream(), contragent);
        }catch(Exception e){
            Log.d("myLog", "Контрагент не сохранён");
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
            return ResponseParser.getInstance().parsePrice(connection.getInputStream());
        }catch(Exception e){
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
            return ResponseParser.getInstance().parsePriceSum(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public List<PriceSum> getNomenclatureTree(String token, String parent, int start, int count) {
        String urlString = mBaseUrl + "?action=getNomenclature";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("start", start);
            root.put("count", count);
            root.put("parent", parent);

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
            return ResponseParser.getInstance().parsePriceSum(connection.getInputStream());
        }catch(Exception e){
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
            return ResponseParser.getInstance().parsePrice(connection.getInputStream());
        }catch(Exception e){
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
            return ResponseParser.getInstance().parsePriceSum(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public String saveScore (String token, String idContragent) {
        String urlString = mBaseUrl + "?action=makeInvoice";
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
            return ResponseParser.getInstance().parseNumberScore(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }


    /** updateInvoice
     *  token
     *  invoice - id счета,
     *  delete - если добавить больше нуля, удалит счёт
     *  prio - приоритет счёта (0,1,2)
     *  state - изменяет статус счёта (-1, 0, 1)
     *  products - массив товаров, "id-товара":"кол-во товара"     *
     */
    public boolean updateScore (String token, Score  score, int delete) {
        String urlString = mBaseUrl + "?action=updateInvoice";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("invoice", score.getNumberAccount());
            root.put("delete", delete);
            root.put("state", score.getStatus());
            root.put("prio", score.getPriority());
            root.put("date", score.getDateAccount());
            JSONArray products = new JSONArray();
            for (PriceSum price : score.getProductList()) {
                JSONObject product = new JSONObject();
                product.put("id", price.getId());
                product.put("count", price.getSum());
                product.put("price", price.getPrice());
                products.put(product);
            }
            Log.d("myLog", products.toString());
            root.put("products", products);

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
            return ResponseParser.getInstance().parseUpdateScore(connection.getInputStream());
        }catch(Exception e){
            return false;
        }
    }

    public List<Score> getScoreList (String token){
        String urlString = mBaseUrl + "?action=getInvoiceList";

        try{
            JSONObject root = new JSONObject();
            root.put("token", token);

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
            return ResponseParser.getInstance().parseScoreList(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public Score getScore (String token, String invoice){
        String urlString = mBaseUrl + "?action=getInvoice";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("invoice", invoice);

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
            return ResponseParser.getInstance().parseScore(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public boolean updateFullScore (String token, Score  score, int delete) {
        String urlString = mBaseUrl + "?action=updateInvoice";
        Log.d("myLog", "token = " + token + "; id price = " + score.getNumberAccount());
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("invoice", score.getNumberAccount());
            root.put("delete", delete);
            root.put("state", score.getStatus());
            root.put("prio", score.getPriority());
            root.put("date", score.getDateAccount());
            root.put("contactFace", score.getContactFace());
            root.put("contract", score.getContract());
            root.put("initialConditions", score.getInitialConditions());
            root.put("responsible", score.getResponsible());
            root.put("orderWorks", score.getOrderWorks());
            root.put("annotation", score.getAnnotation());

            JSONArray products = new JSONArray();
            for (PriceSum price : score.getProductList()) {
                JSONObject product = new JSONObject();
                product.put("id", price.getId());
                product.put("title", price.getTitle());
                product.put("price", price.getPrice());
                product.put("count", price.getSum());
                products.put(product);
            }
            Log.d("myLog", products.toString());
            root.put("products", products);

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
            return ResponseParser.getInstance().parseUpdateScore(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return false;
        }
    }

    public boolean updateContact (String token, Contact contact, String idContragent) {
        String urlString = mBaseUrl + "?action=updateContact";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            if (contact.getId().equals("")) {
                root.put("contragent", idContragent);
            } else {
                root.put("contact", contact.getId());
            }

            Log.d("myLog", root.toString());
            root.put("fio", contact.getName());
            root.put("role", contact.getRole());
            root.put("comment", contact.getComments());
            root.put("workPhone", contact.getWorkPhone());
            root.put("mobPhone", contact.getMobilePhone());
            root.put("email", contact.getWorkEmail());
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
            return ResponseParser.getInstance().parseUpdateContact(connection.getInputStream());
        }catch(Exception e){
            Log.d("myLog", "запрос не выполнен");
            return false;
        }
    }

    public Contact getContact (String token, String contact){
        String urlString = mBaseUrl + "?action=getContact";
        Log.d("myLog", urlString);
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("contact", contact);

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
            return ResponseParser.getInstance().parseContact(connection.getInputStream());
        }catch(Exception e){
            return null;
        }
    }

    public boolean addPhone (String token, Phone phone){
        String urlString = mBaseUrl + "?action=addPhone";
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            root.put("contragent", phone.getContactsId());
            root.put("phone", phone.getNumber());
            root.put("type", phone.getType());

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
            return ResponseParser.getInstance().parseAddPhone(connection.getInputStream());
        }catch(Exception e){
            return false;
        }
    }

    public boolean addCall (String token, List<Call> callList){
        String urlString = mBaseUrl + "?action=phoneLog";
        Log.d("phone_log", urlString);
        try{
            JSONObject root = new JSONObject();
            root.put("token", token);
            JSONArray array = new JSONArray();
            for (Call call : callList) {
                JSONObject callObj = new JSONObject();
                callObj.put("login", call.getLogin());
                callObj.put("phone", call.getPhone());
                callObj.put("type", call.getType());
                callObj.put("time", call.getTime());
                array.put(callObj);
            }
            root.put("call", array);

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
            return ResponseParser.getInstance().parseAddPhone(connection.getInputStream());
        }catch(Exception e){
            return false;
        }
    }
}
