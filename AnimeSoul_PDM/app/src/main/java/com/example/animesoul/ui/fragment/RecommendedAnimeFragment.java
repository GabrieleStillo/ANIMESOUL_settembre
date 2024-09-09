package com.example.animesoul.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animesoul.R;
import com.example.animesoul.adapter.AnimeDetailAdapter;
import com.example.animesoul.model.Anime;
import com.example.animesoul.model.GenreAnimeResponse;
import com.example.animesoul.service.JikanApi;
import com.example.animesoul.service.RetrofitInstance;
import com.example.animesoul.viewmodel.RecommendedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedAnimeFragment extends Fragment {

    private RecyclerView suggestedAnimeRecyclerView;
    private AnimeDetailAdapter animeDetailAdapter;
    RecommendedViewModel recommendedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_anime, container, false);

        // Collegamenti con la RecyclerView
        suggestedAnimeRecyclerView = view.findViewById(R.id.suggestedAnimeRecyclerView);
        suggestedAnimeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);
        // Imposta l'adapter (si assume che venga passato attraverso il Bundle)
        animeDetailAdapter = new AnimeDetailAdapter(getContext(), null, favoritesRef);
        suggestedAnimeRecyclerView.setAdapter(animeDetailAdapter);
        recommendedViewModel = new ViewModelProvider(this).get(RecommendedViewModel.class);

        // Recupera l'anime corrente
        String animeJson = getArguments().getString("anime");
        Anime anime = new Gson().fromJson(animeJson, Anime.class);

        if (anime != null && anime.getGenres() != null && !anime.getGenres().isEmpty()) {
            recommendedViewModel.fetchGenreAnime(anime.getGenres().get(0).getMal_id(), animeJson, animeDetailAdapter);
        }

        return view;
    }
}
