package com.example.sonjunhyeok.forstudy;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonjunhyeok.forstudy.GPS.GpsTracker;
import com.example.sonjunhyeok.forstudy.Service.ServiceMain;
import com.example.sonjunhyeok.forstudy.Service.ServiceTestActivity;
import com.example.sonjunhyeok.forstudy.weather.Location_data;
import com.example.sonjunhyeok.forstudy.weather.Location_get;
import com.example.sonjunhyeok.forstudy.weather.weather_data;
import com.example.sonjunhyeok.forstudy.weather.weather_get;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "192.168.0.254";
    private MqttClient mqttClient;
    private static TextView Tv;
    private static TextView Tv2;
    private static TextView Tv3;
    public static TextView Tv4;
    private String mJonString;
    private static Button bt;
    public static int test;

    private weather_data weather_data;
    protected ArrayList<User> userarray;
    protected  User ur;

    private GpsTracker gpsTracker;

    private LineChart lineChart;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "notification_channel";
    private NotificationManager manager;

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private Messenger mService = null;
    private ServiceMain mServiceMain = null;
    private boolean mIsBind = false;

    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarTrans(true);
        Button b1 = findViewById(R.id.button);
        Button b2 = findViewById(R.id.button3);
        TextView textView = findViewById(R.id.textView);


        startService(new Intent(this, ServiceMain.class));

        createNotificationChannel();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("out if","here");
                sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String[] tmp = {"first","second"};
                JSONArray a = new JSONArray();
                for(int i=0;i<2;i++){
                    a.put(tmp[i]);
                }
                editor.putString("testkey",a.toString());
                editor.apply();
                Log.i("button1","click");
               /* if(mIsBind){
                    Log.d("in if","here");
                    Message msg = Message.obtain(null, ServiceMain.MSG_TEST_FRIST,0,0);
                    msg.replyTo = mMessenger;
                    try {
                        mService.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }*/
                //sendNotification();

                /*SSHControl sshControl = new SSHControl("sonjuhy.iptime.org","sonjuhy","son278298");
                sshControl.execute();*/
                /*try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, testActivity.class);
                /*Intent intent = new Intent(MainActivity.this, ServiceTestActivity.class);
                startActivity(intent);*/
                String jsontmp = sharedPreferences.getString("testkey",null);
                ArrayList<String> data = new ArrayList<>();
                try {
                    JSONArray a = new JSONArray(jsontmp);
                    for(int i=0;i<2;i++){
                        String tmp = a.optString(i);
                        data.add(tmp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i<data.size();i++) {
                    System.out.println("button2 : " + data.get(i));
                }

            }
        });

    }
    /*class ActivityHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ServiceMain.MSG_TEST_FRIST:
                    System.out.println("msg frist here");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }*/
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mIsBind = true;
             /*ServiceMain.ServiceBinder binder = (ServiceMain.ServiceBinder) service;
            mServiceMain = binder.getService();*/
            try{
                Message msg = Message.obtain(null, ServiceMain.MSG_TEST_FRIST);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mIsBind = false;
        }
    };

    //private final Messenger mMessengerMain = new Messenger(new ActivityHandler());
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("messenger here");
            Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();
            switch (msg.what){
                case ServiceMain.MSG_TEST_FRIST:
                    Bundle bundle = msg.getData();
                    System.out.println("messenger first : " + bundle.getString("data"));
                    break;
                case ServiceMain.MSG_TEST_SECOND:
                    Bundle bundler = msg.getData();
                    System.out.println("messenger second : " + bundler.getString("data"));
                    break;
            }
            return false;
        }
    }));

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("on start","Main activity");
        bindService(new Intent(this, ServiceMain.class),mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("on stop","Main activity");
        if(mIsBind){
            if(mService != null){
                try{
                    Message msg = Message.obtain(null, ServiceMain.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(mConnection);
            mIsBind = false;
        }
    }

    public void sendNotification(){
        NotificationCompat.Builder builder = getNotificationBuidler();
        manager.notify(NOTIFICATION_ID, builder.build());
    }
    public void createNotificationChannel(){
        manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "test", manager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Noti from Mascot");

            manager.createNotificationChannel(notificationChannel);
        }
    }
    private NotificationCompat.Builder getNotificationBuidler(){
        Intent nofi_intent = new Intent(this, testActivity.class);
        PendingIntent noti_pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, nofi_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("noti titile")
                .setContentText("noti content")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(noti_pendingIntent)
                .setAutoCancel(true);
        return builder;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ( requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if ( check_result ) {
                //위치 값을 가져올 수 있음
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    10);
            System.out.println("in fun : " + addresses.get(0).toString());
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }
    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    IntentFilter intentFilter = new IntentFilter();
    private WifiManager wifiManager;
    private String[] wifi_list;
    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false); //스캔 성공 여부 값 반환
            if(success){
                scanSucess();
            }
            else{
                scanFailed();
            }
        }
    };
    public void Wifi_Manage(){
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);

        //wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    private void scanSucess(){
        List<ScanResult> sr =  wifiManager.getScanResults();
        wifi_list=new String[sr.size()];
        Log.e("ssid","size="+sr.size());
        for(ScanResult r : sr){
            //Log.e("ssid",r.toString());
            System.out.println("ssid : " + r.toString());
        }
    }
    private void scanFailed(){
        Log.e("ssid","Failed");
    }
    private void connectMqtt() throws Exception {
        mqttClient = new MqttClient("tcp://192.168.0.254:1886", MqttClient.generateClientId(), null);
        mqttClient.connect();
        mqttClient.subscribe("topic");
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("Message is arrived");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
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
    protected  void setStatusBarTrans(boolean b){
        if (b) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    public class ForGPS extends Thread{
        Location_data location_data;
        public void set_location(Location_data location){
            this.location_data = location;
            return;
        }
        @Override
        public void run() {
            super.run();
            weather_get weather = new weather_get();
            String date = weather.fn_time();
            String time = weather.fn_timeChange(date);
            System.out.println("date :" + date.substring(0,8));
            weather_data.setType("UltraFcst");
            weather.setWeather(weather_data);
            /*weather.execute("UltraFcst","11","1", date.substring(0,8), time,
                    Integer.toString(location_data.getX_code()),
                    Integer.toString(location_data.getY_code()));*/
            weather.execute("UltraFcst","11","1", date.substring(0,8), time,
                    Integer.toString(91),
                    Integer.toString(76));
        }
    }
    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // �떎瑜� Thread�뿉�꽌 �쟾�떖諛쏆� Message 泥섎━
            if (msg.what == 1000) {
                System.out.println("Handler in what == 1000");
                //sResultTextView.setText("Handler sendEmptyMessage()瑜� �넻�븳 handleMessage() �떎�뻾");
            }
        }
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