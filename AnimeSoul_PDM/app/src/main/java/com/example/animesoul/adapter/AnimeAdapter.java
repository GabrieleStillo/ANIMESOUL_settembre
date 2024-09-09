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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private List<Anime> animes;
    private List<Anime> favoriteList = new ArrayList<>();
    private final Context context;
    private final String titleLanguage;
    private final DatabaseReference favoritesRef;

    public AnimeAdapter(Context context, List<Anime> animes, DatabaseReference favoritesRef) {
        this.context = context;
        this.animes = animes;
        this.favoritesRef = favoritesRef;

        // Recupera la preferenza di lingua
        SharedPreferences sharedPreferences = context.getSharedPreferences("AnimeSoulPreferences", Context.MODE_PRIVATE);
        titleLanguage = sharedPreferences.getString("title_language", "default");

        // Ascolta i cambiamenti ai dati dei preferiti nel database
        if (favoritesRef != null) {
            favoritesRef.addValueEventListener(new ValueEventListener() {
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
                    // Gestisci eventuali errori
                }
            });
        }
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_anime, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        holder.bind(animes.get(position));
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Anime> newAnimes) {
        animes = newAnimes;
        notifyDataSetChanged();
    }

    class AnimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView animeTitle;
        private final ImageView animeImage;
        private final ImageView favoriteIcon;
        private Anime anime;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            animeTitle = itemView.findViewById(R.id.animeTitle);
            animeImage = itemView.findViewById(R.id.animeImage);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            itemView.setOnClickListener(this);
            favoriteIcon.setOnClickListener(v -> toggleFavorite());
        }

        public void bind(Anime anime) {
            this.anime = anime;

            // Mostra il titolo in base alla preferenza di lingua
            String title = titleLanguage.equals("japanese") ? anime.getTitle_japanese() : anime.getTitle();
            animeTitle.setText(title);

            Glide.with(itemView.getContext()).load(anime.getImages().getJpg().getImage_url()).into(animeImage);
            favoriteIcon.setImageResource(isFavorite(anime) ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AnimeDetailActivity.class);
            intent.putExtra("image_url", anime.getImages().getJpg().getImage_url());
            intent.putExtra("title", anime.getTitle());  // Titolo predefinito
            intent.putExtra("title_japanese", anime.getTitle_japanese());  // Titolo giapponese
            intent.putExtra("synopsis", anime.getSynopsis());
            intent.putExtra("url", anime.getTrailer() != null ? anime.getTrailer().getEmbed_url() : null);

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
