package com.example.animesoul.model;

public class Genres {

    private int mal_id;    // ID del genere
    private String type;   // Tipo (es. "anime")
    private String name;   // Nome del genere (es. "Action", "Sci-Fi")
    private String url;    // URL relativo al genere

    // Getters e Setters
    public int getMal_id() {
        return mal_id;
    }

    public void setMal_id(int mal_id) {
        this.mal_id = mal_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
