package com.example.weatherapp.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.weatherapp.fragments.NotificationFragment;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener   {


    private OnSelectedListener onSelectedListener;
    public interface OnSelectedListener { void onSelected(int hourOfDay, int minute); }
    public void setOnSelectedListener(OnSelectedListener listener){ onSelectedListener = listener; }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(onSelectedListener!=null) onSelectedListener.onSelected(hourOfDay, minute);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelectedListener = null;
    }
}