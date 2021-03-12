
package com.example.weatherapp.openweathermap.group;

import com.example.weatherapp.openweathermap.group.Main;
import com.example.weatherapp.openweathermap.group.Sys;
import com.example.weatherapp.openweathermap.group.Weather;
import com.example.weatherapp.openweathermap.group.Wind;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    public boolean isExpanded=false;
    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("weather")
    @Expose
    public java.util.List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("visibility")
    @Expose
    public Integer visibility;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public Integer dt;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;

}
