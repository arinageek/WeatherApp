
package com.example.weatherapp.openweathermap.group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("timezone")
    @Expose
    public Integer timezone;
    @SerializedName("sunrise")
    @Expose
    public Integer sunrise;
    @SerializedName("sunset")
    @Expose
    public Integer sunset;

}
