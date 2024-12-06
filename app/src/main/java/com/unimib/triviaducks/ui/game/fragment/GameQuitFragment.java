package com.unimib.triviaducks.ui.game.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimib.triviaducks.R;

public class GameQuitFragment extends Fragment {

    Button close;

    public GameQuitFragment() {
        // Required empty public constructor
    }

    public static GameQuitFragment newInstance() {
        GameQuitFragment fragment = new GameQuitFragment();
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
        return inflater.inflate(R.layout.fragment_game_quit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        close = view.findViewById(R.id.close);

        close.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_gameQuitFragment_to_mainActivity)
        );



    }
}