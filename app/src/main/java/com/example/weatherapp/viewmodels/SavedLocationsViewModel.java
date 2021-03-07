package com.example.weatherapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.fragments.SavedLocationsFragment;
import com.example.weatherapp.openweathermap.current.Weather;
import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;
import com.example.weatherapp.openweathermap.current.WeatherServiceCurrent;
import com.example.weatherapp.openweathermap.forecast.Daily;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SavedLocationsViewModel extends AndroidViewModel {
    private CityRepository repository;
    private LiveData<List<City>> allCities;
    public MutableLiveData<List<WeatherResponseCurrent>> _weatherResponse = new MutableLiveData<>();

    public SavedLocationsViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
    }

    public LiveData<List<City>> getAllCities() {
        return allCities;
    }

    /*
    public void getCitiesWithData(List<City> cities) {

        List<WeatherResponseCurrent> list = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherServiceCurrent service = retrofit.create(WeatherServiceCurrent.class);

        for(int i=0; i<cities.size(); i++) {

            Call<WeatherResponseCurrent> call = service.getCurrentWeatherData(allCities.getValue().get(i).getName(), "ru", "metric", "f955e39cc8ec50741acb39727ead90dd");
            call.enqueue(new Callback<WeatherResponseCurrent>() {
                @Override
                public void onResponse(Call<WeatherResponseCurrent> call, Response<WeatherResponseCurrent> response) {
                    if (response.code() == 200) {
                        WeatherResponseCurrent weatherResponse = response.body();
                        assert weatherResponse != null;
                        list.add(weatherResponse);

                        _weatherResponse.postValue(list);
                        Log.d("SavedLocationsViewModel", "Done!!!");
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponseCurrent> call, Throwable t) {
                    Log.d("SavedLocationsViewModel", t.getMessage());
                }
            });
        }

    }
    */
}
