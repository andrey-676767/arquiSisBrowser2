package com.browser.structures.interfaces;

/**
 * Contrato base para cualquier estructura de datos lineal genérica
 * dentro del simulador de navegador.
 *
 * <p>Define las operaciones mínimas de inserción, eliminación, búsqueda
 * e indexación que toda colección propia debe implementar, prohibiendo
 * el uso de {@code java.util.List}, {@code ArrayList} o {@code LinkedList}.</p>
 *
 * @param <T> Tipo de dato que almacena la estructura.
 * @author Refactorización Fase 1
 * @version 1.0
 */
public interface IEstructuraLineal<T> {

    /**
     * Inserta el dato al final de la estructura.
     *
     * @param dato Elemento a insertar.
     * @return {@code true} si la inserción fue exitosa.
     */
    boolean insertar(T dato);

    /**
     * Elimina y retorna el primer elemento de la estructura.
     *
     * @return El elemento removido.
     * @throws java.util.NoSuchElementException si la estructura está vacía.
     */
    T remover();

    /**
     * Elimina y retorna el elemento en la posición indicada.
     *
     * @param indice Posición base-0 del elemento a eliminar.
     * @return El elemento removido.
     * @throws IndexOutOfBoundsException si el índice es inválido.
     */
    T remover(int indice);

    /**
     * Retorna (sin eliminar) el elemento en la posición indicada.
     *
     * @param indice Posición base-0 del elemento a consultar.
     * @return El elemento en esa posición.
     * @throws IndexOutOfBoundsException si el índice es inválido.
     */
    T obtener(int indice);

    /**
     * Indica si la estructura contiene el objeto especificado,
     * usando igualdad por {@code equals}.
     *
     * @param o Objeto a buscar.
     * @return {@code true} si el objeto está presente.
     */
    boolean contiene(Object o);

    /**
     * Retorna la cantidad de elementos almacenados actualmente.
     *
     * @return Número de elementos.
     */
    int size();

    /**
     * Elimina todos los elementos de la estructura y libera los nodos.
     */
    void limpiar();
}