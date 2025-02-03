package com.unimib.triviaducks.ui.home.fragment;

// Import delle costanti per l'uso nelle SharedPreferences

import static com.unimib.triviaducks.util.Constants.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.Converter;
import com.unimib.triviaducks.util.NetworkUtil;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AccountInformationFragment extends Fragment {
    private static final String TAG = AccountInformationFragment.class.getSimpleName(); // Tag per i log

    // Dichiarazioni delle variabili UI e logiche
    private ImageButton changePfPBtn; // Pulsante per cambiare immagine di profilo
    private ImageButton profilePicture;
    private ImageView profileImageView; // Visualizzazione immagine profilo
    private TextView usernameTextView; // Visualizzazione nome utente
    private TextView bestScoreTextView;
    private UserViewModel userViewModel; // ViewModel per gestire i dati utente
    private Converter converter; // Classe per conversioni
    private SharedPreferencesUtils sharedPreferencesUtils; // Utility per SharedPreferences
    private CircularProgressIndicator circularProgressIndicator;
    private ConstraintLayout accountLayout;
    private LottieAnimationView first_place, second_place, third_place;
    private Button logoutButton;

    public AccountInformationFragment() {
    }

    // Metodo statico per creare una nuova istanza del fragment
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

        // Inizializzazione delle utility
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        // Collegamento della TextView per il nome utente
        usernameTextView = view.findViewById(R.id.username);

        // Collegamento dell'ImageView per il profilo
        profileImageView = view.findViewById(R.id.profilePicture);

        logoutButton = view.findViewById(R.id.logout);

        bestScoreTextView = view.findViewById(R.id.best_score);

        first_place = view.findViewById(R.id.first_place);
        second_place = view.findViewById(R.id.second_place);
        third_place = view.findViewById(R.id.third_place);

        // Carico immagine profilo
        try {
            loadInformation();

            // Collegamento dei pulsanti per cambiare immagine
            changePfPBtn = view.findViewById(R.id.ChangePfPBtn);
            profilePicture = view.findViewById(R.id.profilePicture);

            // Imposta i listener per aprire il dialog per cambiare immagine
            changePfPBtn.setOnClickListener(v -> showProfileImageDialog());
            profilePicture.setOnClickListener(v -> showProfileImageDialog());
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG,"Errore: "+ex.getMessage());
            else Log.e(TAG,"Errore strano");
        }
    }

    //TODO ce un metodo identico dentro pick username
    //TODO da spostare tutti i metodi
    private void showProfileImageDialog() {
        // Creazione e configurazione del dialog per la selezione immagine
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_profile_image_selection); // Imposta il layout del dialog
        dialog.setCancelable(true); // Permette la chiusura del dialog quando si clicca fuori

        // Collegamento dei pulsanti all'interno del dialog
        int[] buttonIds = { R.id.defaultProfilePicture, R.id.defaultProfilePicture2, R.id.defaultProfilePicture3,
                R.id.defaultProfilePicture4, R.id.defaultProfilePicture5, R.id.defaultProfilePicture6 };

        for (int id : buttonIds) {
            ImageButton imageButton = dialog.findViewById(id);

            // Imposta il listener per ogni pulsante
            if (imageButton != null) {
                imageButton.setOnClickListener(v -> {
                    // Cambia immagine profilo e salva
                    Drawable selectedDrawable = ((ImageButton) v).getDrawable();
                    profileImageView.setImageDrawable(selectedDrawable);
                    saveResourceName(selectedDrawable); // Salva il nome della risorsa
                    dialog.dismiss(); // Chiudi il dialog
                });
            }
        }

        // Mostra il dialog
        dialog.show();
    }

    //TODO ce un metodo identico dentro pick username

    // Metodo per salvare il nome della risorsa selezionata
    private void saveResourceName(Drawable selectedDrawable) {
        if (selectedDrawable instanceof BitmapDrawable) {
            String resourceName = getResourceName((BitmapDrawable) selectedDrawable); // Ottieni il nome della risorsa
            sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_PROFILE_PICTURE, resourceName); // Salva il nome della risorsa

            // Carica il nome su Firebase
            userViewModel.saveUserImage(
                    resourceName,
                    userViewModel.getLoggedUser().getIdToken()
            );
        }
    }

    // Metodo per caricare l'immagine del profilo dalle SharedPreferences
    private void loadInformation() {
        userViewModel.getUserImages(
                userViewModel.getLoggedUser().getIdToken()
        );

        userViewModel.getUserPreferences(
                userViewModel.getLoggedUser().getIdToken()
        );

        userViewModel.getCategoriesPodium(
                userViewModel.getLoggedUser().getIdToken()
        );

        userViewModel.getUserBestScore(
                userViewModel.getLoggedUser().getIdToken()
        );

        if (sharedPreferencesUtils.readStringSetData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY)==null)
            Log.d(TAG, "SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY is null");
        else {
            Log.d(TAG, String.valueOf(sharedPreferencesUtils.readStringSetData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY)));
            Set<String> matchPlayedSet = sharedPreferencesUtils.readStringSetData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY);
            if (matchPlayedSet == null || matchPlayedSet.isEmpty()) {
                Log.e(TAG, "The set is empty");
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

        usernameTextView.setText(
                sharedPreferencesUtils.readStringData(
                        Constants.SHARED_PREFERENCES_FILENAME,
                        Constants.SHARED_PREFERENCES_USERNAME));

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
    }

    // Metodo per ottenere l'ID della risorsa dal nome
    private int getResourceIdByName(String resourceName) {
        return requireContext().getResources().getIdentifier(
                resourceName,
                "drawable", // Ricerca nelle risorse di tipo drawable
                requireContext().getPackageName() // Nome del pacchetto
        );
    }

    private int getCategoryIconFromCode(int code) {
        switch (code) {
            default:
                return R.raw.category_all;
            case HISTORY_CODE:
                return R.raw.category_science;
            case SCIENCE_NATURE_CODE:
                return R.raw.category_geography;
            case GEOGRAPHY_CODE:
                return R.raw.category_history;
            case SPORTS_CODE:
                return R.raw.category_sport;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private String getResourceName(Drawable drawable) {
        if (drawable != null) {
            // Confronta la risorsa con le risorse hardcoded per determinare il nome della risorsa
            if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p1).getConstantState())) {
                return getResources().getResourceName(R.drawable.p1); // Restituisce il nome della risorsa
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p2).getConstantState())) {
                return getResources().getResourceName(R.drawable.p2);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p3).getConstantState())) {
                return getResources().getResourceName(R.drawable.p3);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p4).getConstantState())) {
                return getResources().getResourceName(R.drawable.p4);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p5).getConstantState())) {
                return getResources().getResourceName(R.drawable.p5);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p6).getConstantState())) {
                return getResources().getResourceName(R.drawable.p6);
            }
        }
        return null; // Nessuna risorsa corrispondente trovata
    }
}
