package com.browser.structures;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.browser.structures.interfaces.IEstructuraDeDatos;

/**
 * Lista doblemente enlazada genérica que implementa {@link IEstructuraDeDatos}.
 *
 * <p>Utiliza nodos centinela de cabeza y cola para simplificar las operaciones
 * en los extremos y evitar comprobaciones especiales de bordes. Ofrece una API
 * completa de inserción, eliminación, búsqueda y recorrido, análoga a
 * {@code java.util.LinkedList}.</p>
 *
 * <p>Esta clase está diseñada para ser extendida por {@link Pila}, que reusa
 * su infraestructura de nodos añadiendo semántica LIFO.</p>
 *
 * @param <T> el tipo de dato almacenado en la lista
 */
public class ListaDoble<T> implements IEstructuraDeDatos<T> {

    /*Push */

    /** Nodo centinela de cabeza (no almacena dato real). */
    protected Nodo<T> cabeza;

    /** Nodo centinela de cola (no almacena dato real). */
    protected Nodo<T> cola;

    /** Número de nodos con dato real actualmente en la lista. */
    protected int cantidadNodos;

    /**
     * Construye una lista vacía con los nodos centinela ya enlazados.
     */
    public ListaDoble() {
        cabeza = new Nodo<>(null);
        cola = new Nodo<>(null);
        cabeza.setSiguiente(cola);
        cola.setAnterior(cabeza);
    }

    // -------------------------------------------------------------------------
    // Inserción
    // -------------------------------------------------------------------------

    /**
     * Inserta un dato como primer elemento de la lista.
     * Equivalente a {@code addFirst(E)} de {@code LinkedList}.
     *
     * @param dato el elemento a insertar al inicio
     */
    public void insertarAlPrincipio(T dato) {
        Nodo<T> nodo = new Nodo<>(dato);
        Nodo<T> siguiente = cabeza.getSiguiente();
        nodo.setSiguiente(siguiente);
        nodo.setAnterior(cabeza);
        cabeza.setSiguiente(nodo);
        siguiente.setAnterior(nodo);
        cantidadNodos++;
    }

    /*Pequeña sección mia */

    @Override
    public T push(T dato) {
        this.insertarAlPrincipio(dato);
        return dato;
    }

    @Override
    public T pop() {
        return this.remover();
    }

    @Override
    public boolean empty() {
        return this.cantidadNodos == 0;
    }

    @Override
    public int cantidad() {
        return this.cantidadNodos;
    }

    @Override
    public T obtener() {
        return this.obtenerPrimero();
    }
    /**
     * Inserta un dato en la posición {@code index} desplazando hacia adelante
     * al elemento actualmente en esa posición.
     * Equivalente a {@code add(int, E)} de {@code LinkedList}.
     *
     * @param index posición (basada en 0) donde se insertará el dato
     * @param dato  el elemento a insertar
     * @throws IndexOutOfBoundsException si {@code index} está fuera del rango {@code [0, size)}
     */
    public void insertarEn(int index, T dato) throws IndexOutOfBoundsException {
        if (index >= cantidadNodos || index < 0) {
            throw new IndexOutOfBoundsException("Indice " + index + " invalido");
        }
        Nodo<T> nodo = new Nodo<>(dato);
        Nodo<T> actual = cabeza;
        for (int i = 0; i < index; i++) {
            actual = actual.getSiguiente();
        }
        Nodo<T> siguiente = actual.getSiguiente();
        nodo.setAnterior(actual);
        nodo.setSiguiente(siguiente);
        siguiente.setAnterior(nodo);
        actual.setSiguiente(nodo);
        cantidadNodos++;
    }

    /**
     * Inserta un dato como último elemento de la lista.
     * Equivalente a {@code addLast(E)} de {@code LinkedList}.
     *
     * @param dato el elemento a insertar al final
     */
    public void insertarAlFinal(T dato) {
        Nodo<T> nodo = new Nodo<>(dato);
        Nodo<T> actual = cola.getAnterior();
        nodo.setAnterior(actual);
        nodo.setSiguiente(cola);
        actual.setSiguiente(nodo);
        cola.setAnterior(nodo);
        cantidadNodos++;
    }

    /**
     * Inserta un dato al final de la lista.
     * Implementa {@link IEstructuraDeDatos#insertar(Object)}.
     * Equivalente a {@code add(E)} de {@code LinkedList}.
     *
     * @param dato el elemento a insertar
     * @return {@code true} si la lista creció en exactamente un elemento
     */
    @Override
    public boolean insertar(T dato) {
        int antes = cantidadNodos;
        insertarAlFinal(dato);
        return cantidadNodos == antes + 1;
    }

