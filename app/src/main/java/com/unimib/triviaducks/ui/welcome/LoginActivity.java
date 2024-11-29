package com.unimib.triviaducks.ui.welcome;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.unimib.triviaducks.R;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getName();

    private TextInputEditText editTextEMail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
    }
}