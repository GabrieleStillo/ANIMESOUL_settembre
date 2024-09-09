package com.example.animesoul.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.animesoul.model.Anime;

import java.util.List;

@Dao
public interface AnimeDao {
    @Insert
    void insert(Anime anime);

    @Delete
    void delete(Anime anime);

    @Query("SELECT * FROM anime_table")
    LiveData<List<Anime>> getAllFavoriteAnimes();
}
