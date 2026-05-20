package com.browser.structures;

/**
 * Árbol binario de búsqueda (BST) genérico.
 *
 * <p>Soporta inserción ordenada, eliminación con los tres casos clásicos
 * (nodo hoja, nodo con un hijo, nodo con dos hijos), búsqueda por valor
 * y múltiples estrategias de recorrido (BFS, DFS, pre-orden, in-orden,
 * pos-orden).</p>
 *
 * <p>Se usa en el sistema para almacenar el árbol de {@code Categoria},
 * cuyo orden lexicográfico determina la posición de cada nodo.</p>
 *
 * @param <T> tipo de dato almacenado; debe implementar {@link Comparable}
 */
public class ArbolBinario<T extends Comparable<T>> {

    /** Nodo raíz del árbol. */
    public NodoArbol<T> raiz;

    /**
     * Construye un árbol con un único nodo raíz que contiene {@code info}.
     *
     * @param info el dato del nodo raíz
     */
    public ArbolBinario(T info) {
        raiz = new NodoArbol<>(info);
    }

    // -------------------------------------------------------------------------
    // Inserción
    // -------------------------------------------------------------------------

    /**
     * Inserta {@code dato} como hijo derecho de {@code p}, si ese enlace está libre.
     *
     * @param p    el nodo padre
     * @param dato el valor a insertar
     * @return el nuevo {@link NodoArbol} creado, o {@code null} si {@code p} es
     *         {@code null} o ya tiene hijo derecho
     */
    public NodoArbol<T> insertarDerecha(NodoArbol<T> p, T dato) {
        if (p == null || p.hijoDerecha != null) return null;
        NodoArbol<T> referencia = new NodoArbol<>(dato);
        p.hijoDerecha = referencia;
        return referencia;
    }

    /**
     * Inserta {@code dato} como hijo izquierdo de {@code p}, si ese enlace está libre.
     *
     * @param p    el nodo padre
     * @param dato el valor a insertar
     * @return el nuevo {@link NodoArbol} creado, o {@code null} si {@code p} es
     *         {@code null} o ya tiene hijo izquierdo
     */
    public NodoArbol<T> insertarIzquierda(NodoArbol<T> p, T dato) {
        if (p == null || p.hijoIzquierda != null) return null;
        NodoArbol<T> referencia = new NodoArbol<>(dato);
        p.hijoIzquierda = referencia;
        return referencia;
    }

    /**
     * Inserta {@code dato} respetando el invariante del BST (iterativo).
     * Si el árbol ya contiene un nodo igual, lo inserta en el subárbol derecho.
     *
     * @param dato el valor a insertar
     * @return el nuevo {@link NodoArbol} creado
     */
    public NodoArbol<T> insertarOrdenado(T dato) {
        if (this.raiz != null) {
            NodoArbol<T> actual = this.raiz;
            NodoArbol<T> padre = this.raiz;
            boolean der = false;

            while (actual != null) {
                padre = actual;
                if (actual.compararCon(dato) > 0) {
                    actual = actual.hijoIzquierda;
                    der = false;
                } else {
                    actual = actual.hijoDerecha;
                    der = true;
                }
            }
            if (der) {
                padre.hijoDerecha = new NodoArbol<>(dato);
                return padre.hijoDerecha;
            } else {
                padre.hijoIzquierda = new NodoArbol<>(dato);
                return padre.hijoIzquierda;
            }
        }
        this.raiz = new NodoArbol<>(dato);
        return this.raiz;
    }

    // -------------------------------------------------------------------------
    // Eliminación
    // -------------------------------------------------------------------------

    /**
     * Elimina el nodo con valor {@code valor} del BST manteniendo el invariante.
     * Usa el sucesor in-orden (mínimo del subárbol derecho) cuando el nodo
     * tiene dos hijos.
     *
     * @param valor el valor a eliminar; si no existe, la operación no tiene efecto
     */
    public void eliminarOrdenado(T valor) {
        NodoArbol<T> padre = null;
        NodoArbol<T> actual = this.raiz;

        // Buscar el nodo
        while (actual != null && !actual.info.equals(valor)) {
            padre = actual;
            if (actual.compararCon(valor) > 0) {
                actual = actual.hijoIzquierda;
            } else {
                actual = actual.hijoDerecha;
            }
        }
        if (actual == null) return;

        // Caso 1: nodo hoja
        if (actual.hijoIzquierda == null && actual.hijoDerecha == null) {
            if (padre == null) {
                this.raiz = null;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = null;
            } else {
                padre.hijoDerecha = null;
            }
        }
        // Caso 2: nodo con un solo hijo
        else if (actual.hijoIzquierda == null || actual.hijoDerecha == null) {
            NodoArbol<T> hijo = (actual.hijoIzquierda != null)
                    ? actual.hijoIzquierda : actual.hijoDerecha;
            if (padre == null) {
                this.raiz = hijo;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = hijo;
            } else {
                padre.hijoDerecha = hijo;
            }
        }
        // Caso 3: nodo con dos hijos — sucesor in-orden
        else {
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
        }
    }

