package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.ForecastAdapter;
import com.example.weatherapp.adapters.SavedLocationsAdapter;
import com.example.weatherapp.databinding.FragmentSavedLocationsBinding;
import com.example.weatherapp.viewmodels.HomeViewModel;
import com.example.weatherapp.viewmodels.SavedLocationsViewModel;

public class SavedLocationsFragment extends Fragment {

    private FragmentSavedLocationsBinding binding;
    private SavedLocationsViewModel savedViewModel;
    private SavedLocationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_locations, container, false);
        View view = binding.getRoot();

        //creating an instance of SavedLocationsViewModel and binding it
        savedViewModel = new ViewModelProvider(this).get(SavedLocationsViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(savedViewModel);

        //RecyclerView and Adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new SavedLocationsAdapter();
        binding.recyclerView.setAdapter(adapter);

        savedViewModel.getAllCities().observe(getViewLifecycleOwner(), cities -> adapter.submitList(cities));
        /*savedViewModel._weatherResponse.observe(getViewLifecycleOwner(), weatherList -> {
            adapter.submitList(weatherList);
        });*/

        return view;
    }
}
