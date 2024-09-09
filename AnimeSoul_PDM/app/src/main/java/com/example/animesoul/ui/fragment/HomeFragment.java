package com.example.animesoul.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animesoul.R;
import com.example.animesoul.adapter.GenreAnimeAdapter;
import com.example.animesoul.viewmodel.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.animesoul.utils.Constants.*;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewComedy, recyclerViewDrama, recyclerViewKids, recyclerViewFantasy,
            recyclerViewHorror, recyclerViewMystery, recyclerViewSports, recyclerViewSupernatural,
            recyclerViewAdventure, recyclerViewAction;

    private GenreAnimeAdapter comedyAdapter, dramaAdapter, kidsAdapter, fantasyAdapter, horrorAdapter,
            mysteryAdapter, sportsAdapter, supernaturalAdapter, adventureAdapter, actionAdapter;

    private FrameLayout loadingOverlay;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadingOverlay = view.findViewById(R.id.loadingOverlay);
        ImageView gifAnimation = view.findViewById(R.id.gifAnimation);
        Glide.with(this)
                .asGif()
                .load(R.drawable.tanjiro_nezuko_and_zenitsu)
                .into(gifAnimation);

        // Inizializza RecyclerView e Adapter per ciascun genere
        recyclerViewComedy = view.findViewById(R.id.recyclerViewComedy);
        recyclerViewDrama = view.findViewById(R.id.recyclerViewDrama);
        recyclerViewKids = view.findViewById(R.id.recyclerViewKids);
        recyclerViewFantasy = view.findViewById(R.id.recyclerViewFantasy);
        recyclerViewHorror = view.findViewById(R.id.recyclerViewHorror);
        recyclerViewMystery = view.findViewById(R.id.recyclerViewMystery);
        recyclerViewSports = view.findViewById(R.id.recyclerViewSports);
        recyclerViewSupernatural = view.findViewById(R.id.recyclerViewSupernatural);
        recyclerViewAdventure = view.findViewById(R.id.recyclerViewAdventure);
        recyclerViewAction = view.findViewById(R.id.recyclerViewAction);

        setupRecyclerView(recyclerViewComedy);
        setupRecyclerView(recyclerViewDrama);
        setupRecyclerView(recyclerViewKids);
        setupRecyclerView(recyclerViewFantasy);
        setupRecyclerView(recyclerViewHorror);
        setupRecyclerView(recyclerViewMystery);
        setupRecyclerView(recyclerViewSports);
        setupRecyclerView(recyclerViewSupernatural);
        setupRecyclerView(recyclerViewAdventure);
        setupRecyclerView(recyclerViewAction);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);

            // Inizializza gli adapter
            initializeAdapters(favoritesRef);

            // Inizializza il ViewModel
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

            // Mostra la schermata di caricamento
            showLoadingOverlay(true);

            // Avvia il caricamento dei generi
            loadAllGenres();
        }

        return view;
    }

    private void initializeAdapters(DatabaseReference favoritesRef) {
        comedyAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        dramaAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        kidsAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        fantasyAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        horrorAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        mysteryAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        sportsAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        supernaturalAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        adventureAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);
        actionAdapter = new GenreAnimeAdapter(requireContext(), new ArrayList<>(), favoritesRef);

        recyclerViewComedy.setAdapter(comedyAdapter);
        recyclerViewDrama.setAdapter(dramaAdapter);
        recyclerViewKids.setAdapter(kidsAdapter);
        recyclerViewFantasy.setAdapter(fantasyAdapter);
        recyclerViewHorror.setAdapter(horrorAdapter);
        recyclerViewMystery.setAdapter(mysteryAdapter);
        recyclerViewSports.setAdapter(sportsAdapter);
        recyclerViewSupernatural.setAdapter(supernaturalAdapter);
        recyclerViewAdventure.setAdapter(adventureAdapter);
        recyclerViewAction.setAdapter(actionAdapter);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
    }

    private void loadAllGenres() {
        homeViewModel.loadAnimesByGenre(GENRE_ID_COMEDY, comedyAdapter, () ->
                homeViewModel.loadAnimesByGenre(GENRE_ID_DRAMA, dramaAdapter, () ->
                        homeViewModel.loadAnimesByGenre(GENRE_ID_KIDS, kidsAdapter, () ->
                                homeViewModel.loadAnimesByGenre(GENRE_ID_FANTASY, fantasyAdapter, () ->
                                        homeViewModel.loadAnimesByGenre(GENRE_ID_HORROR, horrorAdapter, () ->
                                                homeViewModel.loadAnimesByGenre(GENRE_ID_MYSTERY, mysteryAdapter, () ->
                                                        homeViewModel.loadAnimesByGenre(GENRE_ID_SPORTS, sportsAdapter, () ->
                                                                homeViewModel.loadAnimesByGenre(GENRE_ID_SUPERNATURAL, supernaturalAdapter, () ->
                                                                        homeViewModel.loadAnimesByGenre(GENRE_ID_ADVENTURE, adventureAdapter, () ->
                                                                                homeViewModel.loadAnimesByGenre(GENRE_ID_ACTION, actionAdapter, () -> {
                                                                                    // Nascondi la schermata di caricamento
                                                                                    showLoadingOverlay(false);
                                                                                })
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private void showLoadingOverlay(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        loadingOverlay.setClickable(show); // Rende la schermata non cliccabile durante il caricamento
    }
}
