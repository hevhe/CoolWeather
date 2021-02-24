package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {

    public String day;
    public String date;
    public String week;
    public String wea;
    public String wea_img;
    public String wea_day;
    public String wea_day_img;
    public String wea_night;
    public String wea_night_img;
    public String tem;
    public String tem1;
    public String tem2;
    public String humidity;
    public String visibility;
    public String pressure;
    public String win_speed;
    public String win_meter;
    public String sunrise;
    public String sunset;
    public String air;
    public String air_level;
    public String air_tips;
    @SerializedName("index")
    public List<Suggestion> suggestions;
    @SerializedName("hours")
    public List<HourData> hourData;
}