    // -------------------------------------------------------------------------
    // Eliminación
    // -------------------------------------------------------------------------

    /**
     * Elimina todos los elementos de la lista, liberando las referencias
     * de cada nodo para facilitar la recolección de basura.
     * Equivalente a {@code clear()} de {@code LinkedList}.
     */
    public void limpiar() {
        Nodo<T> actual = cola;
        Nodo<T> anterior = actual.getAnterior();
        for (int i = 0; i < cantidadNodos; i++) {
            actual.setDato(null);
            actual.setSiguiente(null);
            actual.setAnterior(null);
            actual = anterior;
            anterior = actual.getAnterior();
        }
        cola.setAnterior(cabeza);
        cabeza.setSiguiente(cola);
        cantidadNodos = 0;
    }

    /**
     * Elimina y retorna el primer elemento de la lista.
     * Equivalente a {@code remove()} de {@code LinkedList}.
     *
     * @return el dato del primer nodo
     * @throws NoSuchElementException si la lista está vacía
     */
    public T remover() throws NoSuchElementException {
        if (cantidadNodos != 0) {
            T info = cabeza.getSiguiente().getDato();
            Nodo<T> nodo = cabeza.getSiguiente();
            Nodo<T> actual = nodo.getSiguiente();
            actual.setAnterior(cabeza);
            cabeza.setSiguiente(actual);
            nodo.setSiguiente(null);
            nodo.setDato(null);
            nodo.setAnterior(null);
            cantidadNodos--;
            return info;
        }
        throw new NoSuchElementException("Lista vacia");
    }

