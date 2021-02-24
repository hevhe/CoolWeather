package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {
    @SerializedName("title")
    public String title;
    @SerializedName("level")
    public String level;
    @SerializedName("desc")
    public String desc;

}
