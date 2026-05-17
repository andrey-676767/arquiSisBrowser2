package com.browser.structures;

import java.util.EmptyStackException;
import java.util.Objects;

/**
 * Estructura de datos LIFO (Last In, First Out) implementada sobre
 * {@link ListaDoble}.
 *
 * <p>Reutiliza la infraestructura de nodos de la lista doble, restringiendo
 * el acceso al extremo frontal para comportamiento de pila. Extiende
 * {@code ListaDoble} para compartir los campos protegidos {@code cabeza},
 * {@code cola} y {@code cantidadNodos}, evitando duplicar la lógica de
 * enlazado de nodos.</p>
 *
 * <p>Uso principal en el proyecto: historial de navegación
 * hacia adelante y hacia atrás dentro de {@link dominio.entidades.Tab}.</p>
 *
 * @param <T> Tipo de dato almacenado en la pila.
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Pila<T> extends ListaDoble<T> {

    /**
     * Construye una pila vacía.
     */
    public Pila() {
        super();
    }

    /**
     * Empuja el dato en la cima de la pila (equivale a push en java.util.Stack).
     *
     * @param dato Dato a insertar en la cima.
     * @return El mismo dato insertado.
     */
    public T push(T dato) {
        super.insertarAlPrincipio(dato);
        return dato;
    }

    /**
     * Extrae y retorna el elemento en la cima de la pila.
     *
     * @return El elemento en la cima.
     * @throws EmptyStackException si la pila está vacía.
     */
    public T pop() {
        if (cantidadNodos == 0) {
            throw new EmptyStackException();
        }
        return super.remover();
    }

    /**
     * Retorna (sin extraer) el elemento en la cima de la pila.
     *
     * @return El elemento en la cima.
     * @throws EmptyStackException si la pila está vacía.
     */
    @Override
    public T peek() {
        if (cantidadNodos == 0) {
            throw new EmptyStackException();
        }
        return cabeza.getSiguiente().getDato();
    }

    /**
     * Indica si la pila no contiene elementos.
     *
     * @return {@code true} si la pila está vacía.
     */
    public boolean empty() {
        return cantidadNodos == 0;
    }

    /**
     * Busca el dato en la pila y retorna su posición basada en 1 desde la cima.
     *
     * <p>La cima tiene posición 1. Si el elemento no se encuentra, retorna -1.
     * Usa {@link Objects#equals} para la comparación.</p>
     *
     * @param dato Dato a buscar.
     * @return Posición 1-based desde la cima, o -1 si no se encuentra.
     */
    public int search(T dato) {
        Nodo<T> actual = cabeza.getSiguiente();
        int posicion = 1;
        while (actual != cola) {
            if (Objects.equals(actual.getDato(), dato)) {
                return posicion;
            }
            actual = actual.getSiguiente();
            posicion++;
        }
        return -1;
    }

    /**
     * Imprime todos los elementos de la pila usando el formato horizontal
     * de {@link ListaDoble#mostrarLista()}.
     */
    public void mostrarPila() {
        super.mostrarLista();
    }
}