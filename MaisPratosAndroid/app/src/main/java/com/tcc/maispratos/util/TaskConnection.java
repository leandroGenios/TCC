package com.tcc.maispratos.util;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;


public class TaskConnection extends AsyncTask<Object, Object, Object> {
    @Override
    protected Object doInBackground(Object... strings) {
        String url = "http://172.30.246.156:8080/MaisPratosWS/rest/" + strings[1];
        HttpHelper http = new HttpHelper();
        if(strings[0].equals(Constants.GET)){
            try {
                String json = http.doGet(url);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(strings[0].equals(Constants.POST)){
            try {
                String json = http.doPost(url, (JSONObject) strings[2], "UTF-8");
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(strings[0].equals(Constants.PUT)){
            try {
                String json = http.doPut(url, (JSONObject) strings[2], "UTF-8");
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(strings[0].equals(Constants.DELETE)){
            try {
                String json = http.doDelete(url);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
