package com.example.animesoul.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animesoul.R;
import com.example.animesoul.model.Anime;
import com.google.gson.Gson;

public class AnimeDetailFragment extends Fragment {

    private TextView animeSynopsisTextView, airedDateTextView, genreTextView, producersTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_detail, container, false);

        // Collegamenti con le view
        animeSynopsisTextView = view.findViewById(R.id.animeDetailSynopsis);
        airedDateTextView = view.findViewById(R.id.airedDate);
        genreTextView = view.findViewById(R.id.genreText);
        producersTextView = view.findViewById(R.id.producersText);

        // Recupera i dati dell'anime (tramite Bundle o ViewModel)
        assert getArguments() != null;
        String animeJson = getArguments().getString("anime");
        Anime anime = new Gson().fromJson(animeJson, Anime.class);
        if (anime != null) {
            showAnimeDetails(anime);
        }

        return view;
    }

    private void showAnimeDetails(Anime anime) {
        // Mostra la sinossi
        animeSynopsisTextView.setText(anime.getSynopsis() != null ? anime.getSynopsis() : "No synopsis available");

        // Mostra la data di trasmissione
        if (anime.getAired() != null && anime.getAired().getString() != null) {
            airedDateTextView.setText(anime.getAired().getString());
        } else {
            airedDateTextView.setText("No aired date available");
        }

        // Mostra i generi
        if (anime.getGenres() != null && !anime.getGenres().isEmpty()) {
            StringBuilder genres = new StringBuilder();
            for (int i = 0; i < anime.getGenres().size(); i++) {
                genres.append(anime.getGenres().get(i).getName());
                if (i != anime.getGenres().size() - 1) {
                    genres.append(", ");
                }
            }
            genreTextView.setText(genres.toString());
        } else {
            genreTextView.setText("No genres available");
        }

        // Mostra i produttori
        if (anime.getProducers() != null && !anime.getProducers().isEmpty()) {
            StringBuilder producers = new StringBuilder();
            for (int i = 0; i < anime.getProducers().size(); i++) {
                producers.append(anime.getProducers().get(i).getName());
                if (i != anime.getProducers().size() - 1) {
                    producers.append(", ");
                }
            }
            producersTextView.setText(producers.toString());
        } else {
            producersTextView.setText("No producers available");
        }
    }
}
