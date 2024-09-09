package com.example.animesoul.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.animesoul.database.AnimeDao;
import com.example.animesoul.database.AnimeDatabase;
import com.example.animesoul.model.Anime;
import com.example.animesoul.model.AnimeResponse;
import com.example.animesoul.service.JikanApi;
import com.example.animesoul.service.RetrofitInstance;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimeRepository {
    private final AnimeDao animeDao;
    private final LiveData<List<Anime>> allFavoriteAnimes;
    private final ExecutorService executorService;
    private final JikanApi api;

    public AnimeRepository(Application application) {
        AnimeDatabase database = AnimeDatabase.getInstance(application);
        animeDao = database.animeDao();
        allFavoriteAnimes = animeDao.getAllFavoriteAnimes();
        executorService = Executors.newFixedThreadPool(2);
        api = RetrofitInstance.getApi();
    }

    public LiveData<AnimeResponse> getAnimes() {
        MutableLiveData<AnimeResponse> data = new MutableLiveData<>();
        api.getAnimes().enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public void insert(Anime anime) {
        executorService.execute(() -> animeDao.insert(anime));
    }

    public void delete(Anime anime) {
        executorService.execute(() -> animeDao.delete(anime));
    }

    public LiveData<List<Anime>> getAllFavoriteAnimes() {
        return allFavoriteAnimes;
    }
}
