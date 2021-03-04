package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ForecastItemBinding;
import com.example.weatherapp.openweathermap.forecast.Daily;
import com.example.weatherapp.openweathermap.forecast.WeatherResponseForecast;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.weatherapp.StringUtil.formatDegrees;

public class ForecastAdapter extends ListAdapter<Daily, ForecastAdapter.ViewHolder> {

    private MutableLiveData<List<Daily>> list;

    public ForecastAdapter(MutableLiveData<List<Daily>> list_) {
        super(DIFF_CALLBACK);
        list = list_;
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
        if(!list.getValue().isEmpty()){
            holder.binding.degrees.setText(formatDegrees(list.getValue().get(position).temp.day));
            holder.binding.day.setText("Чт, 22"); // for now
            Picasso.get().load("https://openweathermap.org/img/wn/"+list.getValue().get(position).weather.get(0).icon+"@2x.png")
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
