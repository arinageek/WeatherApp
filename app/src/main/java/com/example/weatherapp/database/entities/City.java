package com.example.weatherapp.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName="cities_table")
public class City {
    @NonNull
    @PrimaryKey
    private int cityId;

    private String name;

    public City(String name, int cityId){
        this.name = name;
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public int getCityId(){return cityId;}

    public void setName(String name) {
        this.name = name;
    }
}
