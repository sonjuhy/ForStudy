package com.example.sonjunhyeok.forstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.List;

public class WifiList extends AppCompatActivity implements AutoPermissionsListener {

    IntentFilter intentFilter = new IntentFilter();
    WifiManager wifiManager;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;


    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false); //스캔 성공 여부 값 반환
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }// onReceive()..
                };

    private void scanSuccess() {    // Wifi검색 성공
        List<ScanResult> results = wifiManager.getScanResults();
        System.out.println("results size : " + results.size());
        for(int i=0;i<results.size();i++){
            System.out.println("Wifi SSID : " + results.get(i).SSID);
        }
        mAdapter=new MyAdapter(results);
        recyclerView.setAdapter(mAdapter);
    }

    private void scanFailure() {    // Wifi검색 실패
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.rv_recyclerview);

        //권한에 대한 자동 허가 요청 및 설명
        AutoPermissions.Companion.loadAllPermissions(this,101);

        //Wifi Scna 관련
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);


    }// onCreate()..

    //버튼을 눌렀을 때
    public void clickWifiScan(View view) {
        boolean success = wifiManager.startScan();
        if (!success) Toast.makeText(WifiList.this, "Wifi Scan에 실패하였습니다." ,Toast.LENGTH_SHORT).show();
    }// clickWifiScan()..


    //Permission에 관한 메소드
    @Override
    public void onDenied(int i, String[] strings) {Toast.makeText(this, "onDenied~~", Toast.LENGTH_SHORT).show();}

    @Override
    public void onGranted(int i, String[] strings) {Toast.makeText(this, "onGranted~~", Toast.LENGTH_SHORT).show();}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    //Permission에 관한 메소드
}
