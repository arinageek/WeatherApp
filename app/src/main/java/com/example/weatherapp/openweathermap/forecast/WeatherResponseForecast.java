
package com.example.weatherapp.openweathermap.forecast;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherResponseForecast {

    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("timezone_offset")
    @Expose
    public Integer timezoneOffset;
    @SerializedName("current")
    @Expose
    public Current current;
    @SerializedName("daily")
    @Expose
    public List<Daily> daily = null;

}
