
package com.example.mapsicesi.model;

import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.model.Marker;

public class Hueco {

    private String id;
    private String user;
    private double latitude;
    private double longitude;
    private long date;
    private boolean confirmado;

    public Hueco(String id, String user, double latitude, double longitude, long date, boolean confirmado) {
        this.id = id;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.confirmado = confirmado;
    }

    public Hueco() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
