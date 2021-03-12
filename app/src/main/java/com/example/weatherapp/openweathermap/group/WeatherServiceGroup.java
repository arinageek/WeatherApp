package com.example.weatherapp.openweathermap.group;

import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceGroup {
    @GET("data/2.5/group?")
    Call<WeatherResponseGroup> getGroupWeatherData(@Query("id") String cities, @Query("lang") String lang, @Query("units") String units, @Query("appid") String app_id);
}