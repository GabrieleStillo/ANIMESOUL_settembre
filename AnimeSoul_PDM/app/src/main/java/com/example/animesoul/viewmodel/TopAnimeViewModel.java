package com.example.animesoul.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.animesoul.model.Anime;
import com.example.animesoul.model.AnimeResponse;
import com.example.animesoul.repository.TopAnimeRepository;

import java.util.List;

public class TopAnimeViewModel extends AndroidViewModel {

    private final TopAnimeRepository repository;
    private final MutableLiveData<List<Anime>> topAnimeList;

    public TopAnimeViewModel(@NonNull Application application) {
        super(application);
        repository = new TopAnimeRepository(application);
        topAnimeList = new MutableLiveData<>();
        fetchTopAnimes(); // Inizializza il caricamento degli anime
    }

    // LiveData che ritorna la lista di top anime
    public LiveData<AnimeResponse> getTopAnime() {
        return repository.getTopAnime();
    }

    // Metodo per caricare i top anime dal repository
    public void fetchTopAnimes() {
        repository.getTopAnime().observeForever(animeResponse -> {
            if (animeResponse != null && animeResponse.getData() != null) {
                topAnimeList.setValue(animeResponse.getData());
            } else {
                topAnimeList.setValue(null);// In caso di errore o lista vuota
            }
        });
    }
}
