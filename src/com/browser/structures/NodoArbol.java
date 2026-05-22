package com.browser.structures;

/**
 * Nodo de un árbol binario de búsqueda genérico.
 *
 * <p>Almacena un dato de tipo {@code T} (que debe implementar {@link Comparable})
 * y referencias a los hijos izquierdo y derecho. Provee un método de comparación
 * que delega en {@code T.compareTo} para mantener el invariante del BST.</p>
 *
 * <p>Esta clase tiene visibilidad de paquete; el acceso externo se hace a través
 * de {@link ArbolBinario}.</p>
 *
 * @param <T> tipo de dato almacenado; debe ser {@link Comparable} consigo mismo
 */
public class NodoArbol<T extends Comparable<T>> {

    /** Dato almacenado en este nodo. */
    protected T info;

    /** Hijo derecho del nodo (valores mayores según {@code compareTo}). */
    protected NodoArbol<T> hijoDerecha;

    /** Hijo izquierdo del nodo (valores menores según {@code compareTo}). */
    protected NodoArbol<T> hijoIzquierda;

    /**
     * Construye un nodo hoja con el dato proporcionado.
     *
     * @param dato el valor a almacenar
     */
    public NodoArbol(T dato) {
        this.info = dato;
        hijoDerecha = null;
        hijoIzquierda = null;
    }

    public T getInfo() {
        return info;
    }

    public NodoArbol<T> getHijoDerecha() {
        return hijoDerecha;
    }

    public NodoArbol<T> getHijoIzquierda() {
        return hijoIzquierda;
    }

    public void setHijoDerecha(NodoArbol<T> hijoDerecha) {
        this.hijoDerecha = hijoDerecha;
    }

    public void setHijoIzquierda(NodoArbol<T> hijoIzquierda) {
        this.hijoIzquierda = hijoIzquierda;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    /**
     * Compara el dato de este nodo con {@code otro} usando el orden natural de {@code T}.
     *
     * @param otro el valor con el que comparar
     * @return valor negativo si {@code info < otro}, cero si son iguales,
     *         positivo si {@code info > otro}
     */
    public int compararCon(T otro) {
        return this.info.compareTo(otro);
    }
}
