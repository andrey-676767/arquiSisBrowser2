package com.browser.model;

import com.browser.structures.Pila;

public class Tab {
    private String[] info; //Posicion 0 para titulo posicion 1 para url
    private Pila<String[]> historialAtras;
    private Pila<String[]> historialAdelante;

    //este entero ayuda a reconocer cuando se puede adelantar y cuando se puede ir atras en el historial

    public Tab(){
        this.info = new String[]{"Nueva Pestana", ""};
        this.historialAtras = new Pila<>();
        this.historialAdelante = new Pila<>();
    }

    public void setUrl(String url) {
        String[] atras = this.info.clone();
        this.historialAtras.push(atras);
        this.info[1] = url;
        this.info[0] = url.toUpperCase();
    }

    public void adelante(){
        if (this.historialAdelante.empty()) return;
        String[] url = this.historialAdelante.pop();
        String[] info = this.info.clone();
        this.historialAtras.push(info);

        this.info[1] = url[1];
        this.info[0] = url[1].toUpperCase();
    }

    public void atras(){
        if (this.historialAtras.empty()) return;
        String[] url = this.historialAtras.pop();
        String[] info = this.info.clone();
        this.historialAdelante.push(info);

        this.info[1] = url[1];
        this.info[0] = url[1].toUpperCase();
    }

    public String getTitulo() {
        return this.info[0];
    }

    public String getUrl() {
        return this.info[1];
    }



    @Override
    public String toString(){
        return this.info[0];
    }
}
