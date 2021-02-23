package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public Update update;
    public String weatherId;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
