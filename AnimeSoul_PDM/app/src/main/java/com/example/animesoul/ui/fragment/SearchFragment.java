package com.example.animesoul.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animesoul.R;
import com.example.animesoul.adapter.AnimeDetailAdapter;
import com.example.animesoul.model.Anime;
import com.example.animesoul.viewmodel.SearchViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private AnimeDetailAdapter searchResultsAdapter;
    private ImageView emptyStateImageView;
    private TextView searchListEmpty;
    private static final String TAG = "SearchFragment";
    private DatabaseReference favoritesRef;
    private SearchViewModel searchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);
        emptyStateImageView = view.findViewById(R.id.emptyStateImageView1);
        searchListEmpty = view.findViewById(R.id.searchlistEmpty);
        Glide.with(requireContext()).load(R.drawable.aniyuki_gif_demon_slayer_38).into(emptyStateImageView);

        recyclerViewSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Inizializza il ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(currentUser.getUid());
        } else {
            favoritesRef = null;
        }

        searchResultsAdapter = new AnimeDetailAdapter(getContext(), new ArrayList<>(), favoritesRef);
        recyclerViewSearchResults.setAdapter(searchResultsAdapter);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Search submitted: " + query);
                searchViewModel.performSearch(query.toLowerCase().trim()); // Chiamata alla ViewModel
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "Search text changed: " + newText);
                if (newText.isEmpty()) {
                    searchResultsAdapter.updateList(new ArrayList<>()); // Pulisci i risultati
                    emptyStateImageView.setVisibility(View.VISIBLE); // Mostra la GIF quando non c'è testo di ricerca
                    searchListEmpty.setVisibility(View.VISIBLE);
                } else {
                    searchViewModel.performSearch(newText.toLowerCase().trim());
                }
                return false;
            }
        });

        // Osserva i risultati della ricerca
        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), animeList -> {
            if (animeList != null && !animeList.isEmpty()) {
                searchResultsAdapter.updateList(animeList);
                emptyStateImageView.setVisibility(View.GONE); // Nascondi la GIF quando ci sono risultati
                searchListEmpty.setVisibility(View.GONE);
                recyclerViewSearchResults.setVisibility(View.VISIBLE);  // Mostra i risultati

            } else {
                Toast.makeText(getContext(), "No anime found", Toast.LENGTH_SHORT).show();
                emptyStateImageView.setVisibility(View.VISIBLE); // Mostra la GIF quando non ci sono risultati
                searchListEmpty.setVisibility(View.VISIBLE);

            }
        });

        return view;
    }
}
