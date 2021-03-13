package com.example.weatherapp.fragments;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.AlertReceiver;
import com.example.weatherapp.R;
import com.example.weatherapp.databinding.FragmentNotificationBinding;
import com.example.weatherapp.dialogs.ChooseCityDialog;
import com.example.weatherapp.dialogs.TimePickerFragment;
import com.example.weatherapp.viewmodels.NotificationViewModel;

import java.text.DateFormat;
import java.util.Calendar;

public class NotificationFragment extends Fragment{

    private FragmentNotificationBinding binding;
    private NotificationViewModel viewModel;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        View view = binding.getRoot();

        //creating an instance of NotificationViewModel and binding it
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setFragment(this);

        //Initializing shared preferences
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        binding.city.setText("Выбранный город: "+sharedPref.getString("notification_city", ""));
        binding.time.setText("Время оповещения: "+sharedPref.getString("notification_time", ""));

        //OnClickListeners
        binding.btnChooseCity.setOnClickListener(v ->{
            viewModel.getAllCities().observe(getViewLifecycleOwner(), cities ->{
                CharSequence[] cityNames = new CharSequence[cities.size()];
                for(int i=0; i<cities.size(); i++){
                    cityNames[i] = cities.get(i).getName();
                }
                ChooseCityDialog dialog = new ChooseCityDialog(cityNames);
                dialog.show(getChildFragmentManager(), "choose city dialog");
                dialog.setOnSelectedListener(city -> {
                    binding.city.setText("Выбранный город: "+cityNames[city].toString());
                    editor.putString("notification_city", cityNames[city].toString());
                    editor.apply();
                });
            });
        });

        binding.btnChooseTime.setOnClickListener(v ->{
            TimePickerFragment timePicker = new TimePickerFragment();
            timePicker.show(getActivity().getSupportFragmentManager(), "time picker");
            timePicker.setOnSelectedListener(((hourOfDay, minute) -> {
                onTimeSet(hourOfDay, minute);
            }));
        });

        binding.btnRemoveAlarm.setOnClickListener(v ->{
            cancelAlarm();
        });
        return view;
    }

    public void onTimeSet(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }
    private void updateTimeText(Calendar c) {
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        binding.time.setText("Время оповещения: "+timeText);
        editor.putString("notification_time", timeText);
        editor.apply();
    }
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        binding.time.setText("Alarm canceled");
    }
}
