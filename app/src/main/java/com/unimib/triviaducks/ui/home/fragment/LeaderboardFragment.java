package com.unimib.triviaducks.ui.home.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.adapter.RankRecyclerAdapter;
import com.unimib.triviaducks.model.Rank;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RankRecyclerAdapter adapter;
    private List<Rank> rankList;

    public LeaderboardFragment() {
    }

    public static LeaderboardFragment newInstance() {
        LeaderboardFragment fragment = new LeaderboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

            recyclerView = view.findViewById(R.id.rankList);

            rankList = new ArrayList<>();
            rankList.add(new Rank(1, R.drawable.p1, "Account 1", 1000));
            rankList.add(new Rank(2, R.drawable.p2, "Account 2", 950));
            rankList.add(new Rank(3, R.drawable.p3, "Account 3", 900));
            rankList.add(new Rank(4, R.drawable.p4, "Account 4", 850));
            rankList.add(new Rank(5, R.drawable.p5, "Account 5", 800));
            rankList.add(new Rank(6, R.drawable.p6, "Account 6", 750));
            rankList.add(new Rank(7, R.drawable.p1, "Account 7", 700));
            rankList.add(new Rank(8, R.drawable.p2, "Account 8", 650));
            rankList.add(new Rank(9, R.drawable.p3, "Account 9", 600));
            rankList.add(new Rank(10, R.drawable.p4, "Account 10", 550));

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new RankRecyclerAdapter(rankList);
            recyclerView.setAdapter(adapter);

            return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}