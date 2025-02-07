package com.unimib.triviaducks.ui.home.fragment;

// Import delle costanti per l'uso nelle SharedPreferences

import static com.unimib.triviaducks.util.Constants.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.model.Question;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.home.HomeActivity;
import com.unimib.triviaducks.ui.welcome.fragment.PickUsernameFragment;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.NetworkUtil;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class AccountInformationFragment extends Fragment {
    private static final String TAG = AccountInformationFragment.class.getSimpleName(); // Tag per i log

    // Dichiarazioni delle variabili UI e logiche
    private Button editProfileButton;
    private ImageView profileImageView, profilePicture; // Visualizzazione immagine profilo
    private TextView usernameTextView; // Visualizzazione nome utente
    private TextView bestScoreTextView;
    private UserViewModel userViewModel; // ViewModel per gestire i dati utente
    private SharedPreferencesUtils sharedPreferencesUtils; // Utility per SharedPreferences
    private CircularProgressIndicator circularProgressIndicator;
    private LottieAnimationView first_place, second_place, third_place;
    private ConstraintLayout profileLayout;

    public AccountInformationFragment() {}

    public static AccountInformationFragment newInstance() {
        return new AccountInformationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!NetworkUtil.isInternetAvailable(getContext())) {
            NavHostFragment.findNavController(this).navigate(R.id.action_accountInformationFragment_to_connectionErrorActivity);
        }

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);// Inizializzazione base del fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_information, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileLayout = view.findViewById(R.id.profileLayout);
        circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);

        // Inizializzazione delle utility
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        // Collegamento della TextView per il nome utente
        usernameTextView = view.findViewById(R.id.username);

        // Collegamento dell'ImageView per il profilo
        profileImageView = view.findViewById(R.id.profilePicture);

        editProfileButton = view.findViewById(R.id.editProfile);

        bestScoreTextView = view.findViewById(R.id.best_score);

        first_place = view.findViewById(R.id.first_place);
        second_place = view.findViewById(R.id.second_place);
        third_place = view.findViewById(R.id.third_place);

        // Carico i dati del profilo
        try {
            loadInformation();
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG,GENERIC_ERROR+ex.getMessage());
            else ex.printStackTrace();
        }

        editProfileButton.setOnClickListener(v -> {
            try {
                Navigation.findNavController(view).navigate(R.id.action_accountInformationFragment_to_pickUsernameFragment);
            }catch(Exception ex) {
                if (ex.getMessage() != null) Log.e(TAG,GENERIC_ERROR+ex.getMessage());
                else ex.printStackTrace();
            }
        });
    }

    // Metodo per caricare i dati del profilo dalle SharedPreferences
    private void loadInformation() {
        showLoadingScreen();
        try {
            userViewModel.fetchUserInformations(userViewModel.getLoggedUser().getIdToken()).observe(getViewLifecycleOwner(), item -> {
                MutableLiveData<Result> rs = userViewModel.getUserInformations();
                if (Objects.requireNonNull(rs.getValue()).isSuccess()) {
                    usernameTextView.setText(
                            sharedPreferencesUtils.readStringData(
                                    Constants.SHARED_PREFERENCES_FILENAME,
                                    Constants.SHARED_PREFERENCES_USERNAME)
                    );
                    profileImageView.setImageResource(
                            getResourceIdByName(
                                    sharedPreferencesUtils.readStringData(
                                            Constants.SHARED_PREFERENCES_FILENAME,
                                            Constants.SHARED_PREFERENCES_PROFILE_PICTURE
                                    )
                            )
                    );
                    bestScoreTextView.setText(
                            String.valueOf(sharedPreferencesUtils.readIntData(
                                    Constants.SHARED_PREFERENCES_FILENAME,
                                    Constants.SHARED_PREFERENCES_BEST_SCORE)));
                    hideLoadingScreen();
                } else {
                    View view = getView();
                    if (view != null) {
                        Snackbar.make(view, WARNING_LOADING_PROFILE_DATA, Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG,GENERIC_ERROR+ex.getMessage());
            else ex.printStackTrace();
        }

        if (sharedPreferencesUtils.readStringSetData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY)!=null) {
            Set<String> matchPlayedSet = sharedPreferencesUtils.readStringSetData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY);
            if (matchPlayedSet == null || matchPlayedSet.isEmpty()) {
                Log.e(TAG, ERROR_SET_IS_EMPTY);
            } else {
                List<String> matchPlayedList = new ArrayList<>(matchPlayedSet);

                if (matchPlayedList.size() >= 1) {
                    first_place.setAnimation(getCategoryIconFromCode(Integer.parseInt(matchPlayedList.get(0))));
                    if (matchPlayedList.size() >= 2) {
                        second_place.setAnimation(getCategoryIconFromCode(Integer.parseInt(matchPlayedList.get(1))));
                        if (matchPlayedList.size() > 2) {
                            third_place.setAnimation(getCategoryIconFromCode(Integer.parseInt(matchPlayedList.get(2))));
                        }
                    }
                }
            }
        }
    }

    // Metodo per ottenere l'ID della risorsa dal nome
    private int getResourceIdByName(String resourceName) {
        try {
            return requireContext().getResources().getIdentifier(
                    resourceName,
                    DRAWABLE, // Ricerca nelle risorse di tipo drawable
                    requireContext().getPackageName() // Nome del pacchetto
            );
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG,GENERIC_ERROR+ex.getMessage());
            else ex.printStackTrace();
        }
        return R.drawable.p1;
    }

    private int getCategoryIconFromCode(int code) {
        switch (code) {
            case HISTORY_CODE:
                return R.raw.category_history;
            case SCIENCE_NATURE_CODE:
                return R.raw.category_science;
            case GEOGRAPHY_CODE:
                return R.raw.category_geography;
            case SPORTS_CODE:
                return R.raw.category_sport;
            default:
                return R.raw.category_all;
        }
    }

    public void showLoadingScreen() {
        profileLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoadingScreen(){
        circularProgressIndicator.setVisibility(View.GONE);
        profileLayout.setVisibility(View.VISIBLE);
    }
}
