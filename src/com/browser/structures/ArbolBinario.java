package com.browser.structures;

/**
 * Nodo interno del árbol binario de búsqueda genérico.
 *
 * <p>Almacena un dato comparable y mantiene referencias a sus dos
 * hijos (izquierdo y derecho). La comparación entre nodos se delega
 * al método {@link Comparable#compareTo} del dato almacenado.</p>
 *
 * <p>Clase de infraestructura — no debe ser instanciada directamente
 * desde capas superiores al paquete de estructuras.</p>
 *
 * @param <T> Tipo del dato, que debe implementar {@link Comparable}.
 * @author Refactorización Fase 1
 * @version 1.0
 */
class NodoArbol<T extends Comparable<T>> {

    /** Dato almacenado en este nodo del árbol. */
    protected T info;

    /** Hijo derecho (valores mayores que este nodo). */
    protected NodoArbol<T> hijoDerecha;

    /** Hijo izquierdo (valores menores que este nodo). */
    protected NodoArbol<T> hijoIzquierda;

    /**
     * Construye un nodo hoja con el dato proporcionado y sin hijos.
     *
     * @param dato Valor a almacenar en el nodo.
     */
    public NodoArbol(T dato) {
        this.info = dato;
        this.hijoDerecha = null;
        this.hijoIzquierda = null;
    }

    /**
     * Compara el dato de este nodo con otro valor usando
     * {@link Comparable#compareTo}.
     *
     * @param otro Valor con el que se compara.
     * @return Valor negativo, cero o positivo según la ordenación natural.
     */
    public int compararCon(T otro) {
        return this.info.compareTo(otro);
    }

    /**
     * Retorna el dato almacenado en este nodo.
     *
     * @return El dato del nodo.
     */
    public T getInfo() {
        return info;
    }

    /**
     * Retorna el hijo derecho de este nodo.
     *
     * @return Nodo hijo derecho, o {@code null} si no existe.
     */
    public NodoArbol<T> getHijoDerecha() {
        return hijoDerecha;
    }

    /**
     * Retorna el hijo izquierdo de este nodo.
     *
     * @return Nodo hijo izquierdo, o {@code null} si no existe.
     */
    public NodoArbol<T> getHijoIzquierda() {
        return hijoIzquierda;
    }
}

