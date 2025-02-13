package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.*;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.adapter.CategoriesPodiumAdapter;
import com.unimib.triviaducks.model.Result;
import com.unimib.triviaducks.repository.user.IUserRepository;
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
import java.util.stream.Collectors;


public class AccountInformationFragment extends Fragment {
    private static final String TAG = AccountInformationFragment.class.getSimpleName(); // Tag per i log

    private Button editProfileButton;
    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView bestScoreTextView;
    private UserViewModel userViewModel;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private CircularProgressIndicator circularProgressIndicator;
    private ConstraintLayout profileLayout;
    private GridView gridView;

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
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileLayout = view.findViewById(R.id.profileLayout);
        circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        usernameTextView = view.findViewById(R.id.username);

        profileImageView = view.findViewById(R.id.profilePicture);

        editProfileButton = view.findViewById(R.id.editProfile);

        bestScoreTextView = view.findViewById(R.id.bestScore);

        gridView = view.findViewById(R.id.gridLayout);

        try {
            loadInformation();
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
            else ex.printStackTrace();
        }

        editProfileButton.setOnClickListener(v -> {
            try {
                Navigation.findNavController(view).navigate(R.id.action_accountInformationFragment_to_pickUsernameFragment);
            }catch(Exception ex) {
                if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
                else ex.printStackTrace();
            }
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

                    Set<String> matchPlayedSet = sharedPreferencesUtils.readStringSetData(
                            Constants.SHARED_PREFERENCES_FILENAME,
                            Constants.SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY);

                    ArrayList<String> matchPlayedList = new ArrayList<>();

                    if (matchPlayedSet != null && !matchPlayedSet.isEmpty())
                        matchPlayedList = (ArrayList<String>) convertAndSortCategories(matchPlayedSet);


                    CategoriesPodiumAdapter adapter = new CategoriesPodiumAdapter(
                            getContext(),
                            R.layout.item_categories_podium,
                            matchPlayedList
                    );
                    gridView.setAdapter(adapter);

                    hideLoadingScreen();
                } else {
                    View view = getView();
                    if (view != null) {
                        Snackbar.make(view, WARNING_LOADING_PROFILE_DATA, Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
            else ex.printStackTrace();
        }
    }

    // metodo per recuperare l'ID dell'immagine profilo dal suo nome da shared preference
    private int getResourceIdByName(String resourceName) {
        try {
            return requireContext().getResources().getIdentifier(
                    resourceName,
                    DRAWABLE,
                    requireContext().getPackageName()
            );
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
            else ex.printStackTrace();
        }
        return R.drawable.p1;
    }

    public void showLoadingScreen() {
        profileLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoadingScreen(){
        circularProgressIndicator.setVisibility(View.GONE);
        profileLayout.setVisibility(View.VISIBLE);
    }

    // metodo per convertire il set di stringhe che contiene le categorie più giocate in una lista
    // di stringhe che contengono il codice della categoria, ordinate in ordine decrescente per il
    // numero di partite giocate.
    private List<String> convertAndSortCategories(Set<String> categoriesSet) {
        List<String> sortedList = new ArrayList<>();
        if (categoriesSet != null && !categoriesSet.isEmpty()) {
            try {
                sortedList = categoriesSet.stream()
                        .map(item -> {
                            if (item != null) {
                                String[] parts = item.split(SPLIT_CHARACTER);
                                String category = parts[0];
                                int matchplay = (parts.length > 1 && !Objects.equals(parts[1], NULL)) ? Integer.parseInt(parts[1]) : 0;
                                return new String[]{category, String.valueOf(matchplay)};
                            } else {
                                Log.e(TAG, ERROR_ITEM_IS_NULL);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .sorted((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])))
                        .map(a -> a[0])
                        .limit(LIMIT_TOP_CATEGORIES) // limite al numero di categorie mostrate all'utente = 3
                        .collect(Collectors.toList());
            } catch (Exception ex) {
                if (ex.getMessage() != null) Log.e(TAG, ERROR + ex.getMessage());
                else ex.printStackTrace();
            }
        } else {
            if (categoriesSet == null)
                Log.e(TAG, ERROR_CATEGORY_SET_IS_NULL);
            if (categoriesSet.isEmpty())
                Log.e(TAG, ERROR_CATEGORY_SET_IS_EMPTY);
        }
        return sortedList;
    }

}
