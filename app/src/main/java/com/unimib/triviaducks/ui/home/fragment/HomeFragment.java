package com.unimib.triviaducks.ui.home.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Intent;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.MainActivity;

public class HomeFragment extends Fragment {
    private Button oneShot;
    private Button trials;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        oneShot = view.findViewById(R.id.oneShot);
        oneShot.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_gameModeFragment);
        });

        trials = view.findViewById(R.id.trials);
        trials.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_gameModeFragment);
        });
    }
}