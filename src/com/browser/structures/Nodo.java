package com.browser.structures;

/**
 * Nodo interno de la lista doblemente enlazada.
 *
 * <p>Cada nodo almacena un dato genérico y dos referencias: al nodo
 * anterior y al nodo siguiente, formando la cadena bidireccional que
 * da soporte a {@link ListaDoble}. Los nodos centinela de cabeza y cola
 * se crean con dato {@code null}.</p>
 *
 * <p>Clase de infraestructura — no debe ser instanciada directamente
 * desde capas superiores (dominio, servicio, controlador).</p>
 *
 * @param <T> Tipo del dato almacenado en este nodo.
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Nodo<T> {

    /** Dato almacenado en este nodo. */
    private T dato;

    /** Referencia al nodo siguiente en la cadena. */
    private Nodo<T> siguiente;

    /** Referencia al nodo anterior en la cadena. */
    private Nodo<T> anterior;

    /**
     * Construye un nodo con el dato proporcionado.
     * Las referencias siguiente y anterior quedan en {@code null}.
     *
     * @param dato Valor a almacenar (puede ser {@code null} para centinelas).
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }

    /**
     * Retorna el dato almacenado en este nodo.
     *
     * @return El dato del nodo.
     */
    public T getDato() {
        return dato;
    }

    /**
     * Establece el dato almacenado en este nodo.
     *
     * @param dato Nuevo valor a almacenar.
     */
    public void setDato(T dato) {
        this.dato = dato;
    }

    /**
     * Retorna la referencia al nodo siguiente.
     *
     * @return El nodo siguiente, o {@code null} si no existe.
     */
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    /**
     * Establece la referencia al nodo siguiente.
     *
     * @param siguiente Nodo que sigue a este en la cadena.
     */
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    /**
     * Retorna la referencia al nodo anterior.
     *
     * @return El nodo anterior, o {@code null} si no existe.
     */
    public Nodo<T> getAnterior() {
        return anterior;
    }

    /**
     * Establece la referencia al nodo anterior.
     *
     * @param anterior Nodo que precede a este en la cadena.
     */
    public void setAnterior(Nodo<T> anterior) {
        this.anterior = anterior;
    }
}