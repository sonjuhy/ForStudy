package com.example.sonjunhyeok.forstudy.Service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sonjunhyeok.forstudy.R;

public class ServiceTestActivity extends AppCompatActivity {
    Messenger mService;
    boolean mIsBind = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
        Button button = findViewById(R.id.button_service);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsBind){
                    Message msg = Message.obtain(null, ServiceMain.MSG_TEST_SECOND,0,0);
                    msg.replyTo = mMessenger;
                    try {
                        mService.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private final Messenger mMessenger = new Messenger(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),"here test : "+msg.what,Toast.LENGTH_SHORT).show();
            switch (msg.what){
                case ServiceMain.MSG_TEST_SECOND:
                    Bundle bundle = msg.getData();
                    Log.d("messenger activity","here");
                    System.out.println("mseenger second in activity : " + bundle.getString("data"));
                    break;
                default:
                    break;
            }
            return false;
        }
    }));

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("on start","sub activity");
        bindService(new Intent(this, ServiceMain.class),mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("on stop","sub activity");
        if(mIsBind){
            unbindService(mConnection);
            mIsBind = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mIsBind = true;
            try{
                Message msg = Message.obtain(null, ServiceMain.MSG_TEST_SECOND);
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
}