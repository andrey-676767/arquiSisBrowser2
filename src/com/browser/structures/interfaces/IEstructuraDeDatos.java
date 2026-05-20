package com.browser.structures.interfaces;

/**
 * Interfaz genérica que define el contrato base para las estructuras de datos
 * del simulador de navegación.
 *
 * <p>Permite desacoplar las implementaciones concretas ({@link ListaDoble}, {@link Pila})
 * de los módulos que las consumen, facilitando el cumplimiento del principio
 * Open/Closed y la sustitución de implementaciones sin afectar al resto del sistema.</p>
 *
 * @param <T> el tipo de dato almacenado en la estructura
 */
public interface IEstructuraDeDatos<T> {

    /**
     * Inserta un dato en la estructura.
     *
     * @param dato el elemento a insertar
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso contrario
     */
    boolean insertar(T dato);

    /**
     * Elimina y retorna el elemento en la posición indicada.
     *
     * @param index índice basado en 0 del elemento a remover
     * @return el elemento removido
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    T remover(int index);

    /**
     * Verifica si la estructura contiene el objeto dado.
     *
     * @param o el objeto a buscar
     * @return {@code true} si el objeto existe en la estructura, {@code false} en caso contrario
     */
    boolean contiene(Object o);

    /**
     * Retorna el elemento en la posición indicada sin modificar la estructura.
     *
     * @param index índice basado en 0 del elemento a consultar
     * @return el elemento en la posición {@code index}
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    T obtener(int index);

    T obtener();

    T push(T dato);

    T pop();

    boolean empty();

    int cantidad();
}
