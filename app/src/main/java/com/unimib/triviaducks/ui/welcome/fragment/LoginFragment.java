package com.unimib.triviaducks.ui.welcome.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.unimib.triviaducks.R;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private TextInputEditText editTextEMail, editTextPassword;

    public LoginFragment() {
        // Required empty public constructor
    }
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEMail = view.findViewById(R.id.textInputEmail);
        editTextPassword = view.findViewById(R.id.textInputPassword);

        Button loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            if(isEmailOk(editTextEMail.getText().toString())) {
                if (isPasswordOk(editTextPassword.getText().toString())) {
                    Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainActivity);
                    //Intent intent = new Intent(this, MainActivity.class);
                    //startActivity(intent);
                }else{
                    editTextPassword.setError("Check your password");
                    Snackbar.make(view.findViewById(android.R.id.content), "Check your password", Snackbar.LENGTH_SHORT).show();

                }
            }else{
                editTextEMail.setError("Insert a correct email");
                Snackbar.make(view.findViewById(android.R.id.content), "Insert a correct email", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isEmailOk(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    private boolean isPasswordOk(String password) {
        return password.length() > 7;
    }
}