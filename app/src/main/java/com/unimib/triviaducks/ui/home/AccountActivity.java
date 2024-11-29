package com.unimib.triviaducks.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.unimib.triviaducks.R;

public class AccountActivity extends AppCompatActivity {

    private ImageButton accountButton;

    // GetContent creates an ActivityResultLauncher<String> to let you pass
    // in the mime type you want to let the user select
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        accountButton.setImageURI(uri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        ImageButton close = findViewById(R.id.close);
        accountButton = findViewById(R.id.profile);

        close.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        accountButton.setOnClickListener(view -> {
            mGetContent.launch("image/*");
        });
    }
}
