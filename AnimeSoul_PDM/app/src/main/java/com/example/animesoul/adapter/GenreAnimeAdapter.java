package com.example.animesoul.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class GenreAnimeAdapter extends RecyclerView.Adapter<GenreAnimeAdapter.GenreAnimeViewHolder> {

    private List<Anime> animeList;
    private final List<Anime> favoriteList = new ArrayList<>();
    private final Context context;
    private final String titleLanguage;
    private final DatabaseReference favoritesRef;


    public GenreAnimeAdapter(@NonNull Context context, List<Anime> animeList, DatabaseReference favoritesRef) {
        this.context = context;
        this.animeList = animeList;
        this.favoritesRef = favoritesRef;

        SharedPreferences sharedPreferences = context.getSharedPreferences("AnimeSoulPreferences", Context.MODE_PRIVATE);
        titleLanguage = sharedPreferences.getString("title_language", "default");


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            this.favoritesRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favoriteList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Anime anime = snapshot.getValue(Anime.class);
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
        }
    }

    @NonNull
    @Override
    public GenreAnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime, parent, false);
        return new GenreAnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAnimeViewHolder holder, int position) {
        holder.bind(animeList.get(position));
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Anime> newAnimes) {
        animeList = newAnimes;
        notifyDataSetChanged();
    }

    class GenreAnimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView animeImage;
        private final ImageView favoriteIcon;
        private final TextView animeTitle;
        private Anime anime;

        public GenreAnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            animeImage = itemView.findViewById(R.id.animeImage);
            animeTitle = itemView.findViewById(R.id.animeTitle);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            itemView.setOnClickListener(this);
            favoriteIcon.setOnClickListener(v -> toggleFavorite());
        }

        public void bind(Anime anime) {
            this.anime = anime;


            String title = titleLanguage.equals("japanese") ? anime.getTitle_japanese() : anime.getTitle();
            animeTitle.setText(title != null && !title.isEmpty() ? title : "Title not available");

            Glide.with(context).load(anime.getImages().getJpg().getImage_url()).into(animeImage);
            favoriteIcon.setImageResource(isFavorite(anime) ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AnimeDetailActivity.class);
            intent.putExtra("image_url", anime.getImages().getJpg().getImage_url());
            intent.putExtra("title", anime.getTitle());
            intent.putExtra("title_japanese", anime.getTitle_japanese());
            intent.putExtra("synopsis", anime.getSynopsis());
            intent.putExtra("url", anime.getTrailer().getEmbed_url());// Aggiungi l'URL del trailer

            Gson gson = new Gson();
            String animeJson = gson.toJson(anime);
            intent.putExtra("anime", animeJson);

            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
