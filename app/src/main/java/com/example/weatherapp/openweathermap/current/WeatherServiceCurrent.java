package com.example.weatherapp.openweathermap.current;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceCurrent {
    @GET("data/2.5/weather?")
    Call<WeatherResponseCurrent> getCurrentWeatherData(@Query("q") String city, @Query("lang") String lang, @Query("units") String units, @Query("appid") String app_id);
}