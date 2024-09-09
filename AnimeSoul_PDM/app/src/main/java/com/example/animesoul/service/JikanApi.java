package com.example.animesoul.service;

import com.example.animesoul.model.AnimeResponse;
import com.example.animesoul.model.GenreAnimeResponse;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;

public interface JikanApi {
    @GET("v4/anime")
    Call<AnimeResponse> getAnimes();

    @GET("v4/top/anime")
    Call<AnimeResponse> getTopAnime();

    @GET("v4/anime")
    Call<AnimeResponse> getAnimeSearch(@Query("q") String query);

    @GET("v4/anime")
    Call<AnimeResponse> getAnimeByGenre
            (@Query("genres") int genreId);

    @GET("v4/anime")
    Call<GenreAnimeResponse> getAnimeByGenre1
            (@Query("genres") int genreId);
}




