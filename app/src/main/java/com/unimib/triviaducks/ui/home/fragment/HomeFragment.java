package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.LIST_CATEGORY;
import static com.unimib.triviaducks.util.Constants.LIST_LOTTIE_ANIMATIONS;
import static com.unimib.triviaducks.util.Constants.TRIVIA_CATEGORY_PARAMETER;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.unimib.triviaducks.adapter.CategoriesRecyclerAdapter;
import com.unimib.triviaducks.util.NetworkUtil;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private int selectedCategory;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.categoryViewPager);
        TabLayout tabLayout = view.findViewById(R.id.categoryTabLayout);

        CategoriesRecyclerAdapter adapter = new CategoriesRecyclerAdapter(LIST_LOTTIE_ANIMATIONS, LIST_CATEGORY);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectedCategory = getCategoryFromPosition(position);
            }
        });

        Button play = view.findViewById(R.id.play);
        play.setOnClickListener(v -> {
            if (!NetworkUtil.isInternetAvailable(getContext())) {
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_connectionErrorActivity);
            }
            else{
                GameModeDialog gameModeDialog = new GameModeDialog();
                Bundle args = new Bundle();
                args.putInt(TRIVIA_CATEGORY_PARAMETER, selectedCategory);
                gameModeDialog.setArguments(args);
                gameModeDialog.show(getParentFragmentManager(), GameModeDialog.class.getSimpleName());
            }
        });
    }

    private int getCategoryFromPosition(int position) {
        switch (position) {
            case 1:
                return Constants.SCIENCE_NATURE_CODE;
            case 2:
                return Constants.GEOGRAPHY_CODE;
            case 3:
                return Constants.HISTORY_CODE;
            case 4:
                return Constants.SPORTS_CODE;
            default:
                return Constants.ANY_CATEGORIES_CODE;
        }
    }
}