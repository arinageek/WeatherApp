
package com.example.weatherapp.openweathermap.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeelsLike {

    @SerializedName("day")
    @Expose
    public Double day;
    @SerializedName("night")
    @Expose
    public Double night;
    @SerializedName("eve")
    @Expose
    public Double eve;
    @SerializedName("morn")
    @Expose
    public Double morn;

}
