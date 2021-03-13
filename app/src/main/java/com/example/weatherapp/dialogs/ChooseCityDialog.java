package com.example.weatherapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.weatherapp.R;
import com.example.weatherapp.database.CityRepository;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.fragments.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

public class ChooseCityDialog extends DialogFragment {

    private int choice=0;
    private CharSequence[] cities;

    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener { void onCitySelected(int position); }
    public void setOnSelectedListener(OnSelectedListener listener){ onSelectedListener = listener; }

    public ChooseCityDialog(CharSequence[] cities){
        this.cities = cities;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Выберите город")
                .setSingleChoiceItems(cities, 0, (dialog, which) -> choice = which)
                .setNegativeButton("Отмена", (dialog, which) -> {

                })
                .setPositiveButton("Выбрать", (dialog, which) -> {
                    if(onSelectedListener != null) onSelectedListener.onCitySelected(choice);
                });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelectedListener = null;
    }
}
