package com.example.sonjunhyeok.forstudy.weather;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Location_get extends AsyncTask<String,Void,ArrayList> {
    private String state, city, leaf;
    private String url_top = "https://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
    private String url_middle = "https://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.";
    private String url_leaf = "https://www.kma.go.kr/DFSROOT/POINT/DATA/leaf.";
    private URL url;
    private HttpURLConnection httpURLConnection;
    private URLConnection conn;
    private OutputStream wr;
    private InputStream inputStream;
    private BufferedReader br;

    private ArrayList<Integer> test = new ArrayList<>();
    private ArrayList<Integer> test_city = new ArrayList<>();
    private ArrayList<Integer> test_leaf = new ArrayList<>();
    private Location_data location_data;
    public void set_locaion(Location_data locaion){
        location_data = locaion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Location_data> doInBackground(String... strings) {
        state = strings[0];
        city = strings[1];
        leaf = strings[2];

        location_data.setState_name(state);
        location_data.setCity_name(city);
        location_data.setLeaf_name(leaf);

        String result;
        int code;

        try {
            long beforeTopTime = System.currentTimeMillis();
            url = new URL(url_top);
            conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            result = br.readLine().toString();
            System.out.println("result : "+result);
            br.close();
            code = XmlParsing(result, state);
            //XmlParsing_test(result,0);
            if(code == -1){
                System.out.println("error state");
                return null;
            }
            System.out.println("state code : "+ code);
            location_data.setState_code(code);
            long afterTopTime = System.currentTimeMillis();
            long totalTime = (afterTopTime - beforeTopTime)/1000;
            System.out.println("Top time : " + totalTime);

            beforeTopTime = System.currentTimeMillis();
            url = new URL(url_middle+Integer.toString(code)+".json.txt");
            conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            result = br.readLine().toString();
            System.out.println("result : "+result);
            br.close();
            code = XmlParsing(result, city);
            if(code == -1){
                System.out.println("error city");
                return null;
            }
            System.out.println("city code : "+ code);
            location_data.setCity_code(code);
            afterTopTime = System.currentTimeMillis();
            totalTime = (afterTopTime - beforeTopTime)/1000;
            System.out.println("middle time : " + totalTime);

            beforeTopTime = System.currentTimeMillis();
            url = new URL(url_leaf+Integer.toString(code)+".json.txt");
            conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            result = br.readLine().toString();
            System.out.println("result : "+result);
            br.close();
            code = XmlParsing_leaf(result, location_data);

            if(code == -1){
                System.out.println("error leaf");
                return null;
            }
            afterTopTime = System.currentTimeMillis();
            totalTime = (afterTopTime - beforeTopTime)/1000;
            System.out.println("bottom time : " + totalTime);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    private int XmlParsing_test(String parsing, int mode){
        try{
            JSONArray jsonArray = new JSONArray(parsing);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsontmp = jsonArray.getJSONObject(i);
                if(mode == 0) {
                    test.add(jsontmp.getInt("code"));
                }
                else if(mode == 1){
                    test_city.add(jsontmp.getInt("code"));
                }
                else {
                    test_leaf.add(jsontmp.getInt("code"));
                }
                System.out.println("All value : " + jsontmp.getString("value")+
                        " All code : " + jsontmp.getInt("code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private int XmlParsing(String parsing, String search){
        try{
            System.out.println("search is : "+search);
            JSONArray jsonArray = new JSONArray(parsing);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsontmp = jsonArray.getJSONObject(i);
                if(search.equals(jsontmp.getString("value"))){
                    return jsontmp.getInt("code");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private int XmlParsing_leaf(String parsing, Location_data location_data){
        try{
            JSONArray jsonArray = new JSONArray(parsing);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsontmp = jsonArray.getJSONObject(i);
                if(location_data.getLeaf_name().equals(jsontmp.getString("value"))){
                    location_data.setX_code(jsontmp.getInt("x"));
                    location_data.setY_code(jsontmp.getInt("y"));
                    location_data.setLeaf_code(jsontmp.getInt("code"));
                    return 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
