package com.example.sonjunhyeok.forstudy;

import android.os.AsyncTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Date;
import java.time.LocalDate;

public class Network extends AsyncTask<String, Void, String> {
    private HttpURLConnection httpURLConnection = null;
    private OutputStream outputStream = null;
    private String data = null;
    private String link = "https://sonjuhy.iptime.org/Calendar";
    private String line = null;
    private String mJsonString = null;
    private int responseStatusCode = 0;
    private boolean upload_mode = false;
    private LocalDate localDate;
    private Date date;

    @Override
    protected String doInBackground(String... strings) {

        return null;
    }
}
