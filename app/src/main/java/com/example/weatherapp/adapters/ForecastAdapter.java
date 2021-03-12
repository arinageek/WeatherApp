package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ForecastItemBinding;
import com.example.weatherapp.openweathermap.forecast.Daily;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.weatherapp.StringUtil.formatDegrees;

public class ForecastAdapter extends ListAdapter<Daily, ForecastAdapter.ViewHolder> {

    public ForecastAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Daily> DIFF_CALLBACK = new DiffUtil.ItemCallback<Daily>() {
        @Override
        public boolean areItemsTheSame(@NonNull Daily oldItem, @NonNull Daily newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Daily oldItem, @NonNull Daily newItem) {
            return false;
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ForecastItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.forecast_item, viewGroup,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(!getCurrentList().isEmpty()){
            SimpleDateFormat sd = new SimpleDateFormat("dd-MMM");
            String date = sd.format(new Date((long)getItem(position).dt * 1000));
            holder.binding.day.setText(date);
            holder.binding.degrees.setText(formatDegrees(getItem(position).temp.day));
            Picasso.get().load("https://openweathermap.org/img/wn/"+getItem(position).weather.get(0).icon+"@2x.png")
                    .into(holder.binding.icon);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ForecastItemBinding binding;

        public ViewHolder(ForecastItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
