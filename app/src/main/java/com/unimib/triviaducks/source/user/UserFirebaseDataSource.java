package com.unimib.triviaducks.source.user;

import static com.unimib.triviaducks.util.Constants.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.unimib.triviaducks.model.User;
import com.unimib.triviaducks.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Class that gets the user information using Firebase Realtime Database.
 */
public class UserFirebaseDataSource extends BaseUserDataRemoteDataSource {
    private static final String TAG = UserFirebaseDataSource.class.getSimpleName();
    private final DatabaseReference databaseReference;
    private final SharedPreferencesUtils sharedPreferencesUtil;
    public UserFirebaseDataSource(SharedPreferencesUtils sharedPreferencesUtil) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }
    @Override
    public void saveUserData(User user) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User already present in Firebase Realtime Database");
                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                } else {
                    Log.d(TAG, "User not present in Firebase Realtime Database");
                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    userResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

    @Override
    public void getUserPreferences(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_USERNAME).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String username = task.getResult().getValue(String.class);
                        sharedPreferencesUtil.writeStringData(
                                SHARED_PREFERENCES_FILENAME,
                                SHARED_PREFERENCES_USERNAME,
                                username);
                        userResponseCallback.onSuccessFromGettingUserPreferences();
                    }
                });
    }

    @Override
    public void getUserImages(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_PROFILE_PICTURE).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String image = task.getResult().getValue(String.class);
                        if (image != null) {
                            sharedPreferencesUtil.writeStringData(
                                    SHARED_PREFERENCES_FILENAME,
                                    SHARED_PREFERENCES_PROFILE_PICTURE,
                                    image);
                            userResponseCallback.onSuccessFromGettingUserPreferences();
                        } else {
                            Log.e(TAG, "Image data from Firebase is null!");
                        }
                    }
                });
    }

    @Override
    public void getUserBestScore(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_BEST_SCORE).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int score = 0;
                        if (task.getResult().getValue(Integer.class) != null)
                            score = task.getResult().getValue(Integer.class);
                        sharedPreferencesUtil.writeIntData(
                                SHARED_PREFERENCES_FILENAME,
                                SHARED_PREFERENCES_BEST_SCORE,
                                score);
                        userResponseCallback.onSuccessFromGettingUserPreferences();
                    }
                });
    }

    @Override
    public void getCategoriesPodium(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            Map<String, Integer> categoryCountMap = new HashMap<>();

                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                String category = categorySnapshot.getKey();
                                Integer count = categorySnapshot.getValue(Integer.class);
                                categoryCountMap.put(category, count);
                            }

                            Set<String> topCategories = categoryCountMap
                                    .entrySet()
                                    .stream()
                                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                                    .limit(3)
                                    .map(Map.Entry::getKey)
                                    .collect(Collectors.toSet());

//                            for (String category : topCategories) {
//                                Log.d(TAG, "Categoria più giocata: " + category);
//                            }

                            sharedPreferencesUtil.writeStringSetData(
                                    SHARED_PREFERENCES_FILENAME,
                                    SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY,
                                    topCategories
                            );

                            userResponseCallback.onSuccessFromGettingUserPreferences();
                        }
                        else {
                            Log.d(TAG, "No data found for categories!");
                        }
                    }
                    else {
                        Log.d(TAG, "Exception: "+task.getException());
                    }
                });
    }

    @Override
    public void saveUserPreferences(String username, String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_USERNAME).setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    @Override
    public void saveUserImage(String imageName, String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(SHARED_PREFERENCES_PROFILE_PICTURE).setValue(imageName).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
    }

    @Override
    public void saveBestScore(int score, String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION)
                .child(idToken)
                .child(SHARED_PREFERENCES_BEST_SCORE)
                .setValue(score).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
    }

    @Override
    public void updateCategoryCounter(String category, String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY)
                .child(category).setValue(ServerValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
    }
}
