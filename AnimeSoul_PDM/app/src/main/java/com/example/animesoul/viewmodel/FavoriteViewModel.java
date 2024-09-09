package com.example.animesoul.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.animesoul.model.Anime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Anime>> favoriteAnimeList;
    private final DatabaseReference favoritesRef;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteAnimeList = new MutableLiveData<>();

        // Ottieni l'utente corrente
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Riferimento al nodo "favorites" nel database Firebase
            favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.getUid());

            // Inizializza il listener per ottenere i preferiti
            loadFavorites();
        } else {
            favoritesRef = null;
        }
    }

    // Metodo per recuperare i preferiti
    private void loadFavorites() {
        if (favoritesRef != null) {
            favoritesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Anime> favoriteAnimes = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Anime anime = snapshot.getValue(Anime.class);
                        if (anime != null) {
                            favoriteAnimes.add(anime);
                        }
                    }
                    favoriteAnimeList.setValue(favoriteAnimes);                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestione degli errori durante il caricamento
                }
            });
        }
    }

    // LiveData per osservare la lista degli anime preferiti
    public LiveData<List<Anime>> getFavoriteAnimeList() {
        return favoriteAnimeList;
    }
    public DatabaseReference getFavoritesRef(){
        return favoritesRef;
    }
}