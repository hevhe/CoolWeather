package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Tempetature tempetature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
    public class Tempetature{
        public String max;
        public String min;
    }
}
