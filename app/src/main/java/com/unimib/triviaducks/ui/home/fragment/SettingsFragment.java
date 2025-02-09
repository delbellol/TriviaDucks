package com.unimib.triviaducks.ui.home.fragment;

import static com.unimib.triviaducks.util.Constants.MUSIC_OFF;
import static com.unimib.triviaducks.util.Constants.MUSIC_ON;
import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_VOLUME;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.game.QuestionActivity;
import com.unimib.triviaducks.ui.welcome.WelcomeActivity;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.MusicService;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private SharedPreferencesUtils sharedPreferencesUtils;

    private Switch musicSwitch;
    private boolean isMusicOFF;

    private UserViewModel userViewModel;

    private Button logoutButton;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() { return new SettingsFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(requireActivity().getApplication());

        musicSwitch = view.findViewById(R.id.music_switch);
        logoutButton = view.findViewById(R.id.logout);

        isMusicOFF = sharedPreferencesUtils.readBooleanData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_IS_MUSIC_OFF);

        // Imposta lo stato iniziale dello switch music
        musicSwitch.setChecked(!isMusicOFF);

        // Gestione del cambio stato dello switch music
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent intent = new Intent(requireContext(), MusicService.class);
            if (isChecked) {
                intent.setAction(MUSIC_ON);
                isMusicOFF = false;
            } else {
                intent.setAction(MUSIC_OFF);
                isMusicOFF = true;
            }
            requireContext().startService(intent);

            // Salva lo stato dell'interruttore
            sharedPreferencesUtils.writeBooleanData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_IS_MUSIC_OFF,
                    isMusicOFF);
        });

        logoutButton.setOnClickListener(v -> {
            userViewModel.logout();
            sharedPreferencesUtils.clearPreferences(Constants.SHARED_PREFERENCES_FILENAME);
            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(intent);
        });
    }
}
