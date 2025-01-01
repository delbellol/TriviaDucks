package com.unimib.triviaducks.ui.home.fragment;

// Import delle costanti per l'uso nelle SharedPreferences
import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_FILENAME;
import static com.unimib.triviaducks.util.Constants.SHARED_PREFERENCES_PROFILE_PICTURE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimib.triviaducks.R;
import com.unimib.triviaducks.repository.user.IUserRepository;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModel;
import com.unimib.triviaducks.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.triviaducks.util.Constants;
import com.unimib.triviaducks.util.Converter;
import com.unimib.triviaducks.util.ServiceLocator;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.io.IOException;


public class AccountInformationFragment extends Fragment {
    private static final String TAG = AccountInformationFragment.class.getSimpleName(); // Tag per i log

    // Dichiarazioni delle variabili UI e logiche
    private ImageButton changePfPBtn; // Pulsante per cambiare immagine di profilo
    private ImageButton profilePicture;
    private ImageView profileImageView; // Visualizzazione immagine profilo
    private TextView usernameTextView; // Visualizzazione nome utente
    private UserViewModel userViewModel; // ViewModel per gestire i dati utente
    private Converter converter; // Classe per conversioni
    private SharedPreferencesUtils sharedPreferencesUtils; // Utility per SharedPreferences

    public AccountInformationFragment() {
    }

