package com.unimib.triviaducks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.validator.routines.EmailValidator;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getName();

    private TextInputEditText editTextEMail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        editTextEMail = findViewById(R.id.textInputEmail);
        editTextPassword = findViewById(R.id.textInputPassword);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            if(isEmailOk(editTextEMail.getText().toString())) {
                if (isPasswordOk(editTextPassword.getText().toString())) {
                    Log.d(TAG, "Launch new activity");
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Log.d(TAG, "Error password");
                    editTextPassword.setError("Check your password");
                    Snackbar.make(findViewById(android.R.id.content), "Check your password", Snackbar.LENGTH_SHORT).show();

                }
            }else{
                Log.d(TAG, "Error email");
                editTextEMail.setError("Insert a correct email");
                Snackbar.make(findViewById(android.R.id.content), "Insert a correct email", Snackbar.LENGTH_SHORT).show();
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