    /**
     * Elimina y retorna el elemento en la posición {@code index}.
     * Implementa {@link IEstructuraDeDatos#remover(int)}.
     * Equivalente a {@code remove(int)} de {@code LinkedList}.
     *
     * @param index índice (basado en 0) del elemento a eliminar
     * @return el dato del nodo eliminado
     * @throws IndexOutOfBoundsException si {@code index} está fuera del rango {@code [0, size)}
     */
    @Override
    public T remover(int index) throws IndexOutOfBoundsException {
        if (index >= cantidadNodos || index < 0) {
            throw new IndexOutOfBoundsException("Indice " + index + " invalido");
        }
        Nodo<T> anterior = cabeza;
        for (int i = 0; i < index; i++) {
            anterior = anterior.getSiguiente();
        }
        Nodo<T> actual = anterior.getSiguiente();
        T info = actual.getDato();
        Nodo<T> siguiente = actual.getSiguiente();
        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);
        actual.setSiguiente(null);
        actual.setAnterior(null);
        cantidadNodos--;
        return info;
    }

    /**
     * Elimina la primera ocurrencia del objeto {@code o} en la lista.
     * Equivalente a {@code remove(Object)} de {@code LinkedList}.
     *
     * @param o el objeto a eliminar (comparación por referencia)
     * @return {@code true} si se encontró y eliminó, {@code false} si no estaba
     */
    public boolean removerObjeto(Object o) {
        Nodo<T> actual = cabeza;
        boolean encontrado = false;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getSiguiente();
            if (actual.getDato() == o) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) return false;
        Nodo<T> anterior = actual.getAnterior();
        Nodo<T> siguiente = actual.getSiguiente();
        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);
        actual.setAnterior(null);
        actual.setSiguiente(null);
        cantidadNodos--;
        return true;
    }

    /**
     * Elimina la primera ocurrencia de {@code dato}.
     * Equivalente a {@code removeFirstOccurrence(E)} de {@code LinkedList}.
     *
     * @param dato el elemento a eliminar
     * @return {@code true} si se eliminó, {@code false} en caso contrario
     */
    public boolean removerPrimeraOcurrencia(T dato) {
        return removerObjeto(dato);
    }

    /**
     * Elimina la última ocurrencia de {@code dato} recorriendo la lista
     * desde la cola hacia la cabeza.
     *
     * @param dato el elemento a eliminar
     * @return {@code true} si se eliminó, {@code false} en caso contrario
     */
    public boolean removerUltimaOcurrencia(T dato) {
        Nodo<T> actual = cola.getAnterior();
        boolean encontrado = false;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getAnterior();
            if (actual.getDato() == dato) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) return false;
        Nodo<T> anterior = actual.getAnterior();
        Nodo<T> siguiente = actual.getSiguiente();
        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);
        actual.setAnterior(null);
        actual.setSiguiente(null);
        actual.setDato(null);
        cantidadNodos--;
        return true;
    }

    /**
     * Elimina y retorna el último nodo de la lista.
     *
     * @return el nodo eliminado, o {@code null} si la lista está vacía
     */
    public Nodo<T> borrarUltimo() {
        if (cantidadNodos == 0) {
            System.out.println("No hay nodos restantes");
            return null;
        }
        Nodo<T> nodo = cola.getAnterior();
        Nodo<T> anterior = nodo.getAnterior();
        anterior.setSiguiente(cola);
        cola.setAnterior(anterior);
        nodo.setSiguiente(null);
        nodo.setAnterior(null);
        cantidadNodos--;
        return nodo;
    }

    // -------------------------------------------------------------------------
    // Consulta / Acceso
    // -------------------------------------------------------------------------

    /**
     * Verifica si la estructura contiene el objeto dado usando {@link Objects#equals}.
     * Implementa {@link IEstructuraDeDatos#contiene(Object)}.
     *
     * @param o el objeto a buscar
     * @return {@code true} si existe al menos una ocurrencia
     */
    @Override
    public boolean contiene(Object o) {
        Nodo<T> actual = cabeza.getSiguiente();
        int i = 0;
        while (actual.getSiguiente() != null && !Objects.equals(actual.getDato(), o)) {
            actual = actual.getSiguiente();
            i++;
            if (actual.getSiguiente() != null && Objects.equals(actual.getDato(), o)) {
                return true;
            }
        }
        return i == 0 && Objects.equals(actual.getDato(), o);
    }

    /**
     * Retorna el dato del primer nodo sin eliminarlo.
     * Equivalente a {@code element()} de {@code LinkedList}.
     *
     * @return el primer elemento
     * @throws NoSuchElementException si la lista está vacía
     */
    public T elemento() throws NoSuchElementException {
        if (cantidadNodos != 0) return cabeza.getSiguiente().getDato();
        throw new NoSuchElementException("La lista esta vacia");
    }

    /**
     * Retorna el elemento en la posición {@code index} sin eliminarlo.
     * Implementa {@link IEstructuraDeDatos#obtener(int)}.
     * Equivalente a {@code get(int)} de {@code LinkedList}.
     *
     * @param index índice (basado en 0) del elemento a consultar
     * @return el dato en la posición {@code index}
     * @throws IndexOutOfBoundsException si {@code index} está fuera del rango {@code [0, size)}
     */
    @Override
    public T obtener(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= cantidadNodos) {
            throw new IndexOutOfBoundsException("Indice " + index + " invalido para " + cantidadNodos);
        }
        Nodo<T> actual = cabeza;
        for (int j = 0; j <= index; j++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     * Retorna el primer elemento sin eliminarlo.
     * Equivalente a {@code getFirst()} de {@code LinkedList}.
     *
     * @return el dato del primer nodo
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerPrimero() throws NoSuchElementException {
        if (cantidadNodos != 0) return cabeza.getSiguiente().getDato();
        throw new NoSuchElementException("Lista vacia");
    }

    /**
     * Retorna el último elemento sin eliminarlo.
     * Equivalente a {@code getLast()} de {@code LinkedList}.
     *
     * @return el dato del último nodo
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerUltimo() throws NoSuchElementException {
        if (cantidadNodos != 0) return cola.getAnterior().getDato();
        throw new NoSuchElementException("Lista vacia");
    }

    /**
     * Reemplaza el elemento en la posición {@code index} por {@code dato}.
     * Equivalente a {@code set(int, E)} de {@code LinkedList}.
     *
     * @param index índice (basado en 0) del elemento a reemplazar
     * @param dato  el nuevo valor
     * @return el dato anterior en esa posición
     * @throws IndexOutOfBoundsException si {@code index} está fuera de rango
     */
    public T reemplazar(int index, T dato) throws IndexOutOfBoundsException {
        if (index < 0 || index >= cantidadNodos) {
            throw new IndexOutOfBoundsException("Indice " + index + " invalido");
        }
        if (index == 0) {
            T obj = cabeza.getSiguiente().getDato();
            cabeza.getSiguiente().setDato(dato);
            return obj;
        }
        Nodo<T> actual = cabeza.getSiguiente();
        for (int i = 0; i < index; i++) {
            actual = actual.getSiguiente();
        }
        T elemento = actual.getDato();
        actual.setDato(dato);
        return elemento;
    }

    /**
     * Retorna el índice de la primera ocurrencia del objeto {@code o}.
     * Equivalente a {@code indexOf(Object)} de {@code LinkedList}.
     *
     * @param o el objeto a buscar
     * @return índice de la primera ocurrencia, o {@code -1} si no se encuentra
     */
    public int indiceDe(Object o) {
        Nodo<T> actual = cabeza;
        for (int i = 0; i < cantidadNodos; i++) {
            actual = actual.getSiguiente();
            if (Objects.equals(actual.getDato(), o)) {
                return i;
            }
        }
        System.out.println("El elemento no se encuentra en la lista");
        return -1;
    }

    // -------------------------------------------------------------------------
    // API de cola / deque
    // -------------------------------------------------------------------------

    /**
     * Inserta el dato al final de la lista (semántica de cola).
     *
     * @param dato el elemento a insertar
     * @return {@code true} si la inserción fue exitosa
     */
    public boolean offer(T dato) {
        return insertar(dato);
    }

    /**
     * Inserta el dato al inicio de la lista.
     *
     * @param dato el elemento a insertar
     * @return {@code true} si la inserción fue exitosa
     */
    public boolean offerFirst(T dato) {
        int antes = cantidadNodos;
        insertarAlPrincipio(dato);
        return cantidadNodos == antes + 1;
    }

    /**
     * Inserta el dato al final de la lista.
     *
     * @param dato el elemento a insertar
     * @return {@code true} si la inserción fue exitosa
     */
    public boolean offerLast(T dato) {
        return insertar(dato);
    }

    /**
     * Elimina y retorna el primer elemento, o {@code null} si la lista está vacía.
     *
     * @return el primer elemento, o {@code null}
     */
    public T poll() {
        return cantidadNodos != 0 ? remover() : null;
    }

    /**
     * Retorna el primer elemento sin eliminarlo, o {@code null} si la lista está vacía.
     *
     * @return el primer elemento, o {@code null}
     */
    public T peek() {
        return cantidadNodos != 0 ? elemento() : null;
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

    /**
     * Retorna todos los elementos de la lista como un arreglo de {@code Object}.
     *
     * @return arreglo con los elementos en orden de inserción
     */
    public Object[] toArray() {
        Nodo<T> actual = cabeza.getSiguiente();
        Object[] array = new Object[cantidadNodos];
        for (int i = 0; i < cantidadNodos; i++) {
            array[i] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return array;
    }

    /**
     * Retorna el número de elementos con dato real actualmente en la lista.
     *
     * @return tamaño de la lista
     */
    public int size() {
        return cantidadNodos;
    }

    /**
     * Imprime cada elemento junto a su índice en formato horizontal.
     * Útil para depuración en consola.
     */
    public void mostrarLista() {
        if (size() != 0) {
            System.out.println("Elementos: " + size());
            Nodo<T> actual = cabeza.getSiguiente();
            int i = 0;
            while (actual.getSiguiente() != null) {
                System.out.print("|" + actual.getDato().toString() + " - " + i + "|");
                actual = actual.getSiguiente();
                i++;
            }
            return;
        }
        System.out.println("Lista vacia");
    }

    /**
     * Imprime cada elemento junto a su índice en formato vertical (un elemento por línea).
     */
    public void mostrarVertical() {
        if (size() != 0) {
            System.out.println("Elementos: " + size());
            Nodo<T> actual = cabeza.getSiguiente();
            int i = 0;
            while (actual.getSiguiente() != null) {
                System.out.println(actual.getDato().toString() + " - " + i);
                actual = actual.getSiguiente();
                i++;
            }
            return;
        }
        System.out.println("Lista vacia");
    }

    /**
     * Retorna una representación en cadena de todos los elementos con sus índices.
     *
     * @return cadena con el formato {@code (dato - índice)(dato - índice)...},
     *         o {@code null} si la lista está vacía
     */
    @Override
    public String toString() {
        if (size() != 0) {
            StringBuilder info = new StringBuilder();
            Nodo<T> actual = cabeza.getSiguiente();
            int i = 0;
            while (actual.getSiguiente() != null) {
                info.append("(").append(actual.getDato().toString())
                        .append(" - ").append(i).append(")");
                actual = actual.getSiguiente();
                i++;
            }
            return info.toString();
        }
        return null;
    }
}
