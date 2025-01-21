package com.unimib.triviaducks.ui.welcome.fragment;

import static com.unimib.triviaducks.R.*;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private SharedPreferencesUtils sharedPreferencesUtils;

    private EditText usernameEditText;
    private Button confirmUsernameButton;
    private ImageButton profilePictureButton;
    private UserViewModel userViewModel;

    private Drawable selectedProfileDrawable;

    public PickUsernameFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_username, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        usernameEditText = view.findViewById(R.id.usernameEditText);
        confirmUsernameButton = view.findViewById(R.id.confirmUsernameButton);
        profilePictureButton = view.findViewById(R.id.profilePictureImageButton);

        profilePictureButton.setOnClickListener(v -> showProfileImageDialog());

        confirmUsernameButton.setOnClickListener(v -> saveUserData());
    }

    private void showProfileImageDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_profile_image_selection);
        dialog.setCancelable(true);

        int[] buttonIds = {
                R.id.defaultProfilePicture, R.id.defaultProfilePicture2, R.id.defaultProfilePicture3,
                R.id.defaultProfilePicture4, R.id.defaultProfilePicture5, R.id.defaultProfilePicture6
        };

        for (int id : buttonIds) {
            ImageButton imageButton = dialog.findViewById(id);
            if (imageButton != null) {
                imageButton.setOnClickListener(v -> {
                    selectedProfileDrawable = imageButton.getDrawable();
                    profilePictureButton.setImageDrawable(selectedProfileDrawable);
                    dialog.dismiss();
                });
            }
        }

        dialog.show();
    }

    private void saveUserData() {
        String username = usernameEditText.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() > 15) {
            Toast.makeText(getContext(), "Please enter a shorter username", Toast.LENGTH_SHORT).show();
            return;
        }

        String resourceName = getResourceName(selectedProfileDrawable);

        sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERENCES_USERNAME, username);
        sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERENCES_PROFILE_PICTURE, resourceName);

        userViewModel.saveUserPreferences(username, userViewModel.getLoggedUser().getIdToken());

        if (resourceName != null) {
            userViewModel.saveUserImage(resourceName, userViewModel.getLoggedUser().getIdToken());
        }

        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private String getResourceName(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            if (drawable.getConstantState() != null) {
                int[] resourceIds = {
                        R.drawable.p1, R.drawable.p2, R.drawable.p3,
                        R.drawable.p4, R.drawable.p5, R.drawable.p6
                };

                for (int resourceId : resourceIds) {
                    Drawable resourceDrawable = requireContext().getDrawable(resourceId);
                    if (resourceDrawable != null && drawable.getConstantState().equals(resourceDrawable.getConstantState())) {
                        return getResources().getResourceName(resourceId);
                    }
                }
            }
        }
        return null;
    }
}
