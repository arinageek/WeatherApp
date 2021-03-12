package com.example.weatherapp.database;


import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;
import com.example.weatherapp.openweathermap.current.WeatherServiceCurrent;
import com.example.weatherapp.openweathermap.forecast.Daily;
import com.example.weatherapp.openweathermap.forecast.WeatherResponseForecast;
import com.example.weatherapp.openweathermap.forecast.WeatherServiceForecast;
import com.example.weatherapp.openweathermap.group.WeatherResponseGroup;
import com.example.weatherapp.openweathermap.group.WeatherServiceGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityRepository {
    private CityDao cityDao;
    private LiveData<List<City>> allCities;
    private MutableLiveData<WeatherResponseGroup> citiesWeather = new MutableLiveData<>();
    private MutableLiveData<Double> _degrees = new MutableLiveData<>();
    private MutableLiveData<String> _date = new MutableLiveData<>();
    private MutableLiveData<String> _description = new MutableLiveData<>("");
    private MutableLiveData<String> _city = new MutableLiveData<>("");
    private MutableLiveData<Integer> _cityId = new MutableLiveData<>();
    private MutableLiveData<String> _icon = new MutableLiveData<>("http://openweathermap.org/img/wn/10d@2x.png");
    private MutableLiveData<List<Daily>> _daily = new MutableLiveData<>();

    public CityRepository(Application application) {
        CityDatabase database = CityDatabase.getInstance(application);
        cityDao = database.cityDao();
        allCities = cityDao.getAllCities();
    }

    public void insert(City city) {
        new InsertCityAsyncTask(cityDao).execute(city);
    }

    public void delete(int id) {
        new DeleteCityAsyncTask(cityDao).execute(id);
    }

    public LiveData<List<City>> getAllCities() {
        return allCities;
    }

    public MutableLiveData<WeatherResponseGroup> getCitiesWeather() {return citiesWeather;}

    public MutableLiveData<Double> get_degrees() {
        return _degrees;
    }

    public MutableLiveData<String> get_date() {
        return _date;
    }

    public MutableLiveData<String> get_description() {
        return _description;
    }

    public MutableLiveData<String> get_city() {
        return _city;
    }

    public MutableLiveData<Integer> get_cityId() {
        return _cityId;
    }

    public MutableLiveData<String> get_icon() {
        return _icon;
    }

    public MutableLiveData<List<Daily>> get_daily() {
        return _daily;
    }

    //SavedLocationsViewModel
    public void getCitiesWeather(List<City> cities){
        String locations="";
        for(int i=0; i<cities.size(); i++){
            locations+=cities.get(i).getCityId();
            if(i!=cities.size()-1) locations+=",";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherServiceGroup service = retrofit.create(WeatherServiceGroup.class);

        Call<WeatherResponseGroup> call = service.getGroupWeatherData(locations, "ru", "metric", "f955e39cc8ec50741acb39727ead90dd");
        call.enqueue(new Callback<WeatherResponseGroup>() {
            @Override
            public void onResponse(Call<WeatherResponseGroup> call, Response<WeatherResponseGroup> response) {
                if (response.code() == 200) {
                    WeatherResponseGroup weatherResponse = response.body();
                    assert weatherResponse != null;
                    citiesWeather.postValue(weatherResponse);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponseGroup> call, Throwable t) {
                Log.d("SavedLocationsAdapter", t.getMessage());
            }
        });
    }

    //HomeViewModel
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
                    SimpleDateFormat sd = new SimpleDateFormat("dd MMMM yyyy");
                    _date.postValue(sd.format(new Date((long)weatherResponse.dt * 1000)));
                    _description.postValue(weatherResponse.weather.get(0).description);
                    _city.postValue(weatherResponse.name);
                    _cityId.postValue(weatherResponse.id);
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


    //Async functions
    private static class InsertCityAsyncTask extends AsyncTask<City, Void, Void> {
        private CityDao cityDao;

        private InsertCityAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            cityDao.insert(cities[0]);
            return null;
        }
    }

    private static class DeleteCityAsyncTask extends AsyncTask<Integer, Void, Void> {
        private CityDao cityDao;

        private DeleteCityAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            cityDao.delete(ids[0]);
            return null;
        }
    }

}
