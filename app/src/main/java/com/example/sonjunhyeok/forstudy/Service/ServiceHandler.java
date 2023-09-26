package com.example.sonjunhyeok.forstudy.Service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;


public class ServiceHandler extends Handler {
    Context context;
    Messenger messenger = null;

    static final int MSG_TEST_FRIST = 1;
    static final int MSG_TEST_SECOND = 2;

    ServiceHandler(Context mContext){
        this.context = mContext;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_TEST_FRIST:
                System.out.println("message arrive here");
                break;
            case MSG_TEST_SECOND:
                System.out.println("message arrive second");
            default:
                super.handleMessage(msg);
                break;
        }
    }
}
