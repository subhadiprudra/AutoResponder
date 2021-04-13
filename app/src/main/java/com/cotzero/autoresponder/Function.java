package com.cotzero.autoresponder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Switch;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Function {


    Context context;

    public Function(Context context) {
        this.context = context;
    }

    public String read (String key){

        String MyPREFERENCES = "MyPrefs" ;
        SharedPreferences sharedPreferences= context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String string = sharedPreferences.getString(key,"null");
        return string;

    }


    public void write(String key, String data){

        String MyPREFERENCES = "MyPrefs" ;
        SharedPreferences sharedPreferences= context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,data);
        editor.commit();

    }

    public Boolean insert(JSONObject jsonObject) {

        JSONArray jsonArray ;
        String data = read("list");
        if(data.equals("null")){

            jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            write("list",jsonArray.toString());
            return true;

        }else {

            try {

                jsonArray = new JSONArray(data);
                jsonArray.put(jsonObject);

                write("list",jsonArray.toString());
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }


        }


    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Boolean delete(int id){


        String data = read("list");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);


        for(int i = 0; i<jsonArray.length();i++){

            if(jsonArray.getJSONObject(i).getInt("id")==id){
                jsonArray.remove(i);
                write("list",jsonArray.toString());
                return true;
            }
        }

        return false;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }



    }


    public JSONObject getObject(int id) throws JSONException {


        String data = read("list");
        JSONArray jsonArray = new JSONArray(data);

        for(int i = 0; i<jsonArray.length();i++){

            if(jsonArray.getJSONObject(i).getInt("id")==id){
                return jsonArray.getJSONObject(i);
            }
        }

        return null;

    }


    public int getIndex(int id) throws JSONException {


        String data = read("list");
        JSONArray jsonArray = new JSONArray(data);

        for(int i = 0; i<jsonArray.length();i++){

            if(jsonArray.getJSONObject(i).getInt("id")==id){
                return i;
            }
        }

        return -1;

    }



    public Boolean editObject(int id,JSONObject object) {

        String data = read("list");
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(data);

                for(int i = 0; i<jsonArray.length();i++){

                    if(jsonArray.getJSONObject(i).getInt("id")==id){
                        jsonArray.put(i,object);
                        write("list",jsonArray.toString());
                        return true;
                    }

                }
                return false;

            }catch (JSONException e) {
                e.printStackTrace();

                return true;
            }


    }

    public Boolean enable(int id,Boolean v) {

        String data = read("list");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);

            for(int i = 0; i<jsonArray.length();i++){

                if(jsonArray.getJSONObject(i).getInt("id")==id){

                    JSONObject obj = jsonArray.getJSONObject(i);
                    obj.put("enable",v);

                    jsonArray.put(i,obj);
                    write("list",jsonArray.toString());
                    return true;
                }

            }
            return false;

        }catch (JSONException e) {
            e.printStackTrace();

            return true;
        }


    }



    public JSONArray getList() throws JSONException {

        String data = read("list");

        if(data.equals("null")){
            return new JSONArray();
        }else {
            JSONArray jsonArray = new JSONArray(data);
            return jsonArray;
        }


    }

    public JSONArray getList(int type) throws JSONException {

        String data = read("list");

        if(data.equals("null")){
            return new JSONArray();
        }else {
            JSONArray jsonArray = new JSONArray(data);

            JSONArray jsonArray1 = new JSONArray();

            for(int i =0;i<jsonArray.length();i++){

                JSONArray targets = jsonArray.getJSONObject(i).getJSONArray("target");
                for(int j=0;j<targets.length();j++){
                    if(targets.getInt(j)==type) {
                        jsonArray1.put(jsonArray.getJSONObject(i));
                        break;
                    }
                }
            }

            return jsonArray1;
        }


    }



    public JSONObject target(){

        String data = read("target");

        JSONObject jsonObject = null;

        if(data.equals("null")){
            jsonObject = new JSONObject();
            try {
                jsonObject.put("wp",false);
                jsonObject.put("mes",false);
                jsonObject.put("tel",false);
                jsonObject.put("insta",false);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            write("target",jsonObject.toString());
            return jsonObject;

        }else {
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

    }

    public void enableTarget(int id,Boolean v){
        String data = read("target");
        try {

            JSONObject jsonObject = new JSONObject(data);

            switch (id){

                case 0:
                    jsonObject.put("wp",v);
                    break;

                case 1:
                    jsonObject.put("mes",v);
                    break;

                case 2:
                    jsonObject.put("tel",v);
                    break;

                case 3:
                    jsonObject.put("insta",v);
                    break;

            }

            write("target",jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public boolean isOn(int target){

        String data = read("target");
        try {

            JSONObject jsonObject = new JSONObject(data);

            switch (target){

                case 0:
                    return jsonObject.getBoolean("wp");


                case 1:
                    return jsonObject.getBoolean("mes");


                case 2:
                    return jsonObject.getBoolean("tel");


                case 3:
                    return jsonObject.getBoolean("insta");

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;


    }

    public Boolean isOnIgnoredContacts(JSONObject jsonObject,String name){

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("ignoreContacts");
            for(int i=0;i<jsonArray.length();i++){
                if(jsonArray.getString(i).equals(name))return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isOnContacts(JSONObject jsonObject,String name){

        try {

            JSONArray jsonArray = jsonObject.getJSONArray("contacts");
            if(jsonArray.length()==0)return true;
            for(int i=0;i<jsonArray.length();i++){
                if(jsonArray.getString(i).equals(name))return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isOnMsg(JSONObject jsonObject,String name){

        name = name.toLowerCase();

        try {

            JSONArray jsonArray = jsonObject.getJSONArray("receiveMsg");

            for(int i=0;i<jsonArray.length();i++){
                if(jsonArray.getString(i).equals(name) || jsonArray.getString(i).equals("*") )return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getReply(JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("reply");
            Random random = new Random();
            int index = random.nextInt(jsonArray.length());

            return jsonArray.getString(index);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }


    public JSONObject getRulesCount(){

        int wpcount=0;
        int mesCount=0;
        int telCount=0;
        int instaCount=0;

        try {

            JSONArray jsonArray = getList();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = obj.getJSONArray("target");

                for(int j=0;j<jsonArray1.length();j++){
                    switch (jsonArray1.getInt(j)){
                        case 0:
                            wpcount++;
                            break;
                        case 1:
                            mesCount++;
                            break;
                        case 2:
                            telCount++;
                            break;
                        case 3:
                            instaCount++;
                            break;
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject r = new JSONObject();
        try {

            r.put("wpCount",wpcount);
            r.put("mesCount",mesCount);
            r.put("telCount",telCount);
            r.put("instaCount",instaCount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return r;


    }



















}
