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

import java.util.HashSet;

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
                    Log.d(TAG, INFO_USER_ALREADY_IN_DB);
                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                } else {
                    Log.d(TAG, INFO_USER_NOT_IN_DB);
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
    public void getUserUsername(String idToken) {
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
                            Log.e(TAG, ERROR_FIREBASE_IMAGE_DATA_NULL);
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
    public void saveUserUsername(String username, String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                //TODO Change void with result
                child(SHARED_PREFERENCES_USERNAME).setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, INFO_SUCCESS);
                    }
                });
    }

    @Override
    public void saveUserImage(String imageName, String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                //TODO Change void with result
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
                //TODO Change void with result
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
                //TODO Change void with result
                .child(category).setValue(ServerValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });
    }

    @Override
    public void fetchUserInformations(String idToken) {
        try {
            getUserUsername(idToken);
            getUserImages(idToken);
            getUserBestScore(idToken);
            getCategoriesPodium(idToken);
        }catch(Exception ex) {
            if (ex.getMessage() != null) Log.e(TAG, ERROR +ex.getMessage());
            else ex.printStackTrace();
        }
    }

    @Override
    public void getLeaderboard() {
        databaseReference
                .child(FIREBASE_USERS_COLLECTION)
                .orderByChild(SHARED_PREFERENCES_BEST_SCORE)
                .limitToLast(LIMIT_LEADERBOARD_ACCOUNT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();

                        HashSet<String> leaderboardSet = new HashSet<>();

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String username = userSnapshot.child(SHARED_PREFERENCES_USERNAME).getValue(String.class);
                            Integer bestScore = userSnapshot.child(SHARED_PREFERENCES_BEST_SCORE).getValue(Integer.class);
                            String image = userSnapshot.child(SHARED_PREFERENCES_PROFILE_PICTURE).getValue(String.class);

                            String topUserData = bestScore + SPLIT_CHARACTER + username + SPLIT_CHARACTER + image;

                            if (username != null && image != null) {
                                leaderboardSet.add(topUserData);
                            }
                        }

                        sharedPreferencesUtil.writeStringSetData(
                                SHARED_PREFERENCES_FILENAME,
                                SHARED_PREFERENCES_LEADERBOARD,
                                leaderboardSet
                        );

                        userResponseCallback.onSuccessFromGettingUserPreferences();
                    } else {
                        Log.d(TAG, ERROR + task.getException());
                    }
                });
    }

    @Override
    public void getCategoriesPodium(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION)
                .child(idToken)
                .child(SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            HashSet<String> categorySet = new HashSet<>();

                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                String category = categorySnapshot.getKey();
                                Integer count = categorySnapshot.getValue(Integer.class);
                                String categoryData = category + SPLIT_CHARACTER + count;

                                if (category != null && count != null) {
                                    categorySet.add(categoryData);
                                }
                            }

                            sharedPreferencesUtil.writeStringSetData(
                                    SHARED_PREFERENCES_FILENAME,
                                    SHARED_PREFERENCES_MATCH_PLAYED_BY_CATEGORY,
                                    categorySet
                            );

                            userResponseCallback.onSuccessFromGettingUserPreferences();
                        }
                        else {
                            Log.e(TAG, ERROR_NO_DATA_CATEGORY_FOUND);
                        }
                    }
                    else {
                        Log.e(TAG, ERROR +task.getException());
                    }
                });
    }
}
