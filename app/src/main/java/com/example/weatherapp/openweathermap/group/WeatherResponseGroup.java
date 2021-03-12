
package com.example.weatherapp.openweathermap.group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherResponseGroup {

    @SerializedName("cnt")
    @Expose
    public Integer cnt;
    @SerializedName("list")
    @Expose
    public java.util.List<com.example.weatherapp.openweathermap.group.List> list = null;

}
