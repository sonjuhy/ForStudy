package com.example.sonjunhyeok.forstudy.weather;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class weather_get extends AsyncTask<String, Void, String> {
    private String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst?serviceKey=8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D&numOfRows=10&pageNo=1&base_date=20151201&base_time=0630&nx=55&ny=127";
    private String key = "8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D";
    private String url_UltraNcst = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst"; //need key, pageNo, numOfRows, base_date, base_time, x, y | 초단기실황
    private String url_UltraFcst ="http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst"; //need key, pageNo, numOfRows, base_date, base_time, x, y | 초단기예보
    private String url_VilageFcst = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";// need key, pageNo, numOfRows, dataType, base_date, base_time, x, y | 동네예보조회
    private String url_main = null;

    private String numOfRows = null;
    private String pageNo = null;
    private String base_date = null;
    private String base_time = null;
    private String place_x = null;
    private String place_y = null;

    private URLConnection conn;
    private OutputStream wr;
    private InputStream inputStream;
    private BufferedReader br;

    private weather_data weather = null;
    String ConnectValue = "";
    private String test1 ="http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D&pageNo=1&numOfRows=10&dataType=XML&base_date=20210213&base_time=0600&nx=96&ny=76";
    private String test2 ="http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D&pageNo=1&numOfRows=10&dataType=XML&base_data=20210213&base_time=0500&nx=91&ny=76";
    public String fn_time() {

        SimpleDateFormat Format = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date time = new Date();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now();
            String date_string = Integer.toString(date.getYear())+date.getMonthValue()+date.getDayOfMonth();
            System.out.println("local date : " + date_string);
        }

        String timedata = Format.format(time);
        System.out.println("time data : " + time);
        for(int i=0;i<test1.length();i++){
            if(test1.charAt(i) != test2.charAt(i)){
                System.out.println("i : " + i+ " char[1] : " + test1.charAt(i));
                System.out.println("i : " + i+ " char[2] : " + test2.charAt(i));
            }
        }
        if(test1.equals(test2)){
            System.out.println("same");
        }
        else{
            System.out.println("different");
        }
        return timedata;

    }
    public void setWeather(weather_data weather){
        this.weather = weather;
    }
    @Override
    protected String doInBackground(String... strings) {
        this.numOfRows = strings[1];
        this.pageNo = strings[2];
        this.base_date = strings[3];
        this.base_time = strings[4];
        this.place_x = strings[5];
        this.place_y = strings[6];
        if(strings[0].equals("UltraNcst")) {
            url_main = url_UltraNcst + "?serviceKey="+key+"&pageNo="+this.pageNo+"&numOfRows="+this.numOfRows+"&dataType=JSON&base_date="+this.base_date+"&base_time="+this.base_time+"&nx="+this.place_x+"&ny="+this.place_y;
        }
        else if(strings[0].equals("UltraFcst")){
            url_main = url_UltraFcst + "?serviceKey="+key+"&pageNo="+this.pageNo+"&numOfRows="+this.numOfRows+"&dataType=JSON&base_date="+this.base_date+"&base_time="+this.base_time+"&nx="+this.place_x+"&ny="+this.place_y;
        }
        else if(strings[0].equals("VilageFcst")){
            url_main = url_VilageFcst + "?serviceKey="+key+"&pageNo="+this.pageNo+"&numOfRows="+this.numOfRows+"&dataType=JSON&base_date="+this.base_date+"&base_time="+this.base_time+"&nx="+this.place_x+"&ny="+this.place_y;
        }
        try
        {
            System.out.println("url : " + url_main);
            URL url = new URL(url_main);
            conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ResData = br.readLine();
            System.out.println("resDAta : " + ResData);
            if(ResData == null) {
                System.out.println("응답데이터 == NULL");
            }
            else {
                ConnectValue = fn_Jsonp(ResData);
                System.out.println("connect value : " + ConnectValue);

            }
            br.close();
        }catch(Exception e)
        {
            System.out.println("error : "  + e.getMessage());
        }

        return ConnectValue;
    }
    /****************************************************************************
     * JSON 데이터 파싱함수
     * 데이터 추출
     * @param Data
     * **************************************************************************/
    public String fn_Jsonp(String Data)//UltraNcst
    {
        JSONObject WeatherData;
        String VALUE = "";
        String date = "";
        String time = "";
        String DataValue = "";
        String info = "";

        try
        {
            JSONObject jsonObject = new JSONObject(Data);
            JSONObject jsonObject_res = (JSONObject)jsonObject.get("response");
            JSONObject jsonObject_header = (JSONObject) jsonObject_res.get("body");
            JSONObject jsonObject_body = (JSONObject)jsonObject_header.get("items");
            JSONArray jsonArray = jsonObject_body.getJSONArray("item");

            System.out.println("json array length : " + jsonArray.length());
            this.weather = JsonParsing(jsonArray, this.weather);
            /*for(int i = 0; i < jsonArray.length(); i++)
            {
                WeatherData = (JSONObject) jsonArray.get(i);

                date = WeatherData.get("baseDate").toString();
                time = WeatherData.get("baseTime").toString();
                //DataValue = WeatherData.get("fcstValue").toString();
                info = WeatherData.get("category").toString();
                DataValue = WeatherData.get("obsrValue").toString();
                System.out.println("parsing data : " + date);


                if(info.equals("POP")) {

                    info = "강수확률";
                    DataValue  = DataValue+" %";
                }
                if(info.equals("REH")) {

                    info = "습도";
                    DataValue = DataValue+" %";
                }
                if(info.equals("SKY")) {
                    info = "하늘상태";
                    if(DataValue.equals("1")) {
                        DataValue = "맑음";
                    }else if(DataValue.equals("2")) {
                        DataValue = "비";
                    }else if(DataValue.equals("3")) {
                        DataValue = "구름많음";
                    }else if(DataValue.equals("4")) {
                        DataValue = "흐림";
                    }
                }
                if(info.equals("UUU")) {
                    info = "동서성분풍속";
                    DataValue = DataValue+" m/s";
                }
                if(info.equals("VVV")) {
                    info = "남북성분풍속";
                    DataValue = DataValue+" m/s";
                }
                if(info.equals("T1H")) {
                    info = "기온";
                    DataValue = DataValue+"℃";
                }
                if(info.equals("R06")) {
                    info = "6시간강수량";
                    DataValue = DataValue + " mm";

                }
                if(info.equals("S06")) {
                    info = "6시간적설량";
                    DataValue = DataValue + " mm";
                }
                if(info.equals("PTY")){
                    info = "강수형태";
                    if(DataValue.equals("0")) {
                        DataValue = "없음";
                    }else if(DataValue.equals("1")) {
                        DataValue = "비";
                    }else if(DataValue.equals("2")) {
                        DataValue = "눈/비";
                    }else if(DataValue.equals("3")) {
                        DataValue = "눈";
                    }
                }
                if(info.equals("T3H")) {
                    info = "3시간기온";
                    DataValue = DataValue + " ℃";
                }
                if(info.equals("VEC")) {
                    info = "풍향";
                    DataValue = DataValue + " m/s";
                }


                VALUE += info + "," +  DataValue + "," + date + "," + time + ",";
            }*/

        }catch(Exception e)
        {
            System.out.println("error" + e.getMessage());
        }

        return VALUE;
    }
    public weather_data JsonParsing(JSONArray jsonArray, weather_data weather) {
        JSONObject WeatherData;
        String DataValue = "";
        String info = "";
        String fsct_date = null;
        String fsct_time = null;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                WeatherData = (JSONObject) jsonArray.get(i);

                //DataValue = WeatherData.get("fcstValue").toString();
                info = WeatherData.get("category").toString();
                if("UltraNcst".equals(weather.getType())){
                    DataValue = WeatherData.get("obsrValue").toString();
                }
                else{
                    DataValue = WeatherData.get("fcstValue").toString();
                    fsct_time = WeatherData.get("fcstTime").toString();
                    fsct_date = WeatherData.get("fcstDate").toString();
                    weather.setFcstDate(fsct_date);
                    weather.setFcstTime(fsct_time);
                    System.out.println("info value : " + info);
                    System.out.println("Data value : " + DataValue);
                }
                switch (weather.getType()){
                    case "UltraNcst":// T1H, RN1, UUU, VVV, REH, PTY, VEC, WSD
                    {
                        if (info.equals("WSD")) {
                            info = "풍속";
                            DataValue = DataValue + " m/s";
                            weather.setWSD(DataValue);
                        }
                        if (info.equals("RN1")) {
                            info = "시간당강수량";
                            DataValue = DataValue + " mm";
                            weather.setRN1(DataValue);
                        }
                        if (info.equals("REH")) {
                            info = "습도";
                            DataValue = DataValue + "%";
                            weather.setREH(DataValue);
                        }
                        if (info.equals("UUU")) {
                            info = "동서성분풍속";
                            DataValue = DataValue + " m/s";
                            weather.setUUU(DataValue);
                        }
                        if (info.equals("VVV")) {
                            info = "남북성분풍속";
                            DataValue = DataValue + " m/s";
                            weather.setVVV(DataValue);
                        }
                        if (info.equals("T1H")) {
                            info = "기온";
                            DataValue = DataValue + "℃";
                            weather.setT1H(DataValue);
                        }
                        if (info.equals("PTY")) {
                            info = "강수형태";
                            if (DataValue.equals("0")) {
                                DataValue = "없음";
                                weather.setPTY(DataValue);
                            } else if (DataValue.equals("1")) {
                                DataValue = "비";
                                weather.setPTY(DataValue);
                            } else if (DataValue.equals("2")) {
                                DataValue = "눈/비";
                                weather.setPTY(DataValue);
                            } else if (DataValue.equals("3")) {
                                DataValue = "눈";
                                weather.setPTY(DataValue);
                            }
                        }
                        if (info.equals("VEC")) {
                            info = "풍향";
                            DataValue = DataValue + " m/s";
                            weather.setVEC(DataValue);
                        }
                    }
                    break;
                    case "UltraFcst": //T1H, RN1, SKY, UUU, VVV, REH, PTY, LGT, VEC, WSD
                    {
                        if (info.equals("LGT")){
                            info = "낙뢰";
                            if(DataValue.equals("0")){
                                weather.setLGT("없음");
                            }
                            else if(DataValue.equals("1")){
                                weather.setLGT("있음");
                            }
                        }
                        if (info.equals("WSD")) {
                            info = "풍속";
                            DataValue = DataValue + " m/s";
                            weather.setWSD(DataValue);
                        }
                        if (info.equals("RN1")) {
                            info = "시간당강수량";
                            DataValue = DataValue + " mm";
                            weather.setRN1(DataValue);
                        }
                        if (info.equals("REH")) {
                            info = "습도";
                            DataValue = DataValue + "%";
                            weather.setREH(DataValue);
                        }
                        if (info.equals("SKY")) {
                            info = "하늘상태";
                            if (DataValue.equals("1")) {
                                DataValue = "맑음";
                                weather.setSKY(DataValue);
                            } else if (DataValue.equals("2")) {
                                DataValue = "비";
                                weather.setSKY(DataValue);
                            } else if (DataValue.equals("3")) {
                                DataValue = "구름많음";
                                weather.setSKY(DataValue);
                            } else if (DataValue.equals("4")) {
                                DataValue = "흐림";
                                weather.setSKY(DataValue);
                            }
                        }
                        if (info.equals("UUU")) {
                            info = "동서성분풍속";
                            DataValue = DataValue + " m/s";
                            weather.setUUU(DataValue);
                        }
                        if (info.equals("VVV")) {
                            info = "남북성분풍속";
                            DataValue = DataValue + " m/s";
                            weather.setVVV(DataValue);
                        }
                        if (info.equals("T1H")) {
                            info = "기온";
                            DataValue = DataValue + "℃";
                            weather.setT1H(DataValue);
                        }
                        if (info.equals("PTY")) {
                            info = "강수형태";
                            if (DataValue.equals("0")) {
                                DataValue = "없음";
                                weather.setPTY(DataValue);
                            } else if (DataValue.equals("1")) {
                                DataValue = "비";
                                weather.setPTY(DataValue);
                            } else if (DataValue.equals("2")) {
                                DataValue = "눈/비";
                                weather.setPTY(DataValue);
                            } else if (DataValue.equals("3")) {
                                DataValue = "눈";
                                weather.setPTY(DataValue);
                            }
                        }
                        if (info.equals("VEC")) {
                            info = "풍향";
                            DataValue = DataValue + " m/s";
                            weather.setVEC(DataValue);
                        }
                        break;
                    }
                    case "VilageFcst":// *POP, *PTY, *R06, *REH, *S06, *SKY, *T3H, *TMN, *TMX, *UUU, *VVV, *WAV, *VEC, *WSD
                    {
                        if(info.equals("POP")) {
                            info = "강수확률";
                            DataValue  = DataValue+" %";
                            weather.setPOP(DataValue);
                        }
                        if(info.equals("REH")) {
                            info = "습도";
                            DataValue = DataValue+" %";
                            weather.setREH(DataValue);
                        }
                        if(info.equals("SKY")) {
                            info = "하늘상태";
                            if(DataValue.equals("1")) {
                                DataValue = "맑음";
                            }else if(DataValue.equals("2")) {
                                DataValue = "비";
                            }else if(DataValue.equals("3")) {
                                DataValue = "구름많음";
                            }else if(DataValue.equals("4")) {
                                DataValue = "흐림";
                            }
                            weather.setSKY(DataValue);
                        }
                        if(info.equals("UUU")) {
                            info = "동서성분풍속";
                            DataValue = DataValue+" m/s";
                            weather.setUUU(DataValue);
                        }
                        if(info.equals("VVV")) {
                            info = "남북성분풍속";
                            DataValue = DataValue+" m/s";
                            weather.setVVV(DataValue);
                        }
                        if(info.equals("R06")) {
                            info = "6시간강수량";
                            DataValue = DataValue + " mm";
                            weather.setR06(DataValue);
                        }
                        if(info.equals("S06")) {
                            info = "6시간적설량";
                            DataValue = DataValue + " mm";
                            weather.setS06(DataValue);
                        }
                        if(info.equals("PTY")){
                            info = "강수형태";
                            if(DataValue.equals("0")) {
                                DataValue = "없음";
                            }else if(DataValue.equals("1")) {
                                DataValue = "비";
                            }else if(DataValue.equals("2")) {
                                DataValue = "눈/비";
                            }else if(DataValue.equals("3")) {
                                DataValue = "눈";
                            }
                            weather.setPTY(DataValue);
                        }
                        if(info.equals("T3H")) {
                            info = "3시간기온";
                            DataValue = DataValue + " ℃";
                            weather.setT3H(DataValue);
                        }
                        if(info.equals("VEC")) {
                            info = "풍향";
                            DataValue = DataValue + " m/s";
                            weather.setVEC(DataValue);
                        }
                        /*if (info.equals("WAV")) {
                            info = "파고";
                            DataValue = DataValue + " M";
                            weather.setWSD(DataValue);
                        }
                        if(info.equals("TMN")) {
                            info = "아침최저기온";
                            DataValue = DataValue + " ℃";
                        }
                        if(info.equals("TMX")) {
                            info = "낮최고기온";
                            DataValue = DataValue + " ℃";
                        }*/
                        if (info.equals("WSD")) {
                            info = "풍속";
                            DataValue = DataValue + " m/s";
                            weather.setWSD(DataValue);
                        }
                    }
                    break;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }
        /**********************************************************************
     * 현재시간을 가져와서 ex) 1000 형태로 만들어줌
     * 3시간 마다 업데이트 되기 때문에 각 시간에 따라 업데이트 시간으로 설정
     * @param timedata
     * @return
     * *********************************************************************/
    public String fn_timeChange(String timedata)
    {
        String hh = timedata.substring(9, 11);
        String baseTime = "";
        hh = hh + "00";

        // 현재 시간에 따라 데이터 시간 설정(3시간 마다 업데이트) //
        switch(hh) {

            case "0200":
            case "0300":
            case "0400":
                baseTime = "0200";
                break;
            case "0500":
            case "0600":
            case "0700":
                baseTime = "0500";
                break;
            case "0800":
            case "0900":
            case "1000":
                baseTime = "0800";
                break;
            case "1100":
            case "1200":
            case "1300":
                baseTime = "1100";
                break;
            case "1400":
            case "1500":
            case "1600":
                baseTime = "1400";
                break;
            case "1700":
            case "1800":
            case "1900":
                baseTime = "1700";
                break;
            case "2000":
            case "2100":
            case "2200":
                baseTime = "2000";
                break;
            default:
                baseTime = "2300";

        }
        return baseTime;
    }

}