    /**
     * Variante de {@link #eliminarOrdenado(Comparable)} que nulifica explícitamente
     * las referencias del nodo eliminado para facilitar la recolección de basura.
     *
     * @param valor el valor a eliminar
     */
    public void eliminarOrdenadoA(T valor) {
        NodoArbol<T> padre = null;
        NodoArbol<T> actual = this.raiz;

        while (actual != null && !actual.info.equals(valor)) {
            padre = actual;
            if (actual.compararCon(valor) > 0) {
                actual = actual.hijoIzquierda;
            } else {
                actual = actual.hijoDerecha;
            }
        }
        if (actual == null) return;

        if (actual.hijoDerecha == null && actual.hijoIzquierda == null) {
            if (padre == null) {
                this.raiz = null;
            } else if (actual.compararCon(padre.info) < 0) {
                padre.hijoIzquierda = null;
            } else {
                padre.hijoDerecha = null;
            }
            actual.info = null;

        } else if (actual.hijoDerecha == null || actual.hijoIzquierda == null) {
            NodoArbol<T> hijo = (actual.hijoIzquierda != null)
                    ? actual.hijoIzquierda : actual.hijoDerecha;
            if (padre == null) {
                this.raiz = hijo;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = hijo;
            } else {
                padre.hijoDerecha = hijo;
            }
            actual.info = null;

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

    // -------------------------------------------------------------------------
    // Búsqueda
    // -------------------------------------------------------------------------

    /**
     * Indica si el árbol contiene un nodo cuyo valor es igual a {@code info}.
     *
     * @param info el valor a buscar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    public boolean buscar(T info) {
        NodoArbol<T> actual = this.raiz;
        while (actual != null) {
            if (actual.compararCon(info) == 0) return true;
            actual = actual.compararCon(info) > 0
                    ? actual.hijoIzquierda : actual.hijoDerecha;
        }
        return false;
    }

    /**
     * Retorna el dato almacenado en el nodo cuyo valor es igual a {@code info}.
     *
     * @param info el valor a buscar
     * @return el dato del nodo encontrado, o {@code null} si no existe
     */
    public T obtener(T info) {
        NodoArbol<T> actual = this.raiz;
        while (actual != null) {
            if (actual.compararCon(info) == 0) return actual.info;
            actual = actual.compararCon(info) > 0
                    ? actual.hijoIzquierda : actual.hijoDerecha;
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Recorridos
    // -------------------------------------------------------------------------

    /**
     * Recorre el árbol en anchura (BFS), imprimiendo cada valor.
     * Usa una {@link ListaDoble} como cola auxiliar.
     */
    public void recorrer() {
        ListaDoble<NodoArbol<T>> q = new ListaDoble<>();
        q.insertar(this.raiz);
        while (q.size() != 0) {
            NodoArbol<T> actual = q.remover();
            System.out.println(actual.info);
            @SuppressWarnings("unchecked")
            NodoArbol<T>[] hijos = new NodoArbol[2];
            hijos[0] = actual.hijoIzquierda;
            hijos[1] = actual.hijoDerecha;
            for (NodoArbol<T> hijo : hijos) {
                if (!q.contiene(hijo) && hijo != null) {
                    q.insertar(hijo);
                }
            }
        }
    }

    /**
     * Recorre el árbol en profundidad (DFS) a partir de {@code nodo},
     * usando una {@link Pila} auxiliar.
     *
     * @param nodo el nodo desde el cual iniciar el recorrido
     */
    public void recorrerProfundidad(NodoArbol<T> nodo) {
        Pila<NodoArbol<T>> q = new Pila<>();
        q.push(nodo);
        while (q.size() != 0) {
            NodoArbol<T> actual = q.pop();
            System.out.println(actual.info);
            @SuppressWarnings("unchecked")
            NodoArbol<T>[] hijos = new NodoArbol[2];
            hijos[0] = actual.hijoDerecha;
            hijos[1] = actual.hijoIzquierda;
            for (NodoArbol<T> hijo : hijos) {
                if (!q.contiene(hijo) && hijo != null) {
                    q.push(hijo);
                }
            }
        }
    }

    /**
     * Recorre el árbol en pre-orden (raíz → izquierda → derecha) de forma recursiva.
     *
     * @param raiz nodo desde el cual iniciar; si es {@code null}, el método retorna
     */
    public void recorrerPreOrden(NodoArbol<T> raiz) {
        if (raiz == null) return;
        System.out.println(raiz.info);
        recorrerPreOrden(raiz.hijoIzquierda);
        recorrerPreOrden(raiz.hijoDerecha);
    }

    /**
     * Recorre el árbol en in-orden (izquierda → raíz → derecha) de forma recursiva.
     * En un BST produce los elementos en orden ascendente.
     *
     * @param raiz nodo desde el cual iniciar; si es {@code null}, el método retorna
     */
    public void recorrerInOrden(NodoArbol<T> raiz) {
        if (raiz == null) return;
        recorrerInOrden(raiz.hijoIzquierda);
        System.out.println(raiz.info);
        recorrerInOrden(raiz.hijoDerecha);
    }

    /**
     * Recorre el árbol en pos-orden (izquierda → derecha → raíz) de forma recursiva.
     *
     * @param raiz nodo desde el cual iniciar; si es {@code null}, el método retorna
     */
    public void recorrerPosOrden(NodoArbol<T> raiz) {
        if (raiz == null) return;
        recorrerInOrden(raiz.hijoIzquierda);
        recorrerInOrden(raiz.hijoDerecha);
        System.out.println(raiz.info);
    }

    /**
     * Inserta {@code dato} de forma ordenada recursivamente.
     * <em>Nota:</em> implementación incompleta en el código original; conservada
     * para compatibilidad futura.
     *
     * @param dato  el valor a insertar
     * @param raiz  el nodo raíz del subárbol actual
     * @return siempre {@code null} en la implementación actual
     */
    public NodoArbol<T> insertarOrdenadoRecursivo(T dato, NodoArbol<T> raiz) {
        if (raiz == null) return null;
        if (raiz.compararCon(dato) < 0) {
            insertarOrdenadoRecursivo(dato, raiz.hijoDerecha);
        } else {
            insertarOrdenadoRecursivo(dato, raiz.hijoIzquierda);
        }
        return null;
    }
}
