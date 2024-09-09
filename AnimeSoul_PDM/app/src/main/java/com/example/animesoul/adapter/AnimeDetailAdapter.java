package com.example.animesoul.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animesoul.R;
import com.example.animesoul.model.Anime;
import com.example.animesoul.ui.activity.AnimeDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AnimeDetailAdapter extends RecyclerView.Adapter<AnimeDetailAdapter.AnimeGenreViewHolder> {

    private List<Anime> genreAnimeList;
    private final Context context;
    private final List<Anime> favoriteList = new ArrayList<>();
    private final DatabaseReference favoritesRef;// Aggiunto il riferimento ai preferiti
    private final String titleLanguage;


    public AnimeDetailAdapter(Context context, List<Anime> genreAnimeList, DatabaseReference favoritesRef) {
        this.context = context;
        this.genreAnimeList = genreAnimeList;
        this.favoritesRef = favoritesRef; // Inizializza il riferimento ai preferiti

        SharedPreferences sharedPreferences = context.getSharedPreferences("AnimeSoulPreferences", Context.MODE_PRIVATE);
        titleLanguage = sharedPreferences.getString("title_language", "default");


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (favoritesRef != null) {
                this.favoritesRef.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        favoriteList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Anime anime = snapshot.getValue(Anime.class);  // Ottieni direttamente l'oggetto Anime
                            if (anime != null) {
                                favoriteList.add(anime);
                            }
                        }
                        notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                Log.e("AnimeDetailAdapter", "Error: Firebase reference is null.");
            }
        }
    }

    @NonNull
    @Override
    public AnimeGenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_anime, parent, false);
        return new AnimeGenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeGenreViewHolder holder, int position) {
        holder.bind(genreAnimeList.get(position));
    }

    @Override
    public int getItemCount() {
        return genreAnimeList != null ? genreAnimeList.size() : 0;
    }

    public void updateList(List<Anime> newGenreAnimeList) {
        this.genreAnimeList = newGenreAnimeList;
        notifyDataSetChanged();
    }

    class AnimeGenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView animeTitle;
        private final ImageView animeImage;
        private final ImageView favoriteIcon;
        private Anime anime;

        public AnimeGenreViewHolder(@NonNull View itemView) {
            super(itemView);
            animeTitle = itemView.findViewById(R.id.animeTitle);
            animeImage = itemView.findViewById(R.id.animeImage);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon); // Aggiunta icona preferito
            itemView.setOnClickListener(this);

            // Aggiungi un listener per il click sul cuore (preferito)
            favoriteIcon.setOnClickListener(v -> toggleFavorite());
        }

        public void bind(Anime anime) {
            this.anime = anime;
            String title = titleLanguage.equals("japanese") ? anime.getTitle_japanese() : anime.getTitle();
            animeTitle.setText(title != null && !title.isEmpty() ? title : "Title not available");

            // Carica l'immagine dell'anime
            Glide.with(itemView.getContext()).load(anime.getImages().getJpg().getImage_url()).into(animeImage);

            // Imposta l'icona del preferito in base allo stato
            favoriteIcon.setImageResource(isFavorite(anime) ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        }

        @Override
        public void onClick(View v) {
            // Quando si clicca su un anime, apri AnimeDetailActivity
            Intent intent = new Intent(context, AnimeDetailActivity.class);
            intent.putExtra("image_url", anime.getImages().getJpg().getImage_url());
            intent.putExtra("title", anime.getTitle());
            intent.putExtra("title_japanese", anime.getTitle_japanese());
            intent.putExtra("synopsis", anime.getSynopsis());
            intent.putExtra("url", anime.getTrailer().getEmbed_url());
            Gson gson = new Gson();
            String animeJson = gson.toJson(anime);
            intent.putExtra("anime", animeJson);
            context.startActivity(intent);
        }

        private void toggleFavorite() {
            if (isFavorite(anime)) {
                favoritesRef.child(String.valueOf(anime.getMal_id())).removeValue();
                favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            } else {

                favoritesRef.child(String.valueOf(anime.getMal_id())).setValue(anime);
                favoriteIcon.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        }


        private boolean isFavorite(Anime anime) {
            for (Anime favorite : favoriteList) {
                if (favorite.getMal_id() == anime.getMal_id()) {
                    return true;
                }
            }
            return false;
        }

    }
}
