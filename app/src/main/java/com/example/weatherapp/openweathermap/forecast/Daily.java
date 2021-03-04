
package com.example.weatherapp.openweathermap.forecast;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Daily {

    @SerializedName("dt")
    @Expose
    public Integer dt;
    @SerializedName("sunrise")
    @Expose
    public Integer sunrise;
    @SerializedName("sunset")
    @Expose
    public Integer sunset;
    @SerializedName("temp")
    @Expose
    public Temp temp;
    @SerializedName("feels_like")
    @Expose
    public FeelsLike feelsLike;
    @SerializedName("pressure")
    @Expose
    public Integer pressure;
    @SerializedName("humidity")
    @Expose
    public Integer humidity;
    @SerializedName("dew_point")
    @Expose
    public Double dewPoint;
    @SerializedName("wind_speed")
    @Expose
    public Double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    public Integer windDeg;
    @SerializedName("weather")
    @Expose
    public List<Weather_> weather = null;
    @SerializedName("clouds")
    @Expose
    public Integer clouds;
    @SerializedName("pop")
    @Expose
    public Double pop;
    @SerializedName("snow")
    @Expose
    public Double snow;
    @SerializedName("uvi")
    @Expose
    public Double uvi;

}
