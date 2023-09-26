package com.example.sonjunhyeok.forstudy;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import com.jcraft.jsch.*;

public class SSHControl extends AsyncTask<String, Void, String> {
    private String hostname;
    private String username;
    private String password;
    private int mode;

    private JSch jsch;
    private Session session;
    private Channel channel;
    private ChannelSftp channelSftp;
    private ChannelExec channelExec;
    private  FileOutputStream fileOutputStream;

    public SSHControl(String hostname, String username, String password){
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... _param) {
        ChannelSftp channelSftp = null;
        InputStream inputStream = null;
        FileInputStream OutputStream = null;
        try {
            System.out.println("ssh start");
            jsch = new JSch();
            session = jsch.getSession(username, hostname, 22);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            //connect finish

            mode = 3;
            for(int j = 0;j<1;j++){
                if (mode == 1) {//ssh
                    System.out.println("SSH mode");
                    channel = session.openChannel("exec");
                    channelExec = (ChannelExec) channel;
                    channelExec.setPty(true);

                    //channelExec.setCommand("export DISPLAY=:0 && omxplayer /home/ABA/01.mp4");
                    channelExec.setCommand("cd /websites/ssl/www/home && dir");

                    StringBuilder stringBuilder = new StringBuilder();
                    inputStream = channel.getInputStream();
                    ((ChannelExec) channel).getErrStream();
                    //channelExec.connect();
                    channel.connect();

                    System.out.println("inpuStream : " + inputStream.toString());
                    byte[] tmp = new byte[1024];
                    while (true) {
                        while (inputStream.available() > 0) {
                            int i = inputStream.read(tmp, 0, 1024);
                            if (i < 0) {
                                break;
                            }
                            System.out.println(new String(tmp, 0, i));
                        }
                        if (channel.isClosed()) {
                            if (inputStream.available() > 0) {
                                continue;
                            }
                            System.out.println("exit-status : " + ((ChannelExec) channel).getExitStatus());
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    inputStream.close();
                }
            }
            if(mode == 2){//file donwload
                channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp)channel;
                channelSftp.cd("/home/sonjuhy/testf");
                inputStream = channelSftp.get("test.mp4");

                fileOutputStream = new FileOutputStream(new File(_param[0]));
                byte[] buffer = new byte[1024];
                int i, downsize = 0;
                while((i = inputStream.read()) != -1){
                    fileOutputStream.write(i);
                    downsize += i;
                    if(downsize%100 == 0) {
                        System.out.println("DownLoaded : " + downsize + " i : "+i);
                    }
                }
            }
            else if(mode == 3){
                channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp) channel;
                Vector ls = channelSftp.ls("/websites/ssl/www/ABA/");

                String text = null;
                ArrayList<String> textArray = new ArrayList<>();
                for(int i=0;i<ls.size();i++){
                    text = ls.get(i)+"\n";
                    textArray.add(text);
                    text = null;
                }
                for(int i=0;i<textArray.size();i++){
                    System.out.println("text["+i+"] : " + textArray.get(i).toString());
                }


                channelSftp.put(_param[0], "/home/sonjuhy/testf/earthvideo.mp4", new SftpProgressMonitor() {
                    private long max = 0;
                    private long count = 0;
                    private long percent = 0;
                    @Override
                    public void init(int i, String s, String s1, long l) {
                        this.max = l;
                    }

                    @Override
                    public boolean count(long bytes) {
                        this.count += bytes;
                        long percentNow = this.count*100/max;
                        if(percentNow>this.percent){
                            this.percent = percentNow;
                            System.out.println("progress : " + this.percent); // 프로그래스
                        }
                        return true;
                    }

                    @Override
                    public void end() {

                    }
                });
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        finally {
            /*try {
                //fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        channel.disconnect();
        System.out.println("finish");
        return null;
    }

}
