package com.example.weatherapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private CityRepository repository;
    private LiveData<List<City>> allCities;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
    }

    public LiveData<List<City>> getAllCities(){return allCities;}


}

