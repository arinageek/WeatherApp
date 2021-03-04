package com.example.weatherapp.openweathermap.forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceForecast {
    @GET("data/2.5/onecall?")
    Call<WeatherResponseForecast> getForecastWeatherData(@Query("lat") Double lat, @Query("lon") Double lon, @Query("exclude") String exclude, @Query("lang") String lang, @Query("units") String units, @Query("appid") String app_id);
//exclude: minutely,hourly,alerts
}