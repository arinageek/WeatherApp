package com.example.weatherapp.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName="cities_table")
public class City {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private boolean isHome;

    public City(String name, boolean isHome){
        this.name = name;
        this.isHome = isHome;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHome(boolean home) {
        isHome = home;
    }
}
