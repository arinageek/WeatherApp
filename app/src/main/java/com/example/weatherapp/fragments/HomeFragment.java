package com.example.weatherapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.ForecastAdapter;
import com.example.weatherapp.databinding.FragmentHomeBinding;
import com.example.weatherapp.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {

    private static final int SEARCH_MODE_ENABLED = 1;
    private static final int SEARCH_MODE_DISABLED = 0;
    private static HomeViewModel homeViewModel;
    private int mode;
    private ForecastAdapter adapter;
    private FragmentHomeBinding binding;
    private int cityId;
    private boolean isStarred = false;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        //creating an instance of HomeViewModel and binding it
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(homeViewModel);

        //Initializing shared preferences to retrieve the latest searched city
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //If the user hasn't searched anything yet
        if (sharedPref.getString("last_city", "") == "") {
            editor.putString("last_city", "Москва");
            editor.apply();
        }

        if(isConnected()){
            homeViewModel.getCurrentData(sharedPref.getString("last_city", ""));
        }else{
            disconnected();
        }

        //RecyclerView and Adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ForecastAdapter();
        binding.recyclerView.setAdapter(adapter);

        //ViewModel Observers
        homeViewModel._daily.observe(getViewLifecycleOwner(), dailyList -> { adapter.submitList(dailyList); });
        homeViewModel._cityId.observe(getViewLifecycleOwner(), id -> { cityId = id; });

        //OnClickListeners
        binding.retryBtn.setOnClickListener(v -> { if (isConnected()) connected(); });

        binding.toolbar.searchButton.setOnClickListener(v -> enableSearchMode());

        binding.toolbar.checkButton.setOnClickListener(v -> {
            binding.star.setImageResource(R.drawable.ic_baseline_star_border_24);
            if (binding.toolbar.etSearchField.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Поле не может быть пустым", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("last_city", binding.toolbar.etSearchField.getText().toString().trim());
                editor.apply();
                if (isConnected()) {
                    homeViewModel.getCurrentData(binding.toolbar.etSearchField.getText().toString().trim());
                } else {
                    disconnected();
                }
            }
            disableSearchMode();
        });

        binding.star.setOnClickListener(v -> {
            if (!isStarred) {
                homeViewModel.insert(binding.textCityName.getText().toString().trim(), cityId);
                binding.star.setImageResource(R.drawable.ic_baseline_filled_star_24);
                Toast.makeText(getActivity(), "Город добавлен в сохраненные", Toast.LENGTH_LONG).show();
            } else {
                homeViewModel.delete(cityId);
                binding.star.setImageResource(R.drawable.ic_baseline_star_border_24);
                Toast.makeText(getActivity(), "Город удален из сохраненных", Toast.LENGTH_LONG).show();
            }
            isStarred = !isStarred;
        });

        return view;
    }


    private void enableSearchMode() {
        binding.toolbar.toolbarSearchButton.setVisibility(View.GONE);
        binding.toolbar.toolbarSearchField.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarCheckButton.setVisibility(View.VISIBLE);
        mode = SEARCH_MODE_ENABLED;
    }

    private void disableSearchMode() {
        binding.toolbar.toolbarSearchField.setVisibility(View.INVISIBLE);
        binding.toolbar.toolbarCheckButton.setVisibility(View.GONE);
        binding.toolbar.toolbarSearchButton.setVisibility(View.VISIBLE);
        mode = SEARCH_MODE_DISABLED;
    }

    private boolean isConnected() {
        //Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void connected() {
        binding.cardView.setVisibility(View.VISIBLE);
        binding.layoutNoConnection.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private void disconnected() {
        binding.layoutNoConnection.setVisibility(View.VISIBLE);
        binding.cardView.setVisibility(View.GONE);
    }

}
