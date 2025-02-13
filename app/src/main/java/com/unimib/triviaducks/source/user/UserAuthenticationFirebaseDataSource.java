package com.unimib.triviaducks.source.user;

import static com.unimib.triviaducks.util.Constants.ERROR_INVALID_CREDENTIALS;
import static com.unimib.triviaducks.util.Constants.ERROR_INVALID_USER;
import static com.unimib.triviaducks.util.Constants.ERROR_UNEXPECTED;
import static com.unimib.triviaducks.util.Constants.ERROR_USER_COLLISION;
import static com.unimib.triviaducks.util.Constants.ERROR_WEAK_PASSWORD;
import static com.unimib.triviaducks.util.Constants.WARNING_SIGN_IN_WITH_CREDENTIAL_FAILURE;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.unimib.triviaducks.model.User;

public class UserAuthenticationFirebaseDataSource extends BaseUserAuthenticationRemoteDataSource {
    private static final String TAG = UserAuthenticationFirebaseDataSource.class.getSimpleName();
    private final FirebaseAuth firebaseAuth;
    public UserAuthenticationFirebaseDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return null;
        } else {
            return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid());
        }
    }
    @Override
    public void logout() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    userResponseCallback.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();
    }
    @Override
    public void signUp(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    userResponseCallback.onSuccessFromAuthentication(
                            new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid())
                    );
                } else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }
    @Override
    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    userResponseCallback.onSuccessFromAuthentication(
                            new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid())
                    );
                } else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }
    @Override
    public void signInWithGoogle(String idToken) {
        if (idToken !=  null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        userResponseCallback.onSuccessFromAuthentication(
                                new User(firebaseUser.getDisplayName(),
                                        firebaseUser.getEmail(),
                                        firebaseUser.getUid()
                                )
                        );
                    } else {
                        userResponseCallback.onFailureFromAuthentication(
                                getErrorMessage(task.getException()));
                    }
                } else {
                    Log.w(TAG, WARNING_SIGN_IN_WITH_CREDENTIAL_FAILURE, task.getException());
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            });
        }
    }
    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return ERROR_WEAK_PASSWORD;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return ERROR_INVALID_CREDENTIALS;
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return ERROR_INVALID_USER;
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return ERROR_USER_COLLISION;
        }
        return ERROR_UNEXPECTED;
    }


}