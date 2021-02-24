package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    @SerializedName("city")
    public String cityName;
    @SerializedName("cityid")
    public String cityId;
    @SerializedName("update_time")
    public String updateTime;
    @SerializedName("data")
    public List<WeatherData> data;

}
