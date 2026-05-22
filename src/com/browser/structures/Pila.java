package com.browser.structures;

import java.util.EmptyStackException;
import java.util.Objects;

/**
 * Pila (stack) genérica con semántica LIFO (<em>Last In, First Out</em>).
 *
 * <p>Extiende {@link ListaDoble} reutilizando su infraestructura de nodos
 * doblemente enlazados. El tope de la pila corresponde siempre al primer
 * nodo de la lista (posición 0), por lo que {@code push} y {@code pop}
 * operan en O(1).</p>
 *
 * <p>Se usa principalmente en {@code Tab} para gestionar el historial
 * de navegación hacia atrás y hacia adelante.</p>
 *
 * @param <T> el tipo de dato almacenado en la pila
 */
public class Pila<T> extends ListaDoble<T> {

    /**
     * Construye una pila vacía.
     */
    public Pila() {
        super();
    }

    /**
     * Empuja un dato al tope de la pila.
     *
     * @param dato el elemento a apilar
     * @return el mismo {@code dato} que fue apilado
     */
    @Override
    public T push(T dato) {
        super.insertarAlPrincipio(dato);
        return dato;
    }

    /**
     * Extrae y retorna el elemento en el tope de la pila.
     *
     * @return el dato del tope
     * @throws EmptyStackException si la pila está vacía
     */
    @Override
    public T pop() throws EmptyStackException {
        if (cantidadNodos != 0) return super.remover();
        throw new EmptyStackException();
    }

    /**
     * Retorna el elemento en el tope sin extraerlo.
     *
     * @return el dato del tope
     * @throws EmptyStackException si la pila está vacía
     */
    @Override
    public T peek() throws EmptyStackException {
        if (cantidadNodos != 0) return cabeza.getSiguiente().getDato();
        throw new EmptyStackException();
    }

    @Override
    public T obtener() {
        return this.peek();
    }

    /**
     * Indica si la pila no contiene elementos.
     *
     * @return {@code true} si la pila está vacía
     */
    @Override
    public boolean empty() {
        return cantidadNodos == 0;
    }

    /**
     * Busca el dato en la pila y retorna su posición 1-based desde el tope.
     * La posición {@code 1} corresponde al tope.
     *
     * @param dato el elemento a buscar
     * @return posición 1-based desde el tope, o {@code -1} si no se encuentra
     */
    public int search(T dato) {
        Nodo<T> actual = cabeza;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getSiguiente();
            if (Objects.equals(actual.getDato(), dato)) {
                return j + 1;
            }
        }
        return -1;
    }

    /**
     * Imprime el contenido de la pila en formato horizontal desde el tope.
     * Delegado a {@link ListaDoble#mostrarLista()}.
     */
    public void mostrarPila() {
        super.mostrarLista();
    }
}
