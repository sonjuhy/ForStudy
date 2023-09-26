package com.example.sonjunhyeok.forstudy.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ServiceMain extends Service {

    Messenger mMessager = null;
    Messenger mClient_frist = null;
    Messenger mClient_seconde = null;

    public static final int MSG_TEST_FRIST = 1;
    public static final int MSG_TEST_SECOND = 2;

    public static final int MSG_UNREGISTER_CLIENT = -1;

    public String word="null";


    private MqttClient mqttClient = null;
    private String TOPIC = "test/pub";
    private String TOPIC_SUB = "test/sub";

    private void MQTT(){
        try {
            mqttClient = new MqttClient("tcp://192.168.0.254:1883", MqttClient.generateClientId(), null);
            mqttClient.connect();
            mqttClient.subscribe(TOPIC_SUB);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("mqtt arrive : " + mqttMessage.toString());
                    mqttClient.publish(TOPIC,new MqttMessage(word.getBytes()));
                    sendToActivity();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    private void sendToActivity(){
        Bundle bundle = new Bundle();
        bundle.putString("data",word);
        Message msg;
        try {
            Log.d("sendtoActivity",word);
            if(word.equals("second")){
                msg = Message.obtain(null, MSG_TEST_SECOND);
            }else{
                msg = Message.obtain(null, MSG_TEST_FRIST);
            }
            msg.setData(bundle);
            mMessager.send(msg);
            System.out.println("sentoactivity");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return Service.START_NOT_STICKY;
        }
        else{
            System.out.println("Mqtt is start");
            MQTT();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Service main onbind start");
        mMessager = new Messenger(new ServiceHandlerInClass(this));
        //binder = mMessager.getBinder();
        return mMessager.getBinder();
        //return binder;
    }


    public class ServiceHandlerInClass extends Handler {
        Context context;

        static final int MSG_TEST_FRIST = 1;
        static final int MSG_TEST_SECOND = 2;

        ServiceHandlerInClass(Context mContext){
            this.context = mContext;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_TEST_FRIST:
                    System.out.println("message arrive here");
                    word = "frist";
                    //mClient_frist = msg.replyTo;
                    mMessager = msg.replyTo;
                    break;
                case MSG_TEST_SECOND:
                    System.out.println("message arrive second");
                    word = "second";
                    mMessager = msg.replyTo;
                    break;
                case  MSG_UNREGISTER_CLIENT:

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
