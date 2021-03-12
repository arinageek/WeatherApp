package com.example.weatherapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.openweathermap.group.WeatherResponseGroup;

import java.util.List;

public class SavedLocationsViewModel extends AndroidViewModel {
    private CityRepository repository;
    private LiveData<List<City>> allCities;
    private static MutableLiveData<WeatherResponseGroup> weather = new MutableLiveData<>();

    public SavedLocationsViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
        weather = repository.getCitiesWeather();
    }

    public LiveData<List<City>> getAllCities() {
        return allCities;
    }

    public MutableLiveData<WeatherResponseGroup> getWeather(){return weather;}

    public void delete(int cityId) {
        repository.delete(cityId);
    }

    public void getCitiesWeather(List<City> cities){ repository.getCitiesWeather(cities); }

}
