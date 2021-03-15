package com.example.weatherapp;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;
import com.example.weatherapp.openweathermap.current.WeatherServiceCurrent;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "1";
    public static final String channelName = "Channel 1";

    private NotificationManager mManager;
    private String degrees;
    private String city;

    public NotificationHelper(Context base, String degrees, String city) {
        super(base);
        this.degrees = degrees;
        this.city = city;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }


    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Погода в "+city)
                .setContentText("Температура: "+degrees)
                .setSmallIcon(R.drawable.ic_baseline_cloud_24);
    }
}