/**
 * Árbol Binario de Búsqueda (ABB) genérico y ordenado.
 *
 * <p>Almacena elementos que implementan {@link Comparable} manteniendo
 * la propiedad BST: todo elemento en el subárbol izquierdo es menor que
 * la raíz, y todo elemento en el subárbol derecho es mayor. Ofrece
 * inserción ordenada, búsqueda, obtención y eliminación con sucesor
 * inorden para el caso de dos hijos.</p>
 *
 * <p>Esta estructura es la columna vertebral del índice de marcadores,
 * organizados por {@code Categoria} que implementa {@link Comparable}.</p>
 *
 * <p>Los recorridos (inOrden, preOrden, posOrden, por anchura y por
 * profundidad) se conservan íntegramente del código original e internamente
 * usan {@link ListaDoble} y {@link Pila} en lugar de colecciones nativas.</p>
 *
 * @param <T> Tipo del dato almacenado; debe implementar {@link Comparable}.
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class ArbolBinario<T extends Comparable<T>> {

    /** Nodo raíz del árbol. */
    public NodoArbol<T> raiz;

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Construye un árbol con un único nodo raíz que contiene el dato indicado.
     *
     * @param info Dato del nodo raíz inicial.
     */
    public ArbolBinario(T info) {
        this.raiz = new NodoArbol<>(info);
    }

    // ── Inserción ────────────────────────────────────────────────────────────

    /**
     * Inserta un hijo derecho al nodo indicado, solo si el nodo existe y
     * no tiene aún hijo derecho.
     *
     * @param padre Nodo al que se le añade el hijo derecho.
     * @param dato  Dato del nuevo nodo.
     * @return La referencia al nuevo nodo, o {@code null} si no se pudo insertar.
     */
    public NodoArbol<T> insertarDerecha(NodoArbol<T> padre, T dato) {
        if (padre == null || padre.hijoDerecha != null) {
            return null;
        }
        NodoArbol<T> nuevo = new NodoArbol<>(dato);
        padre.hijoDerecha = nuevo;
        return nuevo;
    }

    /**
     * Inserta un hijo izquierdo al nodo indicado, solo si el nodo existe y
     * no tiene aún hijo izquierdo.
     *
     * @param padre Nodo al que se le añade el hijo izquierdo.
     * @param dato  Dato del nuevo nodo.
     * @return La referencia al nuevo nodo, o {@code null} si no se pudo insertar.
     */
    public NodoArbol<T> insertarIzquierda(NodoArbol<T> padre, T dato) {
        if (padre == null || padre.hijoIzquierda != null) {
            return null;
        }
        NodoArbol<T> nuevo = new NodoArbol<>(dato);
        padre.hijoIzquierda = nuevo;
        return nuevo;
    }

    /**
     * Inserta el dato respetando la propiedad BST (iterativo).
     *
     * <p>Si la raíz es {@code null} el dato se convierte en la nueva raíz.
     * Si el dato es igual a un nodo existente la inserción no se realiza
     * (no se permiten duplicados).</p>
     *
     * @param dato Dato a insertar en orden.
     * @return El nuevo nodo creado, o la raíz si el árbol estaba vacío.
     */
    public NodoArbol<T> insertarOrdenado(T dato) {
        if (this.raiz == null) {
            this.raiz = new NodoArbol<>(dato);
            return this.raiz;
        }
        NodoArbol<T> actual = this.raiz;
        NodoArbol<T> padre = null;
        boolean insertarDerecha = false;

        while (actual != null) {
            int cmp = actual.compararCon(dato);
            if (cmp == 0) {
                // Duplicado: no se inserta
                return actual;
            }
            padre = actual;
            if (cmp > 0) {
                actual = actual.hijoIzquierda;
                insertarDerecha = false;
            } else {
                actual = actual.hijoDerecha;
                insertarDerecha = true;
            }
        }

        NodoArbol<T> nuevo = new NodoArbol<>(dato);
        if (insertarDerecha) {
            padre.hijoDerecha = nuevo;
        } else {
            padre.hijoIzquierda = nuevo;
        }
        return nuevo;
    }

    // ── Búsqueda y obtención ─────────────────────────────────────────────────

    /**
     * Indica si existe un nodo cuyo dato sea igual al valor buscado.
     *
     * @param info Valor a buscar.
     * @return {@code true} si el árbol contiene el valor.
     */
    public boolean buscar(T info) {
        NodoArbol<T> actual = this.raiz;
        while (actual != null) {
            int cmp = actual.compararCon(info);
            if (cmp == 0) return true;
            actual = (cmp > 0) ? actual.hijoIzquierda : actual.hijoDerecha;
        }
        return false;
    }

    /**
     * Retorna el dato almacenado en el nodo que coincide con {@code info}.
     *
     * <p>Útil para recuperar la referencia viva al objeto y modificarlo
     * (por ejemplo, agregar un marcador a una categoría existente).</p>
     *
     * @param info Valor de búsqueda.
     * @return El dato del nodo encontrado, o {@code null} si no existe.
     */
    public T obtener(T info) {
        NodoArbol<T> actual = this.raiz;
        while (actual != null) {
            int cmp = actual.compararCon(info);
            if (cmp == 0) return actual.info;
            actual = (cmp > 0) ? actual.hijoIzquierda : actual.hijoDerecha;
        }
        return null;
    }

    // ── Eliminación ──────────────────────────────────────────────────────────

    /**
     * Elimina el nodo que contiene el valor indicado usando el algoritmo
     * del sucesor inorden para el caso de dos hijos.
     *
     * <p>Tres casos manejados:</p>
     * <ol>
     *   <li>Nodo hoja: se desvincula directamente.</li>
     *   <li>Nodo con un solo hijo: el hijo sube a la posición del padre.</li>
     *   <li>Nodo con dos hijos: el valor del sucesor inorden (mínimo del
     *       subárbol derecho) reemplaza al dato del nodo, y el sucesor
     *       es eliminado.</li>
     * </ol>
     *
     * @param valor Valor del nodo a eliminar.
     */
    public void eliminarOrdenado(T valor) {
        NodoArbol<T> padre = null;
        NodoArbol<T> actual = this.raiz;

        // Localizar el nodo
        while (actual != null && !actual.info.equals(valor)) {
            padre = actual;
            actual = (actual.compararCon(valor) > 0)
                    ? actual.hijoIzquierda
                    : actual.hijoDerecha;
        }

        if (actual == null) return; // No encontrado

        // Caso 1: nodo hoja
        if (actual.hijoIzquierda == null && actual.hijoDerecha == null) {
            if (padre == null) {
                this.raiz = null;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = null;
            } else {
                padre.hijoDerecha = null;
            }

        // Caso 2: un solo hijo
        } else if (actual.hijoIzquierda == null || actual.hijoDerecha == null) {
            NodoArbol<T> hijo = (actual.hijoIzquierda != null)
                    ? actual.hijoIzquierda
                    : actual.hijoDerecha;
            if (padre == null) {
                this.raiz = hijo;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = hijo;
            } else {
                padre.hijoDerecha = hijo;
            }

        // Caso 3: dos hijos — sucesor inorden
        } else {
            NodoArbol<T> sucesorPadre = actual;
            NodoArbol<T> sucesor = actual.hijoDerecha;
            while (sucesor.hijoIzquierda != null) {
                sucesorPadre = sucesor;
                sucesor = sucesor.hijoIzquierda;
            }
            actual.info = sucesor.info;
            if (sucesorPadre == actual) {
                sucesorPadre.hijoDerecha = sucesor.hijoDerecha;
            } else {
                sucesorPadre.hijoIzquierda = sucesor.hijoDerecha;
            }
            sucesor.info = null;
        }
    }

    /**
     * Alias de {@link #eliminarOrdenado(Object)} mantenido por compatibilidad
     * con el código original que usaba {@code eliminarOrdenadoA}.
     *
     * @param valor Valor del nodo a eliminar.
     */
    public void eliminarOrdenadoA(T valor) {
        eliminarOrdenado(valor);
    }

    // ── Recorridos ───────────────────────────────────────────────────────────

    /**
     * Recorrido por anchura (BFS) usando una {@link ListaDoble} como cola.
     * Imprime cada elemento en el orden nivel por nivel.
     */
    public void recorrer() {
        if (this.raiz == null) return;
        ListaDoble<NodoArbol<T>> cola = new ListaDoble<>();
        cola.insertar(this.raiz);
        while (cola.size() != 0) {
            NodoArbol<T> actual = cola.remover();
            System.out.println(actual.info);
            if (actual.hijoIzquierda != null) cola.insertar(actual.hijoIzquierda);
            if (actual.hijoDerecha != null) cola.insertar(actual.hijoDerecha);
        }
    }

    /**
     * Recorrido por profundidad (DFS) iterativo usando una {@link Pila}.
     *
     * @param nodo Nodo desde el que inicia el recorrido (normalmente {@code raiz}).
     */
    public void recorrerProfundidad(NodoArbol<T> nodo) {
        if (nodo == null) return;
        Pila<NodoArbol<T>> pila = new Pila<>();
        pila.push(nodo);
        while (!pila.empty()) {
            NodoArbol<T> actual = pila.pop();
            System.out.println(actual.info);
            // Se empuja derecho primero para que izquierdo salga primero (DFS-preorden)
            if (actual.hijoDerecha != null) pila.push(actual.hijoDerecha);
            if (actual.hijoIzquierda != null) pila.push(actual.hijoIzquierda);
        }
    }

    /**
     * Recorrido pre-orden recursivo: raíz → izquierdo → derecho.
     *
     * @param raiz Nodo desde el que inicia el recorrido.
     */
    public void recorrerPreOrden(NodoArbol<T> raiz) {
        if (raiz == null) return;
        System.out.println(raiz.info);
        recorrerPreOrden(raiz.hijoIzquierda);
        recorrerPreOrden(raiz.hijoDerecha);
    }

    /**
     * Recorrido in-orden recursivo: izquierdo → raíz → derecho.
     * Produce la salida en orden ascendente para un ABB.
     *
     * @param raiz Nodo desde el que inicia el recorrido.
     */
    public void recorrerInOrden(NodoArbol<T> raiz) {
        if (raiz == null) return;
        recorrerInOrden(raiz.hijoIzquierda);
        System.out.println(raiz.info);
        recorrerInOrden(raiz.hijoDerecha);
    }

    /**
     * Recorrido pos-orden recursivo: izquierdo → derecho → raíz.
     *
     * @param raiz Nodo desde el que inicia el recorrido.
     */
    public void recorrerPosOrden(NodoArbol<T> raiz) {
        if (raiz == null) return;
        recorrerPosOrden(raiz.hijoIzquierda);
        recorrerPosOrden(raiz.hijoDerecha);
        System.out.println(raiz.info);
    }
}