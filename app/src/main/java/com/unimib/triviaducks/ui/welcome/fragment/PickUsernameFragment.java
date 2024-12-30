package com.unimib.triviaducks.ui.welcome.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.home.HomeActivity;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

public class PickUsernameFragment extends Fragment {
    public static final String TAG = PickUsernameFragment.class.getName();

    private TextView usernameTextView;
    private EditText usernameEditText;
    private Button confirmUsernameButton;
    private UserViewModel userViewModel;

    public PickUsernameFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_username, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        confirmUsernameButton = view.findViewById(R.id.confirmUsernameButton);

        confirmUsernameButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
            }
            else if(username.length() > 15) {
                Toast.makeText(getContext(), "Please enter a shorter username", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

                sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                        Constants.SHARED_PREFERENCES_USERNAME,
                        username);

                userViewModel.saveUserPreferences(
                        username,
                        userViewModel.getLoggedUser().getIdToken()
                );

                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}