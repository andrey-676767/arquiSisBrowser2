package com.browser.structures;

import java.util.Arrays;

public class ColaDoble<T> {
    private int numElementos;
    private int frente;
    private int ultimo;
    private final Object[] cola;

    public ColaDoble(int num){
        this.cola = new Object[num];
        this.ultimo = -1;
        this.frente = 0;
        this.numElementos = 0;
    }

    public int getNumElementos(){
        return this.numElementos;
    }

    public boolean vacia(){
        return this.numElementos == 0;
    }

    public int getFrente() {
        return frente;
    }

    public int getUltimo() {
        return ultimo;
    }

    public boolean insertarFrente(T elemento) {
        if (ultimo == cola.length && frente == 0 || ultimo == frente - 1 && frente != 0){
            System.out.println("ERROR: Cola llena");
            return false;
        }
        else {
            if (frente == 0) {
                frente = cola.length - 1;
            } else {
                frente = (frente - 1) % cola.length;
            }
            cola[frente] = elemento;
            numElementos++;
        }
        return true;
    }

    public boolean insertarUltimo(T elemento) {
        if (ultimo == cola.length && frente == 0 || ultimo == frente - 1 && frente != 0){
            System.out.println("ERROR: Cola llena");
            return false;
        }
        else {
            if (ultimo == cola.length - 1){
                ultimo = 0;
            } else {
                ultimo = ultimo + 1;
            }
            cola[ultimo] = elemento;
            numElementos++;
        }
        return true;
    }

    public Object removerFrente(){
        if (ultimo != -1){
            Object elemento = cola[frente];
            cola[frente] = null;
            if (ultimo == frente){
                frente = 0;
                ultimo = -1;
            } else if (frente == cola.length - 1) {
                frente = 0;
            }
            else {
                frente++;
            }
            numElementos--;
            return elemento;
        } else System.out.println("ERROR: Cola vacia");
        return null;
    }

    public Object removerFinal(){
        if (ultimo != -1){ //Confirma que no este vacia la cola
            Object elemento = cola[ultimo];
            if (ultimo == frente){
                frente = 0;
                ultimo = -1;
            } else if (ultimo == 0) {
                ultimo = cola.length - 1;
            }
            else {
                ultimo--;
            }
            numElementos--;
            return elemento;
        } else System.out.println("ERROR: Cola vacia");
        return null;
    }

    /*private void redimensionar(){
        //No se como es que se tiene que implementar (preguntarle al profesor)
    }*/

    @Override
    public String toString(){
        return Arrays.toString(cola);
    }
}
