package com.browser.structures;

class NodoArbol<T extends Comparable<T>>{
    protected T info;
    protected NodoArbol<T> hijoDerecha;
    protected NodoArbol<T> hijoIzquierda;

    public NodoArbol(T dato){
        this.info = dato;
        hijoDerecha = null;
        hijoIzquierda = null;
    }

    public int compararCon(T otro){
        return this.info.compareTo(otro);
    }

}

public class ArbolBinario<T extends Comparable<T>> {
    public NodoArbol<T> raiz;

    public ArbolBinario(T info){
        raiz = new NodoArbol<>(info);
    }

    public NodoArbol<T> insertarDerecha(NodoArbol<T> p, T dato){
        if (p == null || p.hijoDerecha != null){
            return null;
        }
        NodoArbol<T> referencia = new NodoArbol<>(dato);
        p.hijoDerecha = referencia;

        return referencia;
    }

    public NodoArbol<T> insertarIzquierda(NodoArbol<T> p, T dato){
        if (p == null || p.hijoIzquierda != null){
            return null;
        }
        NodoArbol<T> referencia = new NodoArbol<>(dato);
        p.hijoIzquierda = referencia;

        return referencia;
    }

    public void recorrer(){
        ListaDoble<NodoArbol<T>> q = new ListaDoble<>();
        q.insertar(this.raiz);
        while (q.size() != 0){
            NodoArbol<T> actual = q.remover();
            System.out.println(actual.info);
            NodoArbol<T>[] hijos = new NodoArbol[2];
            hijos[0] = actual.hijoIzquierda;
            hijos[1] = actual.hijoDerecha;

            for (NodoArbol<T> hijo: hijos){
                if (!q.contiene(hijo) && hijo != null){
                    q.insertar(hijo);
                }
            }
        }
    }

    public void recorrerProfundidad(NodoArbol<T> nodo){
        Pila<NodoArbol<T>> q = new Pila<>();
        q.push(nodo);
        while (q.size() != 0){
            NodoArbol<T> actual = q.pop();
            System.out.println(actual.info);
            NodoArbol<T>[] hijos = new NodoArbol[2];
            hijos[0] = actual.hijoDerecha;
            hijos[1] = actual.hijoIzquierda;

            for (NodoArbol<T> hijo: hijos){
                if (!q.contiene(hijo) && hijo != null){
                    q.push(hijo);
                }
            }
        }
    }

    //Recursivo
    public void recorrerPreOrden(NodoArbol<T> raiz){
        if (raiz == null) return;
        System.out.println(raiz.info);
        recorrerPreOrden(raiz.hijoIzquierda);
        recorrerPreOrden(raiz.hijoDerecha);
    }

    //Recursivo
    public void recorrerInOrden(NodoArbol<T> raiz){
        if (raiz == null) return;

        recorrerInOrden(raiz.hijoIzquierda);
        System.out.println(raiz.info);
        recorrerInOrden(raiz.hijoDerecha);
    }

    public void recorrerPosOrden(NodoArbol<T> raiz){
        if (raiz == null) return;
        recorrerInOrden(raiz.hijoIzquierda);
        recorrerInOrden(raiz.hijoDerecha);
        System.out.println(raiz.info);
    }

    public void recorrerPreordenPila(NodoArbol<T> raiz){
        Pila<NodoArbol<T>> pila = new Pila<>();
        while (!pila.empty()){
            NodoArbol<T> aux = raiz;
            if (aux != null){
                System.out.println(raiz.info);
                pila.push(raiz.hijoIzquierda);
                aux = pila.pop();
            }
        }
    }

    public NodoArbol<T> insertarOrdenado(T dato){
        if (this.raiz != null){
            NodoArbol<T> actual = this.raiz;
            NodoArbol<T> padre = this.raiz;
            boolean der = false;

            while (actual != null){
                padre = actual;
                if (actual.compararCon(dato) > 0){

                    actual = actual.hijoIzquierda;
                    der = false;
                }
                else if (actual.compararCon(dato) < 0){

                    actual = actual.hijoDerecha;
                    der = true;
                }
            }
            if (der) {
                padre.hijoDerecha = new NodoArbol<>(dato);
                return padre.hijoDerecha;
            }
            else {
                padre.hijoIzquierda = new NodoArbol<>(dato);
                return padre.hijoIzquierda;
            }
        }
        this.raiz = new NodoArbol<>(dato);
        return this.raiz;
    }

    private NodoArbol<T> buscarPadre(NodoArbol<T> hijo){
        NodoArbol<T> actual = this.raiz;

        while (actual.hijoDerecha != hijo && actual.hijoIzquierda != hijo){
            if (hijo.compararCon(actual.info) > 0){
                actual = actual.hijoDerecha;
            } else {
                actual = actual.hijoIzquierda;
            }
        }
        return actual;
    }

