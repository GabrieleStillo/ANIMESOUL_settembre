package com.example.animesoul.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animesoul.R;
import com.example.animesoul.adapter.AnimeAdapter;
import com.example.animesoul.model.Anime;
import com.example.animesoul.viewmodel.FavoriteViewModel;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;
    private ImageView emptyStateImageView;
    private TextView listEmpty;
    private FavoriteViewModel favoriteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        emptyStateImageView = view.findViewById(R.id.emptyStateImageView);
        listEmpty = view.findViewById(R.id.listEmpty);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Inizializza il ViewModel
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        // Osserva la lista dei preferiti dal ViewModel
        favoriteViewModel.getFavoriteAnimeList().observe(getViewLifecycleOwner(), new Observer<List<Anime>>() {
            @Override
            public void onChanged(List<Anime> favoriteAnimes) {
                if (favoriteAnimes != null && !favoriteAnimes.isEmpty()) {
                    // Nascondi la schermata vuota e mostra la lista
                    recyclerView.setVisibility(View.VISIBLE);
                    listEmpty.setVisibility(View.GONE);
                    emptyStateImageView.setVisibility(View.GONE);

                    // Inizializza l'adapter con la lista di preferiti e collegalo alla RecyclerView
                    animeAdapter = new AnimeAdapter(requireContext(), favoriteAnimes, favoriteViewModel.getFavoritesRef());
                    recyclerView.setAdapter(animeAdapter);
                } else {
                    // Mostra un messaggio di stato vuoto
                    recyclerView.setVisibility(View.GONE);
                    listEmpty.setVisibility(View.VISIBLE);
                    emptyStateImageView.setVisibility(View.VISIBLE);
                    Glide.with(requireContext()).load(R.drawable.snow_demonslayer).into(emptyStateImageView);
                }
            }
        });

        return view;
    }
}