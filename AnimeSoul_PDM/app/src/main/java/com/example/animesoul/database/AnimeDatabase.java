package com.example.animesoul.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.animesoul.model.Anime;
import com.example.animesoul.utils.Converters;

@Database(entities = {Anime.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AnimeDatabase extends RoomDatabase {
    private static AnimeDatabase instance;

    public abstract AnimeDao animeDao();

    public static synchronized AnimeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AnimeDatabase.class, "anime_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