    // Metodo statico per creare una nuova istanza del fragment
    public static AccountInformationFragment newInstance() {
        return new AccountInformationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        userViewModel.setAuthenticationError(false);// Inizializzazione base del fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                                handleGalleryImageResult(uri);
                            }
                        }
                    }
        );

        // Inizializzazione delle utility
        converter = new Converter();
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        // Collegamento della TextView per il nome utente
        usernameTextView = view.findViewById(R.id.username);

        // Impostazione del nome utente letto dalle SharedPreferences
        usernameTextView.setText(sharedPreferencesUtils.readStringData(Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_USERNAME));

        // Collegamento dell'ImageView per il profilo
        profileImageView = view.findViewById(R.id.profilePicture);

        // Controllo se è salvata un'immagine di profilo e caricamento
        if (sharedPreferencesUtils.readStringData(SHARED_PREFERENCES_FILENAME, SHARED_PREFERENCES_PROFILE_PICTURE) != null) {
            loadProfileImageFromPreferences();
        }

        // Collegamento dei pulsanti per cambiare immagine
        changePfPBtn = view.findViewById(R.id.ChangePfPBtn);
        profilePicture = view.findViewById(R.id.profilePicture);

        // Imposta i listener per aprire il dialog per cambiare immagine
        changePfPBtn.setOnClickListener(v -> showProfileImageDialog(mGetContent));
        profilePicture.setOnClickListener(v -> showProfileImageDialog(mGetContent));
    }

    private void showProfileImageDialog(ActivityResultLauncher<String> mGetContent) {
        // Creazione e configurazione del dialog per la selezione immagine
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_profile_image_selection); // Imposta il layout del dialog
        dialog.setCancelable(true); // Permette la chiusura del dialog quando si clicca fuori

        // Collegamento dei pulsanti all'interno del dialog
        int[] buttonIds = { R.id.defaultProfilePicture, R.id.defaultProfilePicture2, R.id.defaultProfilePicture3,
                R.id.defaultProfilePicture4, R.id.defaultProfilePicture5, R.id.defaultProfilePicture6 };

        for (int id : buttonIds) {
            ImageButton imageButton = dialog.findViewById(id);

            // Imposta il listener per ogni pulsante
            if (imageButton != null) {
                imageButton.setOnClickListener(v -> {
                    // Cambia immagine profilo e salva
                    Drawable selectedDrawable = ((ImageButton) v).getDrawable();
                    profileImageView.setImageDrawable(selectedDrawable);
                    saveResourceName(selectedDrawable); // Salva il nome della risorsa
                    dialog.dismiss(); // Chiudi il dialog
                });
            }
        }

        // Pulsante per importare immagine dalla galleria
        ImageButton importFromGalleryButton = dialog.findViewById(R.id.ImportPfpFromDeviceBtn);
        if (importFromGalleryButton != null) {
            importFromGalleryButton.setOnClickListener(v -> {
                if (mGetContent != null) {
                    mGetContent.launch("image/*");
                } else {
                    Log.e(TAG, "mGetContent is null!");
                }
                dialog.dismiss(); // Chiudi il dialog
            });
        }

        // Mostra il dialog
        dialog.show();
    }

    // Metodo per gestire l'immagine caricata dalla galleria
    private void handleGalleryImageResult(Uri selectedImageUri) {
        if (selectedImageUri != null) {
            try {
                // Carica il Bitmap dall'Uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                // Cambia l'immagine del profilo
                profileImageView.setImageBitmap(bitmap);

                // Salva l'immagine come bitmap
                saveProfileImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                // Puoi gestire errori qui, ad esempio mostrando un messaggio all'utente
            }
        }
    }


    // Metodo per salvare l'immagine selezionata dalla galleria
    private void saveProfileImage(Bitmap bitmap) {
        if (bitmap != null) {
            // Converte il bitmap in stringa Base64
            String imageBase64 = converter.fromBitmapToBase64(bitmap);

            // Salva l'immagine in Base64 nelle SharedPreferences
            sharedPreferencesUtils.writeStringData(
                    Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_PROFILE_PICTURE,
                    imageBase64
            );

            // Carica l'immagine su Firebase
            uploadImageToFirebase(imageBase64);
        }
    }


    // Metodo per salvare il nome della risorsa selezionata
    private void saveResourceName(Drawable selectedDrawable) {
        if (selectedDrawable instanceof BitmapDrawable) {
            String resourceName = getResourceName((BitmapDrawable) selectedDrawable); // Ottieni il nome della risorsa
            sharedPreferencesUtils.writeStringData(Constants.SHARED_PREFERENCES_FILENAME,
                    Constants.SHARED_PREFERENCES_PROFILE_PICTURE, resourceName); // Salva il nome della risorsa

            // Carica il nome su Firebase
            uploadImageToFirebase(resourceName);
        }
    }

    private void uploadImageToFirebase(String imageName) {
        Log.d(TAG, userViewModel.getLoggedUser().getIdToken());
        userViewModel.saveUserImage(
                imageName,
                userViewModel.getLoggedUser().getIdToken()
        );
    }

    // Metodo per caricare l'immagine del profilo dalle SharedPreferences
    private void loadProfileImageFromPreferences() {
        String savedData = sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_PROFILE_PICTURE
        );

        Log.d("ProfileImage", "Saved data: " + savedData); // Log per verificare i dati salvati

        if (savedData != null) {
            if (isBase64String(savedData)) {
                // La stringa è in formato Base64
                Bitmap bitmap = converter.fromBase64ToBitmap(savedData); // Converte Base64 in bitmap
                profileImageView.setImageBitmap(bitmap); // Imposta l'immagine nella ImageView
            } else {
                // La stringa è un nome di risorsa
                int resourceId = getResourceIdByName(savedData); // Ottieni l'ID della risorsa
                Log.d(TAG, resourceId + "");
                if (resourceId != 0) {
                    profileImageView.setImageResource(resourceId); // Imposta l'immagine della risorsa
                }
            }
        }
    }

    // Verifica se la stringa è una stringa Base64 valida
    private boolean isBase64String(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            byte[] decodedBytes = Base64.decode(input, Base64.DEFAULT); // Decodifica la stringa Base64
            // Controlla se i byte possono essere ricodificati in Base64
            String reEncoded = Base64.encodeToString(decodedBytes, Base64.DEFAULT).trim();
            return input.trim().equals(reEncoded);
        } catch (IllegalArgumentException e) {
            return false; // La stringa non è una Base64 valida
        }
    }

    // Metodo per ottenere l'ID della risorsa dal nome
    private int getResourceIdByName(String resourceName) {
        return requireContext().getResources().getIdentifier(
                resourceName,
                "drawable", // Ricerca nelle risorse di tipo drawable
                requireContext().getPackageName() // Nome del pacchetto
        );
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private String getResourceName(Drawable drawable) {
        if (drawable != null) {
            // Confronta la risorsa con le risorse hardcoded per determinare il nome della risorsa
            if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p1).getConstantState())) {
                return getResources().getResourceName(R.drawable.p1); // Restituisce il nome della risorsa
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p2).getConstantState())) {
                return getResources().getResourceName(R.drawable.p2);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p3).getConstantState())) {
                return getResources().getResourceName(R.drawable.p3);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p4).getConstantState())) {
                return getResources().getResourceName(R.drawable.p4);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p5).getConstantState())) {
                return getResources().getResourceName(R.drawable.p5);
            } else if (drawable.getConstantState().equals(requireContext().getDrawable(R.drawable.p6).getConstantState())) {
                return getResources().getResourceName(R.drawable.p6);
            }
        }
        return null; // Nessuna risorsa corrispondente trovata
    }
}