    public void eliminarOrdenado(T valor) {
        // Paso 1: Buscar el nodo a eliminar y su padre
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

        // Si no se encontró el nodo
        if (actual == null) return;

        // Caso 1: com.browser.structures.Nodo hoja (sin hijos)
        if (actual.hijoIzquierda == null && actual.hijoDerecha == null) {
            if (padre == null) {
                this.raiz = null;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = null;
            } else {
                padre.hijoDerecha = null;
            }
        }
        // Caso 2: com.browser.structures.Nodo con un solo hijo
        else if (actual.hijoIzquierda == null || actual.hijoDerecha == null) {
            NodoArbol<T> hijo = (actual.hijoIzquierda != null) ?
                    actual.hijoIzquierda : actual.hijoDerecha;

            if (padre == null) {
                this.raiz = hijo;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = hijo;
            } else {
                padre.hijoDerecha = hijo;
            }
        }
        // Caso 3: com.browser.structures.Nodo con dos hijos
        else {
            // Encontrar el sucesor inorden (mínimo en subárbol derecho)
            NodoArbol<T> sucesorPadre = actual;
            NodoArbol<T> sucesor = actual.hijoDerecha;

            while (sucesor.hijoIzquierda != null) {
                sucesorPadre = sucesor;
                sucesor = sucesor.hijoIzquierda;
            }

            // Copiar el valor del sucesor
            actual.info = sucesor.info;

            // Eliminar el sucesor (que tendrá 0 o 1 hijo derecho)
            if (sucesorPadre == actual) {
                sucesorPadre.hijoDerecha = sucesor.hijoDerecha;
            } else {
                sucesorPadre.hijoIzquierda = sucesor.hijoDerecha;
            }
        }
    }

    public void eliminarOrdenadoA(T valor){

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

        // Si no se encontro el nodo
        if (actual == null) return;

        if (actual.hijoDerecha == null && actual.hijoIzquierda == null) {
            if (padre == null) {
                this.raiz = null;
            } else if (actual.compararCon(padre.info) < 0) padre.hijoIzquierda = null;
            else padre.hijoDerecha = null;
            actual.info = null;
            actual = null;

            //cuando tiene 1 hijo
        } else if (actual.hijoDerecha == null || actual.hijoIzquierda == null) {
            NodoArbol<T> hijo = (actual.hijoIzquierda != null) ?
                    actual.hijoIzquierda : actual.hijoDerecha;

            if (padre == null) {
                this.raiz = hijo;
            } else if (padre.hijoIzquierda == actual) {
                padre.hijoIzquierda = hijo;
            } else {
                padre.hijoDerecha = hijo;
            }
            actual.info = null;
            actual = null;
        } else { //cuando tiene 2 hijos se encuentra el minimo sucesor inorden
            NodoArbol<T> sucesorPadre = actual;
            NodoArbol<T> sucesor = actual.hijoDerecha;

            while (sucesor.hijoIzquierda != null) {
                sucesorPadre = sucesor;
                sucesor = sucesor.hijoIzquierda;
            }

            // reemplazar
            actual.info = sucesor.info;

            // Eliminar el sucesor (que tendrá 0 o 1 hijo derecho)
            if (sucesorPadre == actual) {
                sucesorPadre.hijoDerecha = sucesor.hijoDerecha;
            } else {
                sucesorPadre.hijoIzquierda = sucesor.hijoDerecha;
            }

            sucesor.info = null;
            sucesor = null;
        }

    }

    public boolean buscar(T info) {
        NodoArbol<T> actual = this.raiz;
        NodoArbol<T> padre = this.raiz;

        while (actual != null) {
            padre = actual;
            if (actual.compararCon(info) == 0){
                return true;
            } else if (actual.compararCon(info) > 0) {
                actual = actual.hijoIzquierda;
            } else {
                actual = actual.hijoDerecha;
            }
        }
        return false;
    }

    public T obtener(T info){
        NodoArbol<T> actual = this.raiz;
        NodoArbol<T> padre = this.raiz;

        while (actual != null) {
            padre = actual;
            if (actual.compararCon(info) == 0){
                return actual.info;
            } else if (actual.compararCon(info) > 0) {
                actual = actual.hijoIzquierda;
            } else {
                actual = actual.hijoDerecha;
            }
        }
        return null;
    }


    public NodoArbol<T> insertarOrdenadoRecursivo(T dato, NodoArbol<T> raiz){
        if (raiz == null) return null;
        NodoArbol<T> padre = raiz;
        NodoArbol<T> actual = raiz;
        if (raiz.compararCon(dato) < 0){
            actual = insertarOrdenadoRecursivo(dato, actual.hijoDerecha);
        }
        else  actual = insertarOrdenadoRecursivo(dato, actual.hijoIzquierda);
        return null;
    }

}

