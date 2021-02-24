package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public String date;
    public String week;
    public String cityEn;
    public String country;
    public String countryEn;
    public String wea;
    //天气图标
    public String wea_img;
    public String xue;
    @SerializedName("tem")
    public String nowTem;
    @SerializedName("tem1")
    public String minTem;
    @SerializedName("tem2")
    public String maxTem;
    public String win;
    public String win_speed;
    public String win_meter;
    public String humidity;
    public String visibility;
    public String pressure;
    public String air;
    public String air_pm25;
    public String air_level;
    public String air_tips;
    @SerializedName("city")
    public String cityName;
    @SerializedName("cityid")
    public String cityId;
    @SerializedName("update_time")
    public String updateTime;
    @SerializedName("data")
    public List<Basic> data;

}
