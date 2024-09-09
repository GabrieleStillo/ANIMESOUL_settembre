package com.example.animesoul.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.animesoul.R;
import com.example.animesoul.adapter.ViewPagerAdapter;
import com.example.animesoul.model.Anime;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

public class AnimeDetailActivity extends AppCompatActivity {

    private WebView animeTrailerView;
    private TextView animeTitleTextView;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private String animeJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_detail);

        // Collegamenti con le view
        animeTrailerView = findViewById(R.id.animeDetailTrailer);
        animeTitleTextView = findViewById(R.id.animeDetailTitle);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Ottieni l'oggetto Anime passato dall'intent
        animeJson = getIntent().getStringExtra("anime");
        Anime anime = new Gson().fromJson(animeJson, Anime.class);

        if (anime != null) {
            // Visualizza i dati dell'anime (titolo e trailer)
            showAnimeDetails(anime);
        }

        // Imposta l'adapter per il ViewPager
        viewPagerAdapter = new ViewPagerAdapter(this, animeJson);
        viewPager.setAdapter(viewPagerAdapter);

        // Collega il TabLayout con il ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Details");
            } else {
                tab.setText("Recommended");
            }
        }).attach();
    }

    private void showAnimeDetails(Anime anime) {
        // Ottieni la preferenza di lingua del titolo
        SharedPreferences sharedPreferences = getSharedPreferences("AnimeSoulPreferences", MODE_PRIVATE);
        String titleLanguage = sharedPreferences.getString("title_language", "default");

        // Mostra il titolo in base alla preferenza della lingua
        String titleToDisplay = titleLanguage.equals("japanese") ? anime.getTitle_japanese() : anime.getTitle();
        animeTitleTextView.setText(titleToDisplay != null && !titleToDisplay.isEmpty() ? titleToDisplay : "Title not available");

        // Configura la WebView per il trailer
        WebSettings webSettings = animeTrailerView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        animeTrailerView.setWebViewClient(new WebViewClient());
        if (anime.getTrailer() != null && anime.getTrailer().getEmbed_url() != null) {
            animeTrailerView.loadUrl(anime.getTrailer().getEmbed_url());
        } else {
            // Se non c'è un trailer, carica un'immagine
            String html = "<html><body style=\"background-color:black; text-align:center;\"><img src=\"" +
                    anime.getImages().getJpg().getImage_url() + "\" width=\"auto\" height=\"100%\" style=\"object-fit:cover;\" /></body></html>";
            animeTrailerView.loadData(html, "text/html", "UTF-8");
        }
    }
}
