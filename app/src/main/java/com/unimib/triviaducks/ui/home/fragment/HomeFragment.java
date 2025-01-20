package com.unimib.triviaducks.ui.home.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.unimib.triviaducks.adapter.CategoriesRecyclerAdapter;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private int selectedCategory;

    public HomeFragment() {
    }

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

        // Inizializza il TextView
        TextView descriptionTextView = view.findViewById(R.id.categoryDescription);

        // Imposta una descrizione iniziale
        descriptionTextView.setText("Descrizione iniziale della categoria");

        List<Integer> lottieAnimations = Arrays.asList(
                R.raw.category_all,
                R.raw.category_science,
                R.raw.category_geography,
                R.raw.category_history,
                R.raw.category_sport
        );

        List<String> categoryDescriptions = Arrays.asList(
                "All category",
                "Science",
                "Geography",
                "History",
                "Sport"
        );

        ViewPager2 viewPager = view.findViewById(R.id.categoryViewPager);
        TabLayout tabLayout = view.findViewById(R.id.categoryTabLayout);
        TextView categoryDescription = view.findViewById(R.id.categoryDescription);

        CategoriesRecyclerAdapter adapter = new CategoriesRecyclerAdapter(lottieAnimations, categoryDescriptions);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
        }).attach();

        // Imposta il testo iniziale
        categoryDescription.setSelected(true); // Necessario per il marquee (scorrimento del testo)
        categoryDescription.setText(categoryDescriptions.get(0));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                selectedCategory = getCategoryFromPosition(position);
                SharedPreferencesUtils.setCategory(getCategoryFromPosition(position));
                // Aggiorna il testo in base alla categoria selezionata
                categoryDescription.setText(categoryDescriptions.get(position));
            }
        });

        Button oneShot = view.findViewById(R.id.oneShot);
        oneShot.setOnClickListener(v -> {
            GameModeFragment gameModeDialog = GameModeFragment.newInstance(selectedCategory);
            gameModeDialog.show(getParentFragmentManager(), "gameModeDialog");
        });

        Button trials = view.findViewById(R.id.trials);
        trials.setOnClickListener(v -> {
            GameModeFragment gameModeDialog = GameModeFragment.newInstance(selectedCategory);
            gameModeDialog.show(getParentFragmentManager(), "gameModeDialog");
        });
    }

    private int getCategoryFromPosition(int position) {
        switch (position) {
            default:
                return Constants.ANY_CATEGORIES_CODE;
            case 1:
                return Constants.SCIENCE_NATURE_CODE;
            case 2:
                return Constants.GEOGRAPHY_CODE;
            case 3:
                return Constants.HISTORY_CODE;
            case 4:
                return Constants.SPORTS_CODE;
        }
    }
}