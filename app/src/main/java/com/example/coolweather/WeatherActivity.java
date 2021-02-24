package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.HourData;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.unti.HttpUtil;
import com.example.coolweather.unti.Utilty;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        weatherLayout=this.findViewById(R.id.weather_layout);
        titleCity=this.findViewById(R.id.title_city);
        titleUpdateTime=this.findViewById(R.id.title_update_time);
        degreeText=this.findViewById(R.id.degree_text);
        weatherInfoText=this.findViewById(R.id.weather_info_text);
        forecastLayout=this.findViewById(R.id.forecast_layout);
        aqiText=this.findViewById(R.id.aqi_text);
        pm25Text=this.findViewById(R.id.pm25_text);
        comfortText=this.findViewById(R.id.comfort_text);
        carWashText=this.findViewById(R.id.car_wash_text);
        sportText=this.findViewById(R.id.sport_text);
        swipeRefresh=this.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary);
        bingPicImg=this.findViewById(R.id.bing_pic_img);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        String bingPic=prefs.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        final String city;
        if(weatherString!=null){
            Weather weather= Utilty.handleWeatherResponse(weatherString);
            city=weather.cityName;
            showWeatherInfo(weather);
        }else{
            city=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(city);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(city);
            }
        });

        drawerLayout=this.findViewById(R.id.draw_layout);
        navButton=this.findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


    private void loadBingPic() {
        String requestUrl="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bingPic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId){
        String weatherUrl="https://www.tianqiapi.com/api?version=v6&appid=96464641&appsecret=2KJkY9fI&city="+weatherId;
        System.out.println(weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                    }
                });
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utilty.handleWeatherResponse(responseText);
                System.out.println("转换后的城市名："+weather.cityName);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else
                        {
                            Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }

                });
            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather){
        String cityName=weather.cityName;
        String updateTime=weather.updateTime.split(" ")[1];
        String degree=weather.nowTem+"℃";
        String weatherInfo=weather.wea;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
//        for (HourData hourData:weather.data.get(0).hourData){
//            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
//            TextView dateText=view.findViewById(R.id.date_text);
//            TextView infoText=view.findViewById(R.id.info_text);
//            TextView maxText=view.findViewById(R.id.max_text);
//            TextView minText=view.findViewById(R.id.min_text);
//            dateText.setText(hourData.hours);
//            infoText.setText(hourData.wea);
//            maxText.setText(hourData.tem);
//            minText.setText(hourData.win);
//            forecastLayout.addView(view);
//        }

        aqiText.setText(weather.air);
        pm25Text.setText(weather.air_pm25);

        String comfort=weather.air_tips;
        String carWash=weather.air_tips;
        String sport=weather.air_tips;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
