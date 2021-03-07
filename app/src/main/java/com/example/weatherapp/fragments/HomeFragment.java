package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private static final int SEARCH_MODE_ENABLED = 1;
    private static final int SEARCH_MODE_DISABLED = 0;
    private static HomeViewModel homeViewModel;

    private int mode;
    private Calendar calendar;
    private String currentDate;
    private ForecastAdapter adapter;
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        //Setting current time
        calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        binding.textDateDisplay.setText(currentDate);

        //creating an instance of HomeViewModel and binding it
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(homeViewModel);

        //Initial data shown on home screen
        homeViewModel.getCurrentData("Moscow");

        //RecyclerView and Adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //adapter = new ForecastAdapter(homeViewModel._daily);
        adapter = new ForecastAdapter();
        binding.recyclerView.setAdapter(adapter);
        homeViewModel._daily.observe(getViewLifecycleOwner(), dailyList -> {
            adapter.submitList(dailyList);
        });

        //OnClickListeners
        binding.toolbar.searchButton.setOnClickListener(v -> enableSearchMode());
        binding.toolbar.checkButton.setOnClickListener(v -> {
            binding.star.setImageResource(R.drawable.ic_baseline_star_border_24);
            if(binding.toolbar.etSearchField.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter a valid city", Toast.LENGTH_SHORT).show();
            }else{
                homeViewModel.getCurrentData(binding.toolbar.etSearchField.getText().toString().trim());
            }
            disableSearchMode();
        });
        binding.star.setOnClickListener(v -> {
            if(binding.textCityName.getText().toString().trim() != ""){
                homeViewModel.insert(binding.textCityName.getText().toString().trim());
                binding.star.setImageResource(R.drawable.ic_baseline_filled_star_24);
            }
        });

        return view;
    }

    private void enableSearchMode(){
        binding.toolbar.toolbarSearchButton.setVisibility(View.GONE);
        binding.toolbar.toolbarSearchField.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarCheckButton.setVisibility(View.VISIBLE);
        mode = SEARCH_MODE_ENABLED;
    }
    private void disableSearchMode(){
        binding.toolbar.toolbarSearchField.setVisibility(View.INVISIBLE);
        binding.toolbar.toolbarCheckButton.setVisibility(View.GONE);
        binding.toolbar.toolbarSearchButton.setVisibility(View.VISIBLE);
        mode = SEARCH_MODE_DISABLED;
    }

}
