package com.example.weatherapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.database.entities.City;
import com.example.weatherapp.databinding.FragmentSavedLocationsBinding;
import com.example.weatherapp.databinding.SavedLocationsItemBinding;
import com.example.weatherapp.openweathermap.current.WeatherResponseCurrent;
import com.example.weatherapp.openweathermap.current.WeatherServiceCurrent;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weatherapp.StringUtil.formatDegrees;

public class SavedLocationsAdapter extends ListAdapter<City, SavedLocationsAdapter.ViewHolder> {

    //private MutableLiveData<List<WeatherResponseCurrent>> list;

    public SavedLocationsAdapter() {
        super(DIFF_CALLBACK);
        //list = list_;
    }

    private static final DiffUtil.ItemCallback<City> DIFF_CALLBACK = new DiffUtil.ItemCallback<City>() {
        @Override
        public boolean areItemsTheSame(@NonNull City oldItem, @NonNull City newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull City oldItem, @NonNull City newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        SavedLocationsItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.saved_locations_item, viewGroup,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(!getCurrentList().isEmpty()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WeatherServiceCurrent service = retrofit.create(WeatherServiceCurrent.class);

            Call<WeatherResponseCurrent> call = service.getCurrentWeatherData(getItem(position).getName(), "ru", "metric", "f955e39cc8ec50741acb39727ead90dd");
            call.enqueue(new Callback<WeatherResponseCurrent>() {
                @Override
                public void onResponse(Call<WeatherResponseCurrent> call, Response<WeatherResponseCurrent> response) {
                    if (response.code() == 200) {
                        WeatherResponseCurrent weatherResponse = response.body();
                        assert weatherResponse != null;
                        holder.binding.degrees.setText(formatDegrees(weatherResponse.main.temp));
                        holder.binding.city.setText(weatherResponse.name);
                        Picasso.get().load("https://openweathermap.org/img/wn/"+weatherResponse.weather.get(0).icon+"@2x.png")
                                .into(holder.binding.icon);
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponseCurrent> call, Throwable t) {
                    Log.d("SavedLocationsAdapter", t.getMessage());
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SavedLocationsItemBinding binding;

        public ViewHolder(SavedLocationsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
