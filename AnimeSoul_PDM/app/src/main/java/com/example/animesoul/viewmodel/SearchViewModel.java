package com.example.animesoul.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.animesoul.model.Anime;
import com.example.animesoul.model.AnimeResponse;
import com.example.animesoul.service.JikanApi;
import com.example.animesoul.service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";
    private final JikanApi apiService;
    private final MutableLiveData<List<Anime>> searchResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitInstance.getApi();
        searchResults = new MutableLiveData<>();
    }

    public MutableLiveData<List<Anime>> getSearchResults() {
        return searchResults;
    }

    public void performSearch(String query) {
        Log.d(TAG, "Performing search for: " + query);

        Call<AnimeResponse> call = apiService.getAnimeSearch(query);
        call.enqueue(new Callback<AnimeResponse>() {
            @Override
            public void onResponse(@NonNull Call<AnimeResponse> call, @NonNull Response<AnimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Anime> animeList = response.body().getData();
                    searchResults.setValue(animeList);
                } else {
                    Log.d(TAG, "No anime found for query: " + query);
                    searchResults.setValue(null);  // Quando non trovi nessun risultato
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnimeResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error during search: " + t.getMessage());
                searchResults.setValue(null);  // Imposta null in caso di errore
            }
        });
    }
}
