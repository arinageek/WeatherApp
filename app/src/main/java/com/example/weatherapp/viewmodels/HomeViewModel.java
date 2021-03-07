package com.example.weatherapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;
import com.example.weatherapp.openweathermap.current.WeatherServiceCurrent;
import com.example.weatherapp.openweathermap.forecast.Daily;
import com.example.weatherapp.openweathermap.forecast.WeatherResponseForecast;
import com.example.weatherapp.openweathermap.forecast.WeatherServiceForecast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeViewModel extends AndroidViewModel {

    private CityRepository repository;
    private LiveData<List<City>> allCities;
    public MutableLiveData<Double> _degrees = new MutableLiveData<>();
    public MutableLiveData<String> _date = new MutableLiveData<>();
    public MutableLiveData<String> _description = new MutableLiveData<>("");
    public MutableLiveData<String> _city = new MutableLiveData<>("");
    public MutableLiveData<String> _icon = new MutableLiveData<>("http://openweathermap.org/img/wn/10d@2x.png");
    public MutableLiveData<List<Daily>> _daily = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
    }

    public void insert(String city) {
        repository.insert(new City(city, false));
    }

    public void update(City city) {
        repository.update(city);
    }

    public void delete(City city) {
        repository.delete(city);
    }

    public LiveData<List<City>> getAllCities() {
        return allCities;
    }

    public void deleteAllCities() {
        repository.deleteAllCities();
    }

   /*public boolean isCitySaved(String name){
        return repository.isCitySaved(name);
    }*/

    public void getCurrentData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherServiceCurrent service = retrofit.create(WeatherServiceCurrent.class);
        Call<WeatherResponseCurrent> call = service.getCurrentWeatherData(city, "ru", "metric", "f955e39cc8ec50741acb39727ead90dd");
        call.enqueue(new Callback<WeatherResponseCurrent>() {
            @Override
            public void onResponse(Call<WeatherResponseCurrent> call, Response<WeatherResponseCurrent> response) {
                if (response.code() == 200) {
                    WeatherResponseCurrent weatherResponse = response.body();
                    assert weatherResponse != null;
                    _degrees.postValue(weatherResponse.main.temp);
                    _description.postValue(weatherResponse.weather.get(0).description);
                    _city.postValue(weatherResponse.name);
                    _icon.postValue("https://openweathermap.org/img/wn/" + weatherResponse.weather.get(0).icon + "@2x.png");
                    getForecastData(weatherResponse.coord.lat, weatherResponse.coord.lon);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponseCurrent> call, Throwable t) {
                Log.d("HomeViewModel", t.getMessage());
            }
        });
    }

    public void getForecastData(Double lat, Double lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherServiceForecast service = retrofit.create(WeatherServiceForecast.class);
        Call<WeatherResponseForecast> call = service.getForecastWeatherData(lat, lon, "minutely,hourly,alerts", "ru", "metric", "f955e39cc8ec50741acb39727ead90dd");
        call.enqueue(new Callback<WeatherResponseForecast>() {
            @Override
            public void onResponse(Call<WeatherResponseForecast> call, Response<WeatherResponseForecast> response) {
                if (response.code() == 200) {
                    WeatherResponseForecast weatherResponse = response.body();
                    assert weatherResponse != null;
                    Log.d("HomeViewModel", _daily.toString());
                    _daily.postValue(weatherResponse.daily);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponseForecast> call, Throwable t) {
                Log.d("HomeViewModel", t.getMessage());
            }
        });
    }

}

