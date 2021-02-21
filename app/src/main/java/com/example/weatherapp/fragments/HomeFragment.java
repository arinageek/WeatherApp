package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.R;
import com.example.weatherapp.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {

    private static final int SEARCH_MODE_ENABLED = 1;
    private static final int SEARCH_MODE_DISABLED = 0;
    private static HomeViewModel homeViewModel;

    private int mode;
    private RelativeLayout mCheckContainer, mSearchContainer, mSearchFieldContainer;
    private ImageView searchBtn, checkBtn;
    private EditText et_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mCheckContainer = view.findViewById(R.id.toolbar_check_button);
        mSearchContainer = view.findViewById(R.id.toolbar_search_button);
        mSearchFieldContainer = view.findViewById(R.id.toolbar_search_field);
        searchBtn = view.findViewById(R.id.search_button);
        checkBtn = view.findViewById(R.id.check_button);
        et_search = view.findViewById(R.id.et_search_field);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        searchBtn.setOnClickListener(v -> enableSearchMode());
        checkBtn.setOnClickListener(v -> {
            if(et_search.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter a valid city", Toast.LENGTH_SHORT).show();
            }else{
                homeViewModel.getCurrentData(et_search.getText().toString().trim());
            }
            disableSearchMode();
        });

        return view;
    }

    private void enableSearchMode(){
        mSearchContainer.setVisibility(View.GONE);
        mSearchFieldContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.VISIBLE);
        mode = SEARCH_MODE_ENABLED;
    }
    private void disableSearchMode(){
        mSearchFieldContainer.setVisibility(View.INVISIBLE);
        mCheckContainer.setVisibility(View.GONE);
        mSearchContainer.setVisibility(View.VISIBLE);
        mode = SEARCH_MODE_DISABLED;
    }
}
