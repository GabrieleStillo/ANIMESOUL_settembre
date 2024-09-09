package com.example.animesoul.utils;

import androidx.room.TypeConverter;

import com.example.animesoul.model.Aired;
import com.example.animesoul.model.Genres;
import com.example.animesoul.model.Images;
import com.example.animesoul.model.Jpg;
import com.example.animesoul.model.Producers;
import com.example.animesoul.model.Trailer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Images fromString(String value) {
        Type type = new TypeToken<Images>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromImages(Images images) {
        Gson gson = new Gson();
        return gson.toJson(images);
    }

    @TypeConverter
    public static Jpg fromStringJpg(String value) {
        Type type = new TypeToken<Jpg>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromJpg(Jpg jpg) {
        Gson gson = new Gson();
        return gson.toJson(jpg);
    }

    @TypeConverter
    public static String fromTrailer(Trailer trailer) {
        if (trailer == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Trailer>() {
        }.getType();
        return gson.toJson(trailer, type);
    }

    @TypeConverter
    public static Trailer toTrailer(String trailerString) {
        if (trailerString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Trailer>() {
        }.getType();
        return gson.fromJson(trailerString, type);
    }

    @TypeConverter
    public static String fromGenres(List<Genres> genres) {
        if (genres == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Genres>>() {
        }.getType();
        return gson.toJson(genres, type);
    }

    @TypeConverter
    public static String fromAired(Aired aired) {
        if (aired == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Aired>() {
        }.getType();
        return gson.toJson(aired, type);
    }

    @TypeConverter
    public static Aired toAired(String airedString) {
        if (airedString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Aired>() {
        }.getType();
        return gson.fromJson(airedString, type);
    }

    @TypeConverter
    public static List<Genres> toGenres(String genresString) {
        if (genresString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Genres>>() {
        }.getType();
        return gson.fromJson(genresString, type);
    }

    @TypeConverter
    public static String fromProducers(List<Producers> producers) {
        if (producers == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Producers>>() {
        }.getType();
        return gson.toJson(producers, type);
    }

    @TypeConverter
    public static List<Producers> toProducers(String producersString) {
        if (producersString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Producers>>() {
        }.getType();
        return gson.fromJson(producersString, type);
    }

}
