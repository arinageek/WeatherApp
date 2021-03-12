package com.example.weatherapp.adapters;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.SavedLocationsItemBinding;
import com.example.weatherapp.openweathermap.group.List;
import com.squareup.picasso.Picasso;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.weatherapp.StringUtil.formatDegrees;

public class SavedLocationsAdapter extends ListAdapter<List, SavedLocationsAdapter.ViewHolder> {

    public SavedLocationsAdapter() {
        super(DIFF_CALLBACK);
    }

    public int getCityIdAt(int pos){return getItem(pos).id;}

    private static final DiffUtil.ItemCallback<List> DIFF_CALLBACK = new DiffUtil.ItemCallback<List>() {
        @Override
        public boolean areItemsTheSame(@NonNull List oldItem, @NonNull List newItem) {
            return oldItem.name.equals(newItem.name);
        }

        @Override
        public boolean areContentsTheSame(@NonNull List oldItem, @NonNull List newItem) {
            return oldItem.name.equals(newItem.name);
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        SavedLocationsItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.saved_locations_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.binding.mapsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + getItem(position).name));
            startActivity(holder.binding.description.getContext(), intent, null);
        });
        holder.binding.expandableLayout.setVisibility(getItem(position).isExpanded ? View.VISIBLE: View.GONE);
        holder.binding.description.setText(getItem(position).weather.get(0).description);
        holder.binding.temp.setText("Темп: " + formatDegrees(getItem(position).main.temp));
        holder.binding.tempMin.setText("Мин. темп: " + formatDegrees(getItem(position).main.tempMin));
        holder.binding.tempMax.setText("Макс. темп: " + formatDegrees(getItem(position).main.tempMax));
        holder.binding.pressure.setText("Давление: " + getItem(position).main.pressure);
        holder.binding.humidity.setText("Влажность: " + getItem(position).main.humidity);
        holder.binding.wind.setText("Скорость ветра: " + getItem(position).wind.speed.toString());
        holder.binding.degrees.setText(formatDegrees(getItem(position).main.temp));
        holder.binding.city.setText(getItem(position).name);
        Picasso.get().load("https://openweathermap.org/img/wn/" + getItem(position).weather.get(0).icon + "@2x.png")
                .into(holder.binding.icon);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SavedLocationsItemBinding binding;

        public ViewHolder(SavedLocationsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.container.setOnClickListener(v -> {
                getItem(getAdapterPosition()).isExpanded = !getItem(getAdapterPosition()).isExpanded;
                notifyItemChanged(getAdapterPosition());
            });
        }

    }

}
