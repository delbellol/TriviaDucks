package com.unimib.triviaducks.ui.game.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.MainActivity;


public class GameQuitFragment extends DialogFragment {

    Button close;
    Button cancel;

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

        close.setOnClickListener(v -> {
            //TODO Sistemare questo che va nel fragment sbagliato
                Navigation.findNavController(view).navigate(R.id.action_gameQuitFragment_to_mainActivity);
                }
        );

        cancel = view.findViewById(R.id.cancel);

        cancel.setOnClickListener(v ->
                this.dismiss()
        );



    }
}