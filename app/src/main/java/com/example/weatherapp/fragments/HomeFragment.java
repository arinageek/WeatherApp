package com.example.weatherapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.ForecastAdapter;
import com.example.weatherapp.databinding.FragmentHomeBinding;
import com.example.weatherapp.viewmodels.HomeViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeFragment extends Fragment {

    private static final int SEARCH_MODE_ENABLED = 1;
    private static final int SEARCH_MODE_DISABLED = 0;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private static HomeViewModel homeViewModel;
    private static FusedLocationProviderClient fusedLocationClient;
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        requestLocation();

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

        if (isConnected()) {
            homeViewModel.getCurrentDataByName(sharedPref.getString("last_city", ""));
        } else {
            disconnected();
        }

        //RecyclerView and Adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ForecastAdapter();
        binding.recyclerView.setAdapter(adapter);

        //ViewModel Observers
        homeViewModel._daily.observe(getViewLifecycleOwner(), dailyList -> {
            adapter.submitList(dailyList);
        });
        homeViewModel._cityId.observe(getViewLifecycleOwner(), id -> {
            cityId = id;
        });

        //OnClickListeners
        binding.retryBtn.setOnClickListener(v -> {
            if (isConnected()) connected();
        });

        binding.toolbar.searchButton.setOnClickListener(v -> enableSearchMode());

        binding.toolbar.checkButton.setOnClickListener(v -> {
            binding.star.setImageResource(R.drawable.ic_baseline_star_border_24);
            if (binding.toolbar.etSearchField.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Поле не может быть пустым", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("last_city", binding.toolbar.etSearchField.getText().toString().trim());
                editor.apply();
                if (isConnected()) {
                    homeViewModel.getCurrentDataByName(binding.toolbar.etSearchField.getText().toString().trim());
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

        binding.fab.setOnClickListener(v -> {
            if (isConnected()) {
                getCurrentLocation();
            } else {
                disconnected();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        requestLocation();
    }

    public void requestLocation(){
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
                getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Разрешение не предоставлено!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                homeViewModel.getCurrentDataByLocation(lat, lon);
            }else{
                Toast.makeText(getActivity(), "Что-то пошло не так. Проверьте, включен ли GPS!", Toast.LENGTH_SHORT).show();
            }

        });
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
