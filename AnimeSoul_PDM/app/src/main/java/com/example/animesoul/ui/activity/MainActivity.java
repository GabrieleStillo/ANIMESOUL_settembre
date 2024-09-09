package com.example.animesoul.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.animesoul.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Rimuovi il titolo predefinito
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Rendere trasparente la status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorites, R.id.nav_top_anime, R.id.nav_logout)
                .setDrawerLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Aggiorna il titolo del Toolbar in base al fragment
            TextView toolbarTitle = findViewById(R.id.toolbar_title);
            if (toolbarTitle != null) {
                toolbarTitle.setText(destination.getLabel());
            }
        });

        updateNavHeader();
        checkUserAuthentication();
    }

    private void checkUserAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private void updateNavHeader() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            NavigationView navigationView = findViewById(R.id.navigation_view);
            View headerView = navigationView.getHeaderView(0);
            TextView userName = headerView.findViewById(R.id.user_name);
            TextView userEmail = headerView.findViewById(R.id.user_email);
            ImageView userImage = headerView.findViewById(R.id.user_profile_image);

            userName.setText(currentUser.getDisplayName());
            userEmail.setText(currentUser.getEmail());

            Uri photoUrl = currentUser.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).into(userImage);
            } else {
                userImage.setImageResource(R.mipmap.animesoul_luncher_round);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
        if (!handled) {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navController.navigate(R.id.nav_home);
            } else if (id == R.id.nav_search) {
                navController.navigate(R.id.nav_search);
            } else if (id == R.id.nav_favorites) {
                navController.navigate(R.id.nav_favorites);
            } else if (id == R.id.nav_top_anime) {
                navController.navigate(R.id.nav_top_anime);
            } else if (id == R.id.nav_logout) {
                navController.navigate(R.id.nav_logout);
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
