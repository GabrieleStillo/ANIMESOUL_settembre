package com.example.animesoul.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.animesoul.adapter.GenreAnimeAdapter;
import com.example.animesoul.model.Anime;
import com.example.animesoul.model.AnimeResponse;
import com.example.animesoul.service.JikanApi;
import com.example.animesoul.service.RetrofitInstance;
import com.example.animesoul.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private final Handler handler;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.handler = new Handler();
    }

    // Metodo per caricare gli anime per un determinato genere
    public void loadAnimesByGenre(int genreId, GenreAnimeAdapter adapter, Runnable next) {
        JikanApi apiService = RetrofitInstance.getApi();
        Call<AnimeResponse> call = apiService.getAnimeByGenre(genreId);

        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Anime> animeList = response.body().getData();
                    if (animeList != null && !animeList.isEmpty()) {
                        adapter.updateList(animeList);
                    } else {
                        Toast.makeText(getApplication(), "No anime found for genre", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    retryLoadAnimesByGenre(genreId, adapter, next);
                    return;
                }
                if (next != null) {
                    next.run();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                retryLoadAnimesByGenre(genreId, adapter, next);
            }
        });
    }

    // Metodo per ritentare il caricamento in caso di fallimento
    private void retryLoadAnimesByGenre(int genreId, GenreAnimeAdapter adapter, Runnable onComplete) {
        handler.postDelayed(() -> loadAnimesByGenre(genreId, adapter, onComplete), Constants.DELAY_MS);
    }

    // Pulisce il handler quando il ViewModel viene distrutto
    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacksAndMessages(null);
    }
}
