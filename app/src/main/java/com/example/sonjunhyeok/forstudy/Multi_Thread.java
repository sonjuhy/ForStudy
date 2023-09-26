package com.example.sonjunhyeok.forstudy;

import static com.example.sonjunhyeok.forstudy.MainActivity.test;
public class Multi_Thread extends Thread{
    @Override
    public void run() {
        super.run();
        int i=0;
        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if(i == 5){
                break;
            }
        }
        test = 1;
    }
}
