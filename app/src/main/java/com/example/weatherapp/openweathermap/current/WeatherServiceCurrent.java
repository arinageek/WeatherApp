package com.example.weatherapp.openweathermap.current;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceCurrent {
    @GET("data/2.5/weather?")
    Call<WeatherResponseCurrent> getCurrentWeatherDataByName(@Query("q") String city, @Query("lang") String lang, @Query("units") String units, @Query("appid") String app_id);

    @GET("data/2.5/weather?")
    Call<WeatherResponseCurrent> getCurrentWeatherDataByLocation(@Query("lat") String lat, @Query("lon") String lon, @Query("lang") String lang, @Query("units") String units, @Query("appid") String app_id);
}