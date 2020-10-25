package com.example.mapsicesi.model;

public class User {

    private String id;
    private String nombre;
    private long date;
    private double latitude;
    private double longitude;
    private String color;

    public User() {
    }

    public User(String id, String nombre, long date, double latitude, double longitude, String color) {
        this.id = id;
        this.nombre = nombre;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
