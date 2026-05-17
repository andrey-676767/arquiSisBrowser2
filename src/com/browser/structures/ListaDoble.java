package com.browser.structures;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementación concreta de una lista doblemente enlazada genérica.
 *
 * <p>Utiliza dos nodos centinela (cabeza y cola) para simplificar las
 * operaciones de inserción y eliminación en los extremos, evitando
 * comprobaciones especiales de bordes. Todos los nodos de datos se
 * ubican entre ambos centinelas.</p>
 *
 * <p>Esta clase reemplaza en su totalidad el uso de {@code java.util.List},
 * {@code ArrayList} y {@code LinkedList} dentro del proyecto. Ninguna
 * capa del sistema debe importar esas clases.</p>
 *
 * <p>Es la única colección base autorizada. {@link Pila} extiende esta
 * clase para reutilizar su infraestructura de nodos.</p>
 *
 * @param <T> Tipo de dato que almacena la lista.
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class ListaDoble<T> implements IListaDoble<T> {

    /** Centinela de cabeza — nunca contiene dato real. */
    protected Nodo<T> cabeza;

    /** Centinela de cola — nunca contiene dato real. */
    protected Nodo<T> cola;

    /** Cantidad de nodos con datos reales actualmente en la lista. */
    protected int cantidadNodos;

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Construye una lista vacía, inicializando los centinelas vinculados
     * entre sí: cabeza.siguiente → cola y cola.anterior → cabeza.
     */
    public ListaDoble() {
        cabeza = new Nodo<>(null);
        cola = new Nodo<>(null);
        cabeza.setSiguiente(cola);
        cola.setAnterior(cabeza);
        cantidadNodos = 0;
    }

    // ── Inserción ────────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Inserta el nuevo nodo inmediatamente después del centinela de cabeza,
     * convirtiéndolo en el primer elemento visible de la lista.</p>
     */
    @Override
    public void insertarAlPrincipio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        Nodo<T> primerReal = cabeza.getSiguiente();
        nuevo.setSiguiente(primerReal);
        nuevo.setAnterior(cabeza);
        cabeza.setSiguiente(nuevo);
        primerReal.setAnterior(nuevo);
        cantidadNodos++;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Inserta el nuevo nodo inmediatamente antes del centinela de cola,
     * convirtiéndolo en el último elemento visible de la lista.</p>
     */
    @Override
    public void insertarAlFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        Nodo<T> penultimo = cola.getAnterior();
        nuevo.setAnterior(penultimo);
        nuevo.setSiguiente(cola);
        penultimo.setSiguiente(nuevo);
        cola.setAnterior(nuevo);
        cantidadNodos++;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Navega hasta la posición indicada contando desde el centinela de
     * cabeza e inserta el nuevo nodo antes del nodo que estaba en esa
     * posición. El nodo desplazado pasa al índice {@code indice + 1}.</p>
     *
     * @throws IndexOutOfBoundsException si {@code indice < 0} o
     *                                   {@code indice >= size()}.
     */
    @Override
    public void insertarEn(int indice, T dato) {
        if (indice < 0 || indice >= cantidadNodos) {
            throw new IndexOutOfBoundsException(
                    "Índice " + indice + " inválido para lista de tamaño " + cantidadNodos);
        }
        Nodo<T> nuevo = new Nodo<>(dato);
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        Nodo<T> siguiente = actual.getSiguiente();
        nuevo.setAnterior(actual);
        nuevo.setSiguiente(siguiente);
        siguiente.setAnterior(nuevo);
        actual.setSiguiente(nuevo);
        cantidadNodos++;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Equivale a {@code addLast} → delega en {@link #insertarAlFinal}.</p>
     */
    @Override
    public boolean insertar(T dato) {
        int antes = cantidadNodos;
        insertarAlFinal(dato);
        return cantidadNodos == antes + 1;
    }

    // ── Eliminación ──────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Elimina el nodo inmediatamente siguiente al centinela de cabeza
     * (primer elemento real). Libera todas las referencias del nodo para
     * ayudar al GC.</p>
     *
     * @throws NoSuchElementException si la lista está vacía.
     */
    @Override
    public T remover() {
        if (cantidadNodos == 0) {
            throw new NoSuchElementException("No se puede remover de una lista vacía.");
        }
        Nodo<T> objetivo = cabeza.getSiguiente();
        T dato = objetivo.getDato();
        Nodo<T> siguiente = objetivo.getSiguiente();
        cabeza.setSiguiente(siguiente);
        siguiente.setAnterior(cabeza);
        objetivo.setSiguiente(null);
        objetivo.setAnterior(null);
        objetivo.setDato(null);
        cantidadNodos--;
        return dato;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Navega hasta la posición indicada y desvincula el nodo encontrado,
     * reconectando sus vecinos entre sí.</p>
     *
     * @throws IndexOutOfBoundsException si el índice es negativo o mayor o
     *                                   igual al tamaño actual.
     */
    @Override
    public T remover(int indice) {
        if (indice < 0 || indice >= cantidadNodos) {
            throw new IndexOutOfBoundsException(
                    "Índice " + indice + " inválido para lista de tamaño " + cantidadNodos);
        }
        Nodo<T> anterior = cabeza;
        for (int i = 0; i < indice; i++) {
            anterior = anterior.getSiguiente();
        }
        Nodo<T> objetivo = anterior.getSiguiente();
        T dato = objetivo.getDato();
        Nodo<T> siguiente = objetivo.getSiguiente();
        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);
        objetivo.setSiguiente(null);
        objetivo.setAnterior(null);
        cantidadNodos--;
        return dato;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Elimina el nodo inmediatamente anterior al centinela de cola
     * (último elemento real). Si la lista está vacía retorna {@code null}
     * sin lanzar excepción, para compatibilidad con el uso en el
     * administrador de descargas.</p>
     */
    @Override
    public T removerUltimo() {
        if (cantidadNodos == 0) {
            return null;
        }
        Nodo<T> objetivo = cola.getAnterior();
        T dato = objetivo.getDato();
        Nodo<T> penultimo = objetivo.getAnterior();
        penultimo.setSiguiente(cola);
        cola.setAnterior(penultimo);
        objetivo.setSiguiente(null);
        objetivo.setAnterior(null);
        objetivo.setDato(null);
        cantidadNodos--;
        return dato;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Busca por igualdad de referencia ({@code ==}) la primera coincidencia
     * del objeto y la desvincula. Compatibilidad con el código original que
     * usaba comparación de referencia en {@code removerObjeto}.</p>
     */
    @Override
    public boolean removerObjeto(Object o) {
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != cola) {
            if (actual.getDato() == o) {
                Nodo<T> ant = actual.getAnterior();
                Nodo<T> sig = actual.getSiguiente();
                ant.setSiguiente(sig);
                sig.setAnterior(ant);
                actual.setSiguiente(null);
                actual.setAnterior(null);
                actual.setDato(null);
                cantidadNodos--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Busca la primera ocurrencia usando {@link Objects#equals} y la elimina.</p>
     */
    @Override
    public boolean removerPrimeraOcurrencia(T dato) {
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != cola) {
            if (Objects.equals(actual.getDato(), dato)) {
                Nodo<T> ant = actual.getAnterior();
                Nodo<T> sig = actual.getSiguiente();
                ant.setSiguiente(sig);
                sig.setAnterior(ant);
                actual.setSiguiente(null);
                actual.setAnterior(null);
                actual.setDato(null);
                cantidadNodos--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Recorre la lista desde el final hacia el principio buscando la
     * última ocurrencia por igualdad de referencia.</p>
     */
    @Override
    public boolean removerUltimaOcurrencia(T dato) {
        Nodo<T> actual = cola.getAnterior();
        while (actual != cabeza) {
            if (actual.getDato() == dato) {
                Nodo<T> ant = actual.getAnterior();
                Nodo<T> sig = actual.getSiguiente();
                ant.setSiguiente(sig);
                sig.setAnterior(ant);
                actual.setSiguiente(null);
                actual.setAnterior(null);
                actual.setDato(null);
                cantidadNodos--;
                return true;
            }
            actual = actual.getAnterior();
        }
        return false;
    }

    // ── Consulta ─────────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= cantidadNodos) {
            throw new IndexOutOfBoundsException(
                    "Índice " + indice + " inválido para lista de tamaño " + cantidadNodos);
        }
        Nodo<T> actual = cabeza.getSiguiente();
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException si la lista está vacía.
     */
    @Override
    public T obtenerPrimero() {
        if (cantidadNodos == 0) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return cabeza.getSiguiente().getDato();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException si la lista está vacía.
     */
    @Override
    public T obtenerUltimo() {
        if (cantidadNodos == 0) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return cola.getAnterior().getDato();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Usa {@link Objects#equals} para la comparación.</p>
     */
    @Override
    public boolean contiene(Object o) {
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != cola) {
            if (Objects.equals(actual.getDato(), o)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Retorna {@code -1} si el objeto no se encuentra.</p>
     */
    @Override
    public int indiceDe(Object o) {
        Nodo<T> actual = cabeza.getSiguiente();
        int indice = 0;
        while (actual != cola) {
            if (Objects.equals(actual.getDato(), o)) {
                return indice;
            }
            actual = actual.getSiguiente();
            indice++;
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Retorna el primer elemento sin eliminarlo, o {@code null} si la lista
     * está vacía. Semántica de cola (Queue.peek).</p>
     */
    @Override
    public T peek() {
        if (cantidadNodos == 0) return null;
        return cabeza.getSiguiente().getDato();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Elimina y retorna el primer elemento, o {@code null} si está vacía.
     * Semántica de cola (Queue.poll).</p>
     */
    @Override
    public T poll() {
        if (cantidadNodos == 0) return null;
        return remover();
    }

    /**
     * Retorna (sin eliminar) el primer elemento.
     * Alias explícito para uso como cola/deque.
     *
     * @return El primer elemento.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T elemento() {
        if (cantidadNodos == 0) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return cabeza.getSiguiente().getDato();
    }

    // ── Modificación ─────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    public T reemplazar(int indice, T dato) {
        if (indice < 0 || indice >= cantidadNodos) {
            throw new IndexOutOfBoundsException(
                    "Índice " + indice + " inválido para lista de tamaño " + cantidadNodos);
        }
        Nodo<T> actual = cabeza.getSiguiente();
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        T anterior = actual.getDato();
        actual.setDato(dato);
        return anterior;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Desvincula todos los nodos internos y restablece el vínculo
     * cabeza↔cola, liberando referencias para el recolector de basura.</p>
     */
    @Override
    public void limpiar() {
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != cola) {
            Nodo<T> siguiente = actual.getSiguiente();
            actual.setDato(null);
            actual.setAnterior(null);
            actual.setSiguiente(null);
            actual = siguiente;
        }
        cabeza.setSiguiente(cola);
        cola.setAnterior(cabeza);
        cantidadNodos = 0;
    }

    // ── Tamaño ───────────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return cantidadNodos;
    }

    // ── Visualización ────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Formato de salida: {@code |elemento - indice|}. Si la lista está vacía
     * imprime el mensaje "Lista vacía".</p>
     */
    @Override
    public void mostrarLista() {
        if (cantidadNodos == 0) {
            System.out.println("Lista vacía");
            return;
        }
        System.out.println("Elementos: " + cantidadNodos);
        Nodo<T> actual = cabeza.getSiguiente();
        int i = 0;
        while (actual != cola) {
            System.out.print("|" + actual.getDato().toString() + " - " + i + "|");
            actual = actual.getSiguiente();
            i++;
        }
        System.out.println();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Imprime cada elemento en su propia línea con su índice separado
     * por " - ".</p>
     */
    @Override
    public void mostrarVertical() {
        if (cantidadNodos == 0) {
            System.out.println("Lista vacía");
            return;
        }
        System.out.println("Elementos: " + cantidadNodos);
        Nodo<T> actual = cabeza.getSiguiente();
        int i = 0;
        while (actual != cola) {
            System.out.println(actual.getDato().toString() + " - " + i);
            actual = actual.getSiguiente();
            i++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        Object[] arreglo = new Object[cantidadNodos];
        Nodo<T> actual = cabeza.getSiguiente();
        for (int i = 0; i < cantidadNodos; i++) {
            arreglo[i] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return arreglo;
    }

    /**
     * Retorna una representación textual de la lista en formato
     * {@code (elemento - indice)(elemento - indice)...}, o {@code null}
     * si la lista está vacía.
     *
     * @return Cadena con los elementos de la lista, o {@code null}.
     */
    @Override
    public String toString() {
        if (cantidadNodos == 0) return null;
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = cabeza.getSiguiente();
        int i = 0;
        while (actual != cola) {
            sb.append("(").append(actual.getDato().toString()).append(" - ").append(i).append(")");
            actual = actual.getSiguiente();
            i++;
        }
        return sb.toString();
    }

    // ── Métodos de conveniencia (compatibilidad con Deque) ───────────────────

    /**
     * Inserta el dato al final. Equivale a {@link #insertar}.
     *
     * @param dato Elemento a insertar.
     * @return {@code true} si la inserción fue exitosa.
     */
    public boolean offer(T dato) {
        return insertar(dato);
    }

    /**
     * Inserta el dato al principio.
     *
     * @param dato Elemento a insertar.
     * @return {@code true} si la inserción fue exitosa.
     */
    public boolean offerFirst(T dato) {
        int antes = cantidadNodos;
        insertarAlPrincipio(dato);
        return cantidadNodos == antes + 1;
    }

    /**
     * Inserta el dato al final. Alias de {@link #insertar}.
     *
     * @param dato Elemento a insertar.
     * @return {@code true} si la inserción fue exitosa.
     */
    public boolean offerLast(T dato) {
        return insertar(dato);
    }
}