package com.unimib.triviaducks.ui.home.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.adapter.RankRecyclerAdapter;
import com.unimib.triviaducks.model.Rank;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.NetworkUtil;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LeaderboardFragment extends Fragment {
    private static final String TAG = LeaderboardFragment.class.getSimpleName();
    private SharedPreferencesUtils sharedPreferencesUtils;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private RankRecyclerAdapter adapter;
    Set<String> leaderboardSet;

    public LeaderboardFragment() {}

    public static LeaderboardFragment newInstance() { return new LeaderboardFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetworkUtil.isInternetAvailable(getContext())) {
            NavHostFragment.findNavController(this).navigate(R.id.action_leaderboardFragment_to_connectionErrorActivity);
        }

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
        userViewModel.getLeaderboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        recyclerView = view.findViewById(R.id.rankList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userViewModel.getLeaderboard().observe(getViewLifecycleOwner(), item -> {

            leaderboardSet = sharedPreferencesUtils.readStringSetData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_LEADERBOARD);

            adapter = new RankRecyclerAdapter(getContext(), leaderboardSet);
            recyclerView.setAdapter(adapter);
        });
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null) {
                    if (getActivity() != null) {
                        getActivity().moveTaskToBack(true);
                    }
                }
            }
        });
    }
}