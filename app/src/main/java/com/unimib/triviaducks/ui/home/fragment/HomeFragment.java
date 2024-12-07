package com.unimib.triviaducks.ui.home.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.repository.IQuestionRepository;
import com.unimib.triviaducks.repository.QuestionMockRepository;
import com.unimib.triviaducks.repository.QuestionRepositoryAPI;
import com.unimib.triviaducks.ui.home.MainActivity;
import com.unimib.triviaducks.util.ResponseCallback;

import java.util.List;

public class HomeFragment extends Fragment implements ResponseCallback {
    private Button oneShot;
    private Button trials;
    // private RecyclerView recyclerView;
    private CircularProgressIndicator circularProgressIndicator;
    private IQuestionRepository questionRepository;
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

        /*if(requireActivity().getResources().getBoolean(R.bool.debug_mode)){
            questionRepository = new QuestionMockRepository();
        } else{
            questionRepository = new QuestionRepositoryAPI();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //questionRepository.fetchQuestion();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        circularProgressIndicator = view.findViewById(R.id.progressIndicator);
        // Inflate the layout for this fragment
        return view;
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

    @Override
    public void onSuccess(List<Question> questionList, long lastUpdate) {

    }

    @Override
    public void onFailure(String errorMessage) {

    }
}