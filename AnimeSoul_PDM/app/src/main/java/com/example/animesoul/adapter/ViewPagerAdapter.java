package com.example.animesoul.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.animesoul.ui.fragment.AnimeDetailFragment;
import com.example.animesoul.ui.fragment.RecommendedAnimeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final String animeJson;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String animeJson) {
        super(fragmentActivity);
        this.animeJson = animeJson;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString("anime", animeJson); // Assicurati che il Bundle non sia null

        if (position == 0) {
            fragment = new AnimeDetailFragment();
        } else {
            fragment = new RecommendedAnimeFragment();
        }
        fragment.setArguments(bundle);  // Passa il bundle al fragment
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;  // Due pagine: "Details" e "Recommended"
    }
}
