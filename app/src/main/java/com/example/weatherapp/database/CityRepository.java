package com.example.weatherapp.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.database.entities.City;

import java.util.List;

public class CityRepository {
    private CityDao cityDao;
    private LiveData<List<City>> allCities;

    public CityRepository(Application application) {
        CityDatabase database = CityDatabase.getInstance(application);
        cityDao = database.cityDao();
        allCities = cityDao.getAllCities();
    }

    public void insert(City city) {
        new InsertCityAsyncTask(cityDao).execute(city);
    }

    public void update(City city) {
        new UpdateCityAsyncTask(cityDao).execute(city);
    }

    public void delete(City city) {
        new DeleteCityAsyncTask(cityDao).execute(city);
    }

    public void deleteAllCities() {
        new DeleteAllCitiesAsyncTask(cityDao).execute();
    }

    public LiveData<List<City>> getAllCities() {
        return allCities;
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

    private static class UpdateCityAsyncTask extends AsyncTask<City, Void, Void> {
        private CityDao cityDao;

        private UpdateCityAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            cityDao.update(cities[0]);
            return null;
        }
    }

    private static class DeleteCityAsyncTask extends AsyncTask<City, Void, Void> {
        private CityDao cityDao;

        private DeleteCityAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            cityDao.delete(cities[0]);
            return null;
        }
    }

    private static class DeleteAllCitiesAsyncTask extends AsyncTask<Void, Void, Void> {

        private CityDao cityDao;

        private DeleteAllCitiesAsyncTask(CityDao cityDao) {
            this.cityDao = cityDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cityDao.deleteAllCities();
            return null;
        }
    }
}
