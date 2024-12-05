
package com.unimib.triviaducks.ui.home.fragment;

import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.ui.home.MainActivity;

public class AccountInformationFragment extends Fragment {
    private ImageButton importImageButton; //pulsante importa immagine dalla galleria
    private ImageButton changePfPBtn; //pulsante per cambiare immagine di profilo
    private LinearLayout defaultImagesGallery; //layout contenente la scrollbar delle immagini di default
    private ImageView profileImageView;

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
        return inflater.inflate(R.layout.fragment_account_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImageView = view.findViewById(R.id.profilePicture); //trova immagine di profilo

        //Scegliere immagine dalla galleria
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            profileImageView.setImageURI(uri);
                        }
                    }
                }
        );

        importImageButton = view.findViewById(R.id.ImportPfpFromDeviceBtn);

        importImageButton.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });



        //Default images gallery:

        // Trova il LinearLayout e il pulsante nel layout
        defaultImagesGallery = view.findViewById(R.id.DefaultImagesGallery);
        changePfPBtn = view.findViewById(R.id.ChangePfPBtn);

        defaultImagesGallery.setVisibility(View.GONE); //imposta di base la visibilità della barra di scelta dell'immagine di profilo a "GONE"

        changePfPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultImagesGallery.getVisibility() == View.VISIBLE) {
                    // Nascondi il LinearLayout
                    defaultImagesGallery.setVisibility(View.GONE);
                } else {
                    // Mostra il LinearLayout
                    defaultImagesGallery.setVisibility(View.VISIBLE);
                }
            }
        });


        // Trova tutti gli ImageButton
        int[] buttonIds = { R.id.defaultProfilePicture, R.id.defaultProfilePicture2, R.id.defaultProfilePicture3, R.id.defaultProfilePicture4, R.id.defaultProfilePicture5, R.id.defaultProfilePicture6};

        for (int id : buttonIds) {
            ImageButton imageButton = view.findViewById(id);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chiama il metodo per cambiare l'immagine del profilo
                    changeProfileImage(((ImageButton) v).getDrawable());
                }
            });
        }

    }

    private void changeProfileImage(Drawable drawable) {
        profileImageView.setImageDrawable(drawable);

        //TODO: fare in modo che la modifica sia permanente al riavvio dell'app
    }
}
