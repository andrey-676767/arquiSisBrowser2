package com.browser.structures;

import java.util.EmptyStackException;
import java.util.Objects;
//Estructura LIFO - Last In First Out
public class Pila<T> extends ListaDoble<T> {

    public Pila() {
        super();
    }

    public T push(T dato){
        super.insertarAlPrincipio(dato);
        return dato;
    }

    public T pop() throws EmptyStackException {
        if (cantidadNodos != 0) return super.remover();
        throw new EmptyStackException();
    }

    @Override
    public T peek() throws EmptyStackException{
        if (cantidadNodos != 0) return cabeza.getSiguiente().getDato();
        throw new EmptyStackException();
    }

    public boolean empty(){return cantidadNodos == 0;}

    public int search(T dato){
        Nodo<T> actual = cabeza;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getSiguiente();
            if (Objects.equals(actual.getDato(), dato)) {
                return j + 1;
            }
        }
        return -1;
    }

    public void mostrarPila(){
        super.mostrarLista();
    }
}
