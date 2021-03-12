package com.example.weatherapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.openweathermap.forecast.Daily;
import java.util.List;


public class HomeViewModel extends AndroidViewModel {

    private CityRepository repository;
    private LiveData<List<City>> allCities;
    public MutableLiveData<Double> _degrees = new MutableLiveData<>();
    public MutableLiveData<String> _date = new MutableLiveData<>();
    public MutableLiveData<String> _description = new MutableLiveData<>("");
    public MutableLiveData<String> _city = new MutableLiveData<>("");
    public MutableLiveData<Integer> _cityId = new MutableLiveData<>();
    public MutableLiveData<String> _icon = new MutableLiveData<>("http://openweathermap.org/img/wn/10d@2x.png");
    public MutableLiveData<List<Daily>> _daily = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
        _degrees = repository.get_degrees();
        _date = repository.get_date();
        _description = repository.get_description();
        _city = repository.get_city();
        _cityId = repository.get_cityId();
        _icon = repository.get_icon();
        _daily = repository.get_daily();
    }

    public void insert(String city, int cityId) { repository.insert(new City(city, cityId)); }

    public void delete(int cityId) {
        repository.delete(cityId);
    }

    public void getCurrentData(String city) { repository.getCurrentData(city); }

}

