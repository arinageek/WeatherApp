package com.example.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;
import com.example.weatherapp.openweathermap.current.WeatherServiceCurrent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences("notification_shared_pref",Context.MODE_PRIVATE);;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WeatherServiceCurrent service = retrofit.create(WeatherServiceCurrent.class);
            Call<WeatherResponseCurrent> call = service.getCurrentWeatherDataByName(sharedPref.getString("notification_city", ""), "ru", "metric", "f955e39cc8ec50741acb39727ead90dd");
            call.enqueue(new Callback<WeatherResponseCurrent>() {
                @Override
                public void onResponse(Call<WeatherResponseCurrent> call, Response<WeatherResponseCurrent> response) {
                    if (response.code() == 200) {
                        WeatherResponseCurrent weatherResponse = response.body();
                        assert weatherResponse != null;
                        NotificationHelper notificationHelper = new NotificationHelper(context,
                                StringUtil.formatDegrees(weatherResponse.main.temp), sharedPref.getString("notification_city", ""));
                        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                        notificationHelper.getManager().notify(1, nb.build());
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponseCurrent> call, Throwable t) {
                    Log.d("AlertReceiver", t.getMessage());
                }
            });

    }
}