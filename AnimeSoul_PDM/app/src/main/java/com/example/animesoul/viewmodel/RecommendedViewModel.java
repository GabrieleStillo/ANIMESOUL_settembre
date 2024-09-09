package com.example.animesoul.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.animesoul.adapter.AnimeDetailAdapter;
import com.example.animesoul.model.Anime;
import com.example.animesoul.model.GenreAnimeResponse;
import com.example.animesoul.service.JikanApi;
import com.example.animesoul.service.RetrofitInstance;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedViewModel extends AndroidViewModel {

    private final JikanApi apiService;

    public RecommendedViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitInstance.getApi();
    }

    // Metodo per caricare anime di un genere specifico
    public void fetchGenreAnime(int genreId, String animeJson, AnimeDetailAdapter animeDetailAdapter) {
        Call<GenreAnimeResponse> call = apiService.getAnimeByGenre1(genreId);
        call.enqueue(new Callback<GenreAnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenreAnimeResponse> call, @NonNull Response<GenreAnimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Anime> genreAnimeList = response.body().getData();
                    animeDetailAdapter.updateList(genreAnimeList);
                    // Converti l'anime corrente da JSON
                    Anime currentAnime = new Gson().fromJson(animeJson, Anime.class);

                    // Rimuovi l'anime corrente dalla lista dei suggeriti
                    if (currentAnime != null) {
                        genreAnimeList.removeIf(anime -> anime.getMal_id() == currentAnime.getMal_id());
                    }

                    // Aggiorna l'adapter con la lista filtrata
                    animeDetailAdapter.updateList(genreAnimeList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenreAnimeResponse> call, @NonNull Throwable t) {
                // Gestisci l'errore (puoi gestire i messaggi di errore qui)
            }
        });
    }
}
