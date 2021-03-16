package com.example.weatherapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.weatherapp.database.entities.City;

import java.util.List;


@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(City city);

    @Query("DELETE FROM cities_table WHERE cityId = :cityId")
    void delete(int cityId);

    @Query("SELECT * FROM cities_table")
    LiveData<List<City>> getAllCities();

}
