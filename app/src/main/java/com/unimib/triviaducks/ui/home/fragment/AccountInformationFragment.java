
package com.unimib.triviaducks.ui.home.fragment;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.unimib.triviaducks.R;

public class AccountInformationFragment extends Fragment {
    private ImageButton changePfPBtn; // Pulsante per cambiare immagine di profilo
    private ImageButton profilePicture;
    private ImageView profileImageView;

    private ActivityResultLauncher<String> mGetContent; // Launcher per ottenere immagine dalla galleria

    public AccountInformationFragment() {
        // Required empty public constructor
    }

    public static AccountInformationFragment newInstance() {
        return new AccountInformationFragment();
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

        profileImageView = view.findViewById(R.id.profilePicture); // Trova immagine di profilo

        // Registrazione per la selezione di immagini dalla galleria
        mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        profileImageView.setImageURI(uri); // Imposta l'immagine selezionata
                    }
                }
        );

        // Pulsante per cambiare immagine del profilo
        changePfPBtn = view.findViewById(R.id.ChangePfPBtn);
        profilePicture = view.findViewById(R.id.profilePicture);

        changePfPBtn.setOnClickListener(v -> showProfileImageDialog());
        profilePicture.setOnClickListener(v -> showProfileImageDialog());
    }

    private void showProfileImageDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_profile_image_selection); // Layout del Dialog
        dialog.setCancelable(true);

        // Trova gli ImageButton all'interno del Dialog
        int[] buttonIds = { R.id.defaultProfilePicture, R.id.defaultProfilePicture2, R.id.defaultProfilePicture3,
                R.id.defaultProfilePicture4, R.id.defaultProfilePicture5, R.id.defaultProfilePicture6 };

        for (int id : buttonIds) {
            ImageButton imageButton = dialog.findViewById(id);

            // Imposta il listener per ogni ImageButton
            if (imageButton != null) {
                imageButton.setOnClickListener(v -> {
                    // Cambia l'immagine del profilo
                    Drawable selectedDrawable = ((ImageButton) v).getDrawable();
                    profileImageView.setImageDrawable(selectedDrawable); // Cambia l'immagine del profilo
                    dialog.dismiss(); // Chiudi il dialog
                });
            }
        }

        // Aggiungi la funzionalità per caricare un'immagine dalla galleria
        ImageButton importFromGalleryButton = dialog.findViewById(R.id.ImportPfpFromDeviceBtn);
        if (importFromGalleryButton != null) {
            importFromGalleryButton.setOnClickListener(v -> {
                mGetContent.launch("image/*");
                dialog.dismiss(); // Chiudi il dialog
            });
        }

        // Mostra il Dialog
        dialog.show();
    }
}
