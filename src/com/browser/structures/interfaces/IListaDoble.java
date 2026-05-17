package infraestructura.estructuras.interfaces;

/**
 * Interfaz genérica que define el contrato completo de una lista
 * doblemente enlazada para el simulador de navegador.
 *
 * <p>Extiende {@link IEstructuraLineal} con operaciones propias de
 * una lista doble: inserción en posiciones extremas e intermedias,
 * eliminación por objeto, reemplazo, indexación y recorridos de
 * visualización. Esta interfaz es la base del patrón Strategy
 * implícito descrito en el diagrama de arquitectura objetivo.</p>
 *
 * <p><b>Restricción absoluta:</b> ninguna implementación puede delegar
 * en {@code java.util.List}, {@code ArrayList} o {@code LinkedList}.</p>
 *
 * @param <T> Tipo de dato almacenado en la lista.
 * @author Refactorización Fase 1
 * @version 1.0
 */
public interface IListaDoble<T> extends IEstructuraLineal<T> {

    // ── Inserción ────────────────────────────────────────────────────────────

    /**
     * Inserta el dato como primer elemento de la lista (equivale a addFirst).
     *
     * @param dato Elemento a insertar al frente.
     */
    void insertarAlPrincipio(T dato);

    /**
     * Inserta el dato como último elemento de la lista (equivale a addLast).
     *
     * @param dato Elemento a insertar al final.
     */
    void insertarAlFinal(T dato);

    /**
     * Inserta el dato en la posición indicada, desplazando el elemento
     * que ocupaba ese lugar hacia la derecha (equivale a add(int, E)).
     *
     * @param indice Posición base-0 donde se insertará el elemento.
     * @param dato   Elemento a insertar.
     * @throws IndexOutOfBoundsException si el índice es negativo o mayor
     *                                   que el tamaño actual.
     */
    void insertarEn(int indice, T dato);

    // ── Eliminación ──────────────────────────────────────────────────────────

    /**
     * Elimina y retorna el último elemento de la lista.
     *
     * @return El último elemento, o {@code null} si la lista está vacía.
     */
    T removerUltimo();

    /**
     * Elimina la primera ocurrencia del objeto usando igualdad por referencia
     * o por {@code equals}.
     *
     * @param o Objeto a eliminar.
     * @return {@code true} si se encontró y eliminó el elemento.
     */
    boolean removerObjeto(Object o);

    /**
     * Elimina la primera ocurrencia del dato usando {@code equals}.
     *
     * @param dato Dato a eliminar.
     * @return {@code true} si se encontró y eliminó.
     */
    boolean removerPrimeraOcurrencia(T dato);

    /**
     * Elimina la última ocurrencia del dato usando igualdad por referencia.
     *
     * @param dato Dato a eliminar.
     * @return {@code true} si se encontró y eliminó.
     */
    boolean removerUltimaOcurrencia(T dato);

    // ── Consulta ─────────────────────────────────────────────────────────────

    /**
     * Retorna (sin eliminar) el primer elemento de la lista.
     *
     * @return El primer elemento.
     * @throws java.util.NoSuchElementException si la lista está vacía.
     */
    T obtenerPrimero();

    /**
     * Retorna (sin eliminar) el último elemento de la lista.
     *
     * @return El último elemento.
     * @throws java.util.NoSuchElementException si la lista está vacía.
     */
    T obtenerUltimo();

    /**
     * Retorna el índice de la primera ocurrencia del objeto.
     *
     * @param o Objeto a buscar.
     * @return Índice base-0, o {@code -1} si no se encuentra.
     */
    int indiceDe(Object o);

    /**
     * Retorna (sin eliminar) el primer elemento, o {@code null} si está vacía.
     * Equivale a peek en una cola.
     *
     * @return El primer elemento o {@code null}.
     */
    T peek();

    /**
     * Retorna y elimina el primer elemento, o {@code null} si está vacía.
     * Equivale a poll en una cola.
     *
     * @return El primer elemento eliminado o {@code null}.
     */
    T poll();

    // ── Modificación ─────────────────────────────────────────────────────────

    /**
     * Reemplaza el elemento en la posición indicada por el nuevo dato,
     * retornando el valor anterior (equivale a set(int, E)).
     *
     * @param indice Posición base-0 del elemento a reemplazar.
     * @param dato   Nuevo valor.
     * @return El valor anterior en esa posición.
     * @throws IndexOutOfBoundsException si el índice es inválido.
     */
    T reemplazar(int indice, T dato);

    // ── Visualización ────────────────────────────────────────────────────────

    /**
     * Imprime todos los elementos en línea con su índice, en formato
     * {@code |elemento - indice|}.
     */
    void mostrarLista();

    /**
     * Imprime todos los elementos en formato vertical, uno por línea.
     */
    void mostrarVertical();

    /**
     * Convierte la lista en un arreglo de {@code Object[]} con los
     * datos en orden de inserción.
     *
     * @return Arreglo con los elementos de la lista.
     */
    Object[] toArray();
}