package com.unimib.triviaducks.ui.home.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.MainActivity;

public class AccountInformationFragment extends Fragment {
    private ImageButton accountButton;

    public AccountInformationFragment() {
        // Required empty public constructor
    }

    public static AccountInformationFragment newInstance() {
        AccountInformationFragment fragment = new AccountInformationFragment();
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
        return inflater.inflate(R.layout.fragment_account_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Scegliere immagine dalla galleria
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

        ImageButton close = view.findViewById(R.id.close);
        accountButton = view.findViewById(R.id.profile);

        //Bottone per la chiusura del fragment
        close.setOnClickListener(v -> {
            //TODO cambiare con nav graph
            Log.d("MainActivity", "AAAAAAAAAAAAAAAAAAAAAAAAAA");
        });

        accountButton.setOnClickListener(v -> {
            //richiama il metodo a riga 51
            mGetContent.launch("image/*");
        });
    }
}