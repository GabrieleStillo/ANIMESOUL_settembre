package com.example.animesoul.model;

public class Aired {

    private String from;  // Data di inizio
    private String to;    // Data di fine
    private String string;  // Stringa che rappresenta la data in formato leggibile

    // Getters e setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}