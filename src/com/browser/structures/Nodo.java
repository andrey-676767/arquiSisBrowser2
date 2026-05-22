package com.browser.structures;

/**
 * Nodo genérico utilizado como unidad básica de enlace en las estructuras
 * de datos doblemente enlazadas ({@link ListaDoble}, {@link Pila}).
 *
 * <p>Cada nodo almacena un dato de tipo {@code T} y referencias al nodo
 * anterior y al siguiente dentro de la cadena, permitiendo recorrido
 * bidireccional.</p>
 *
 * @param <T> el tipo de dato almacenado en el nodo
 */
public class Nodo<T> {

    /** Dato almacenado en este nodo. */
    private T dato;

    /** Referencia al nodo siguiente en la estructura. */
    private Nodo<T> siguiente;

    /** Referencia al nodo anterior en la estructura. */
    private Nodo<T> anterior;

    /**
     * Construye un nodo con el dato proporcionado.
     * Las referencias {@code siguiente} y {@code anterior} se inicializan en {@code null}.
     *
     * @param dato el valor a almacenar en el nodo
     */
    public Nodo(T dato) {
        this.dato = dato;
        siguiente = null;
        anterior = null;
    }

    /**
     * Retorna la referencia al nodo anterior.
     *
     * @return el nodo anterior, o {@code null} si no existe
     */
    public Nodo<T> getAnterior() {
        return anterior;
    }

    /**
     * Establece la referencia al nodo anterior.
     *
     * @param anterior el nodo que precede a este
     */
    public void setAnterior(Nodo<T> anterior) {
        this.anterior = anterior;
    }

    /**
     * Retorna la referencia al nodo siguiente.
     *
     * @return el nodo siguiente, o {@code null} si no existe
     */
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    /**
     * Establece la referencia al nodo siguiente.
     *
     * @param siguiente el nodo que sucede a este
     */
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    /**
     * Retorna el dato almacenado en el nodo.
     *
     * @return el dato de tipo {@code T}
     */
    public T getDato() {
        return dato;
    }

    /**
     * Reemplaza el dato almacenado en el nodo.
     *
     * @param dato el nuevo valor a almacenar
     */
    public void setDato(T dato) {
        this.dato = dato;
    }
}
