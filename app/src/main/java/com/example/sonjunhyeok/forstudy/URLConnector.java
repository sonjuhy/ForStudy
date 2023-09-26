package com.example.sonjunhyeok.forstudy;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import static com.example.sonjunhyeok.forstudy.MainActivity.Tv4;
import static com.example.sonjunhyeok.forstudy.MainActivity.test;

public class URLConnector extends AsyncTask<String, Void, String>{
    private String url;
    private ContentValues values;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println("Update "+s);
    }

    private int cnt = 0;

    @Override
    protected String doInBackground(String... _params) {
        HttpURLConnection httpURLConnection = null;

        String Age = _params[0];
        String Add = _params[2];
        String Name = _params[1];
        test = 10;
        String nameef="!asdfaefaewsf";
        Tv4.setText(nameef);
        try{
            String data = URLEncoder.encode("Age","UTF-8")+"="+URLEncoder.encode(Age,"UTF-8");
            data += "&"+URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(Name,"UTF-8");
            data += "&"+URLEncoder.encode("Address","UTF-8")+"="+URLEncoder.encode(Add,"UTF-8");

            String link = "http://192.168.0.254"+"/updatejson.php";

            URL url = new URL(link);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            System.out.println("data is : " + data);
            //OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            OutputStream wr = httpURLConnection.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            wr.close();

            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                System.out.println("Response OK a");
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
                System.out.println("Response Failed");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");

            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();

            String line;

            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }

            httpURLConnection.disconnect();

            return stringBuilder.toString();

        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro 1");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Erro 2");
            e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("Erro 3");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro 4");
            e.printStackTrace();
        }
        return null;
    }

}
class Login extends AsyncTask<String, Void, String>{

    private String id_tmp=null;//name
    private String pw_tmp=null;//age

    @Override
    protected String doInBackground(String... _param) {
        id_tmp = _param[0];
        pw_tmp = _param[1];

        System.out.println("id : " + id_tmp+"\npw : "+pw_tmp);
        HttpURLConnection httpURLConnection = null;

        try{
            String data = URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pw_tmp,"UTF-8");
            data += "&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(id_tmp,"UTF-8");

            String link = "http://192.168.0.254"+"/login.php";
            System.out.println("login data : " + data);
            URL url = new URL(link);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            OutputStream wr = httpURLConnection.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            System.out.println(data);
            wr.flush();
            wr.close();

            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                System.out.println("Response OK");
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
                System.out.println("Response Failed");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }

            httpURLConnection.disconnect();

            System.out.println("stringbuilder : "+stringBuilder.toString().trim());
            if(stringBuilder.toString().trim().equals("Success")){
                System.out.println("Login Suecces");
            }
            else{
                System.out.println("login failed");
            }

            return stringBuilder.toString();

        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro 1");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Erro 2");
            e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("Erro 3");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro 4");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("login s : "+s);
        super.onPostExecute(s);
    }
}
class Test extends AsyncTask<String, Void, String>{

    private String id_tmp=null;//name
    private String pw_tmp=null;//age

    @Override
    protected String doInBackground(String... _param) {
        HttpURLConnection httpURLConnection = null;

        try{
            String data = "";

            //String link = "http://192.168.0.254"+"/test.php";
            String link = "http://sonjuhy.iptime.org/test.php";
            System.out.println("login data : " + data);
            URL url = new URL(link);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            OutputStream wr = httpURLConnection.getOutputStream();
            wr.write(data.getBytes("UTF-8"));
            System.out.println(data);
            wr.flush();
            wr.close();

            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                System.out.println("Response OK");
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
                System.out.println("Response Failed");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }

            httpURLConnection.disconnect();

            System.out.println("stringbuilder : "+stringBuilder.toString().trim());
            if(stringBuilder.toString().trim().equals("hello?")){
                System.out.println("Login Suecces");
            }
            else {
                System.out.println("login failed");
            }

            return stringBuilder.toString();

        } catch (UnsupportedEncodingException e) {
            System.out.println("Erro 1");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Erro 2");
            e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("Erro 3");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro 4");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("test : "+s);
        super.onPostExecute(s);
    }
}