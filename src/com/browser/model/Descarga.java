package com.browser.model;

import java.util.Random;

public class Descarga {
    private final String nombre;
    private final int peso;
    private static final Random random = new Random();

    public Descarga(String nombre){
        this.nombre = nombre;
        this.peso = random.nextInt(1000);
    }

    @Override
    public String toString(){
        return this.nombre + " - " + peso + "mb";
    }

    public String getNombre(){
        return this.nombre;
    }
}
