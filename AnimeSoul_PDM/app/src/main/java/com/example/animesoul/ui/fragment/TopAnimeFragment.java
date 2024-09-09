package com.example.animesoul.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animesoul.R;
import com.example.animesoul.adapter.AnimeAdapter;
import com.example.animesoul.model.Anime;
import com.example.animesoul.model.AnimeResponse;
import com.example.animesoul.viewmodel.TopAnimeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TopAnimeFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;
    private List<Anime> animeList = new ArrayList<>();
    private DatabaseReference favoritesRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_anime, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Ottieni l'utente attualmente autenticato
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Ottieni il riferimento ai favoriti dell'utente corrente nel Realtime Database
            favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.getUid());
        }

        // Inizializza l'adapter
        animeAdapter = new AnimeAdapter(getContext(), animeList, favoritesRef);
        recyclerView.setAdapter(animeAdapter);

        // Ottieni i dati degli anime e osserva eventuali cambiamenti
        TopAnimeViewModel topAnimeViewModel = new ViewModelProvider(this).get(TopAnimeViewModel.class);
        topAnimeViewModel.getTopAnime().observe(getViewLifecycleOwner(), new Observer<AnimeResponse>() {
            @Override
            public void onChanged(AnimeResponse animeResponse) {
                if (animeResponse != null) {
                    animeList = animeResponse.getData();
                    animeAdapter.updateList(animeList);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve data, please wait", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
