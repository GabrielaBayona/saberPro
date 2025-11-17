package com.saberpro.app.models;

public class Incentivo {
    private String descripcion;
    private String nivel;
    private String color;
    private int minPuntaje;
    private int maxPuntaje;

    public Incentivo(String descripcion, String nivel, String color, int minPuntaje, int maxPuntaje) {
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.color = color;
        this.minPuntaje = minPuntaje;
        this.maxPuntaje = maxPuntaje;
    }

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMinPuntaje() {
        return minPuntaje;
    }

    public void setMinPuntaje(int minPuntaje) {
        this.minPuntaje = minPuntaje;
    }

    public int getMaxPuntaje() {
        return maxPuntaje;
    }

    public void setMaxPuntaje(int maxPuntaje) {
        this.maxPuntaje = maxPuntaje;
    }
}