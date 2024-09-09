package com.example.animesoul.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogoutViewModel extends AndroidViewModel {
    private final FirebaseAuth mAuth;
    private final DatabaseReference databaseReference;
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public LogoutViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user_preferences");
    }

    // LiveData per mostrare i messaggi Toast
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public interface OnCompleteListener<T> {
        void onComplete(boolean result);
    }

    // Funzione per salvare la lingua preferita
    public void saveLanguagePreference(String userId, String language, OnCompleteListener<Void> onCompleteListener) {
        databaseReference.child(userId).child("title_language").setValue(language)
                .addOnSuccessListener(aVoid -> {
                    onCompleteListener.onComplete(true);
                    SharedPreferences sharedPreferences = getApplication().getSharedPreferences("AnimeSoulPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("title_language", language);
                    editor.apply();
                    // Operazione completata con successo
                })
                .addOnFailureListener(e -> {
                    onCompleteListener.onComplete(false);
                });
    }

    // Funzione per inviare l'email di reset password
    public void sendResetPasswordEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            toastMessage.setValue("Reset password email sent");
                        } else {
                            toastMessage.setValue("Error sending reset password email");
                        }
                    });
        } else {
            toastMessage.setValue("No email found for the user");
        }
    }

    // Funzione per salvare le preferenze dell'utente
    public void saveUserPreferences(String userId) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("AnimeSoulPreferences", Context.MODE_PRIVATE);
        String titleLanguage = sharedPreferences.getString("title_language", "default");
        databaseReference.child(userId).setValue(new UserPreferences(titleLanguage))
                .addOnSuccessListener(aVoid -> {
                    sharedPreferences.edit().clear().apply();
                    toastMessage.setValue("Preferences saved and local data cleared");
                })
                .addOnFailureListener(e -> {
                    toastMessage.setValue("Error saving user preferences");
                });
    }


    // Funzione per il logout dell'utente
    public void logoutUser() {
        mAuth.signOut();
        toastMessage.setValue("User logged out");
    }

    // Classe helper per rappresentare le preferenze dell'utente
    public static class UserPreferences {
        public String title_language;

        public UserPreferences() {
        }

        public UserPreferences(String title_language) {
            this.title_language = title_language;
        }
    }
}
