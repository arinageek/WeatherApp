package com.example.weatherapp.viewmodels;

import android.app.Application;
import android.content.pm.LauncherApps;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.openweathermap.WeatherResponse;
import com.example.weatherapp.openweathermap.WeatherService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeViewModel extends AndroidViewModel {

    private CityRepository repository;
    private LiveData<List<City>> allCities;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new CityRepository(application);
        allCities = repository.getAllCities();
    }

    public void insert(City city){
        repository.insert(city);
    }
    public void update(City city){
        repository.update(city);
    }
    public void delete(City city){
        repository.delete(city);
    }
    public LiveData<List<City>> getAllCities() {
        return allCities;
    }
    public void deleteAllCities(){ repository.deleteAllCities();}

    public void getCurrentData(String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(city, "metric", "f955e39cc8ec50741acb39727ead90dd");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure;

                    Log.d("HomeViewModel", stringBuilder);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.d("HomeViewModel", t.getMessage());
            }
        });
    }

}