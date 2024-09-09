package com.example.animesoul.model;

import com.example.animesoul.model.Anime;

import java.util.List;


public class AnimeResponse {
    private List<Anime> data;

    public List<Anime> getData() {
        return data;
    }

    public void setData(List<Anime> data) {
        this.data = data;
    }
}
