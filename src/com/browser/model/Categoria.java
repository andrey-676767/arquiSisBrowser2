package com.browser.model;

import com.browser.structures.ListaDoble;

public class Categoria implements Comparable<Categoria> {
    private String nombre;
    public ListaDoble<Marcador> contenido;

    public static final String[] abcd = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public Categoria(){
        this.nombre = "Por defecto";
        this.contenido = new ListaDoble<>();
    }

    public Categoria(String nombre, Marcador incial){
        this.nombre = nombre;
        this.contenido = new ListaDoble<>();
        contenido.insertarAlPrincipio(incial);
    }

    @Override
    public int compareTo(Categoria o) {
        int oInt = buscarIndex(o.nombre);
        int thisInt = buscarIndex(this.nombre);

        if (thisInt > oInt) return 1;
        else if (thisInt == oInt) return 0;
        return -1;
    }

    private static int buscarIndex(String categoria){
        String letra = categoria.substring(0, 1).toUpperCase();
        int index = 0;
        for (String s : abcd) {
            if (s.equals(letra)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void borrarMarcador(String nombre){
        for (int i = 0; i < this.contenido.size(); i++) {
            Marcador aux = this.contenido.obtener(i);
            if (aux.getTitulo().equals(nombre)) {
                this.contenido.remover(i);
            }
        }
    }

    public Marcador pop(String nombre){
        for (int i = 0; i < this.contenido.size(); i++) {
            Marcador aux = this.contenido.obtener(i);
            if (aux.getTitulo().equals(nombre)) {
                return this.contenido.remover(i);
            }
        }
        return null;
    }

    public Marcador obtener(String nombre){
        for (int i = 0; i < this.contenido.size(); i++) {
            Marcador aux = this.contenido.obtener(i);
            if (aux.getTitulo().equals(nombre)) {
                return aux;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return this.nombre + "\n" + contenido.toString();
    }

}
