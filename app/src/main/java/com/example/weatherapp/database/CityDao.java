package com.example.weatherapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.weatherapp.database.entities.City;

import java.util.List;


@Dao
public interface CityDao {

    @Insert
    void insert(City city);

    @Update
    void update(City city);

    @Delete
    void delete(City city);

    @Transaction
    @Query("DELETE FROM cities_table")
    void deleteAllCities();

    @Query("SELECT * FROM cities_table")
    LiveData<List<City>> getAllCities();

}
