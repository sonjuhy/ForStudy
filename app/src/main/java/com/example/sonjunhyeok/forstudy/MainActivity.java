package com.example.sonjunhyeok.forstudy;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "192.168.0.254";
    private static TextView Tv;
    private static TextView Tv2;
    private static TextView Tv3;
    public static TextView Tv4;
    private String mJonString;
    private static Button bt;

    public static int test;

    protected ArrayList<User> userarray;
    protected  User ur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tv = (TextView)findViewById(R.id.Tv_out);
        Tv2 = (TextView)findViewById(R.id.textView);
        Tv3 = (TextView)findViewById(R.id.textView2);
        Tv4 = (TextView)findViewById(R.id.textView3);

        bt = (Button)findViewById(R.id.button);

        userarray = new ArrayList<>();

        Getdata Gd = new Getdata();
        Gd.execute("http://"+ IP_ADDRESS +"/getjson.php","");

        final URLConnector urlConnector = new URLConnector();
        //urlConnector.execute("25","Someone","Seoul");

        //final URLConnector urlConnector = new URLConnector();
       // urlConnector.execute("30","Who?","korea");

        final Login login = new Login();
        login.execute("Son","24");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlConnector.execute("25","Someone","Seoul");
            }
        });
        //Tv.setText(userarray.get(0).Out_name());
    }
    class Getdata extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = strings[0];
            String postParameters = strings[1];

            System.out.println("server : " + serverURL + "\npost : " + postParameters);
            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if(httpURLConnection == null){
                    System.out.println("httpURL is Empty");
                }
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                return stringBuilder.toString().trim();

            } catch (ProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
               return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("s : " + s);
            if(s == null){
                Tv.setText("Error");
            }
            else{
                mJonString = s;
                showResult();
            }
        }
    }
    private void showResult(){
        String TAG_JSON = "sonjuhy";
        String TAG_AGE = "Age";
        String TAG_NAME = "Name";
        String TAG_ADD = "Address";

        try{
            JSONObject jsonObject = new JSONObject(mJonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String age = item.getString(TAG_AGE);
                String name = item.getString(TAG_NAME);
                String add = item.getString(TAG_ADD);

                //add list
                ur = new User();
                ur.Input_data(age,name,add);

                System.out.println("data : " + age + name + add);
                //Tv.setText("test");
                userarray.add(ur);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Tv.setText("이름 : "+userarray.get(0).Out_name()+"\n 나이 : "+ userarray.get(0).Out_age()+"\n 지역 : "+userarray.get(0).Out_add() );
        Tv2.setText("이름 : "+userarray.get(1).Out_name()+"\n 나이 : "+ userarray.get(1).Out_age()+"\n 지역 : "+userarray.get(1).Out_add() );
        Tv3.setText("이름 : "+userarray.get(2).Out_name()+"\n 나이 : "+ userarray.get(2).Out_age()+"\n 지역 : "+userarray.get(2).Out_add() );
    }
}
class User {
    private String age;
    private String name;
    private String add;

    public void Input_data(String age, String name, String add){
        this.age = age;
        this.name = name;
        this.add = add;
    }
    public String Out_name(){
        return this.name;
    }
    public String Out_age(){
        return this.age;
    }
    public String Out_add(){
        return this.add;
    }
}