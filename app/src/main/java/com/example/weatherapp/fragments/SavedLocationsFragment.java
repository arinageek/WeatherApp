package com.example.weatherapp.fragments;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.SavedLocationsAdapter;
import com.example.weatherapp.databinding.FragmentSavedLocationsBinding;
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

        //ViewModel Observers
        savedViewModel.getAllCities().observe(getViewLifecycleOwner(), cities -> {
            if(isConnected()){
                savedViewModel.getCitiesWeather(cities);
            }else{
                disconnected();
            }
        });
        savedViewModel.getWeather().observe(getViewLifecycleOwner(), weather -> { adapter.submitList(weather.list);});

        //OnClickListeners
        binding.retryBtn.setOnClickListener(v -> {
            if (isConnected()) connected();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Вы уверены, что хотите удалить этот город из списка?")
                        .setPositiveButton("Удалить", (dialog, which) -> {
                            savedViewModel.delete(adapter.getCityIdAt(viewHolder.getAdapterPosition()));
                        })
                        .setNegativeButton("Отмена",
                                (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .create().show();

            }
        }).attachToRecyclerView(binding.recyclerView);

        return view;
    }

    private boolean isConnected() {
        //Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void connected() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.layoutNoConnection.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SavedLocationsFragment()).commit();
    }

    private void disconnected() {
        binding.layoutNoConnection.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

}
