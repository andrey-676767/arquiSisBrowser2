package com.browser.structures;

import java.util.NoSuchElementException;
import java.util.Objects;

public class ListaDoble<T> {
    protected Nodo<T> cabeza;
    protected Nodo<T> cola;

    protected int cantidadNodos;

    //Constructor
    public ListaDoble(){
        cabeza = new Nodo<>(null);
        cola = new Nodo<>(null);
        cabeza.setSiguiente(cola);
        cola.setAnterior(cabeza);
    }

    //Equivalente al metodo addFirst(E)
    public void insertarAlPrincipio(T dato){
        Nodo<T> nodo = new Nodo<>(dato);
        Nodo<T> siguiente = cabeza.getSiguiente();
        Nodo<T> anterior = cabeza;
        nodo.setSiguiente(siguiente);
        cabeza.setSiguiente(nodo);
        nodo.setAnterior(anterior);
        siguiente.setAnterior(nodo);
        cantidadNodos++;
        /*System.out.println("Indice " + (cantidadNodos - 1) + "\n com.browser.structures.Nodo: " + nodo.info);*/
    }

    //Metodo equivalente a add(int, E)
    public void insertarEn(int index, T dato) throws IndexOutOfBoundsException{
        if (index >= cantidadNodos || index < 0){
            throw new IndexOutOfBoundsException("Indice " + index + " invalido");
        }
        Nodo<T> nodo = new Nodo<>(dato);
        Nodo<T> actual = cabeza;
        Nodo<T> siguiente;
        int i;
        for (i = 0; i < index; i++) {
            //System.out.println("Ciclando por indice: " + i);
            actual = actual.getSiguiente();
        }
        //System.out.println("\nNodo: " + actual.info + "\nIndice: " + i + "\n");
        siguiente = actual.getSiguiente();
        nodo.setAnterior(actual);
        nodo.setSiguiente(siguiente);
        siguiente.setAnterior(nodo);
        actual.setSiguiente(nodo);
        cantidadNodos++;
    }

    //Equivalente al metodo addLast(E)
    public void insertarAlFinal(T dato){
        Nodo<T> nodo = new Nodo<>(dato);
        Nodo<T> actual = cola.getAnterior();
        nodo.setAnterior(actual);
        nodo.setSiguiente(cola);
        actual.setSiguiente(nodo);
        cola.setAnterior(nodo);
        cantidadNodos++;
    }

    //Equivalente al metodo add(E)
    public boolean insertar(T dato){
        int aumento = cantidadNodos;
        this.insertarAlFinal(dato);
        return cantidadNodos == aumento + 1;
    }

    //Equivalente al metodo clear()
    public void limpiar(){
        Nodo<T> actual = cola;
        Nodo<T> anterior = actual.getAnterior();
        for (int i = 0; i < cantidadNodos; i++) {
            actual.setDato(null);
            actual.setSiguiente(null);
            actual.setAnterior(null);
            actual = null;

            actual = anterior;
            anterior = actual.getAnterior();
        }
        cola.setAnterior(cabeza);
        cabeza.setSiguiente(cola);
        cantidadNodos = 0;
    }

    //Equivalente al metodo set(int, E)
    public T reemplazar(int index, T dato) throws IndexOutOfBoundsException{
        if (index < 0 || index >= cantidadNodos){
            throw new IndexOutOfBoundsException("Indice " + index + "invalido");
        }
        Nodo<T> actual = cabeza.getSiguiente();
        if (index == 0){
            T obj = cabeza.getSiguiente().getDato();
            cabeza.getSiguiente().setDato(dato);
            return obj;
        }
        for (int i = 0; i < index; i++){
            actual = actual.getSiguiente();
        }
        T elemento = actual.getDato();
        actual.setDato(dato);
        return elemento;
    }

    //Equivalente al metodo contains(Object)
    public boolean contiene(Object o){
        Nodo<T> actual = cabeza.getSiguiente();
        int i = 0;
        while (actual.getSiguiente() != null && !Objects.equals(actual.getDato(), o)){
            actual = actual.getSiguiente();
            i++;
            if (actual.getSiguiente() != null && Objects.equals(actual.getDato(), o)){
                return true;
            }
        }
        return i == 0 && Objects.equals(actual.getDato(), o);
    }

    //Equivalente al metodo element()
    public T elemento() throws NoSuchElementException {
        if (cantidadNodos != 0) return cabeza.getSiguiente().getDato();
        throw new NoSuchElementException("La lista esta vacia");
    }

    //Equivalente al metodo get(int)
    public T obtener(int index) throws IndexOutOfBoundsException{
        if (index < 0 || index >= cantidadNodos){
            throw new IndexOutOfBoundsException("Indice " + index + " invalido para " + cantidadNodos);
        }
        Nodo<T> actual = cabeza;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getSiguiente();
            if (j == index) break;
        }
        return actual.getDato();
    }

    //Equivalente a getFirst()
    public T obtenerPrimero() throws NoSuchElementException{
        if (cantidadNodos != 0) return cabeza.getSiguiente().getDato();
        throw new NoSuchElementException("Lista vacia");
    }

    //Equivalente a getLast()
    public T obtenerUltimo() throws NoSuchElementException{
        if (cantidadNodos != 0) return cola.getAnterior().getDato();
        throw new NoSuchElementException("Lista vacia");
    }

    /**
     * Imprime todos los elementos de la lista junto a sus
     * respectivas posiciones desde el indice 0.
     *
     *
     */
    public void mostrarLista(){
        if (this.size() != 0){
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

    public void mostrarVertical(){
        if (this.size() != 0){
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

    //Equivalente a indexOf(Object)
    public int indiceDe(Object o){
        Nodo<T> actual = cabeza;
        boolean encontrado = false;
        int i;
        for (i = 0; i < cantidadNodos; i++) {
            actual = actual.getSiguiente();
            if (Objects.equals(actual.getDato(), o)){
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("El elemento no se encuentra en la lista");
            return -1;
        }
        return i;
    }


    public Nodo<T> borrarUltimo(){
        if (cantidadNodos == 0){
            System.out.println("No hay nodos restantes");
            return null;
        }

        Nodo<T> nodo = cola.getAnterior();
        Nodo<T> actual = cola.getAnterior();
        Nodo<T> anterior = actual.getAnterior();

        anterior.setSiguiente(cola);
        cola.setAnterior(anterior);
        actual.setSiguiente(null);
        actual.setAnterior(null);
        actual = null;

        cantidadNodos--;
        return nodo;
    }

    //Equivalente a remove()
    public T remover() throws NoSuchElementException{
        if (cantidadNodos != 0){
            T info = cabeza.getSiguiente().getDato();
            Nodo<T> nodo = cabeza.getSiguiente();
            Nodo<T> actual = nodo.getSiguiente();
            actual.setAnterior(cabeza);
            cabeza.setSiguiente(actual);

            nodo.setSiguiente(null);
            nodo.setDato(null);
            nodo.setAnterior(null);
            nodo = null;
            cantidadNodos--;

            return info;
        }
        throw new NoSuchElementException("Lista vacia");
    }

    //Equivalente a remove(int)
    public T remover(int index) throws IndexOutOfBoundsException{
        if (index >= cantidadNodos || index < 0) {
            System.out.println("Indice no valido");
            throw new IndexOutOfBoundsException("Indice " + index + "invalido");
        }
        Nodo<T> actual;
        Nodo<T> anterior = cabeza;
        Nodo<T> siguiente;
        int i = 0;
        while (i < index){
            anterior = anterior.getSiguiente();
            i++;
        }
        actual = anterior.getSiguiente();
        T info = actual.getDato();
        siguiente = actual.getSiguiente();

        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);

        actual.setSiguiente(null);
        actual.setAnterior(null);
        actual = null;
        cantidadNodos--;

        return info;
    }

    //Equivalente a remove(Object)
    public boolean removerObjeto(Object o){
        boolean encontrado = false;
        Nodo<T> anterior;
        Nodo<T> actual = cabeza;
        Nodo<T> siguiente;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getSiguiente();
            if (actual.getDato() == o) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) return false;
        T elemento = actual.getDato();
        anterior = actual.getAnterior();
        siguiente = actual.getSiguiente();
        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);

        actual.setAnterior(null);
        actual.setSiguiente(null);
        actual = null;
        cantidadNodos--;

        return true;
    }

    //Equivalente a removeFirstOcurrence(E)
    public boolean removerPrimeraOcurrencia(T dato){
        return this.removerObjeto(dato);
    }

    public boolean removerUltimaOcurrencia(T dato){
        Nodo<T> anterior;
        Nodo<T> actual = cola.getAnterior();
        Nodo<T> siguiente;
        boolean encontrado = false;
        for (int j = 0; j < cantidadNodos; j++) {
            actual = actual.getAnterior();
            if (actual.getDato() == dato) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) return false;

        anterior = actual.getAnterior();
        siguiente = actual.getSiguiente();
        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);
        actual.setAnterior(null);
        actual.setSiguiente(null);
        actual.setDato(null);
        actual = null;
        cantidadNodos--;

        return true;
    }

    //Todo:
    /*Pregunta: el metodo original dice que se inserta el dato como la cola de la lista no entiendo bien, clarificar
    con el profesor*/
    public boolean offer(T dato){
        return insertar(dato);
    }

    public boolean offerFirst(T dato){
        int aumento = cantidadNodos;
        this.insertarAlPrincipio(dato);
        return cantidadNodos ==  aumento + 1;
    }

    public boolean offerLast(T dato){
        return insertar(dato);
    }

    public T poll(){
        if (this.size() != 0){
            return this.remover();
        }
        return null;
    }

    public T peek(){
        if (this.size() != 0){
            return this.elemento();
        }
        return null;    
    }

    public Object[] toArray(){
        Nodo<T> actual = cabeza.getSiguiente();
        Object[] array = new Object[cantidadNodos];

        for (int i = 0; i < cantidadNodos; i++){
            array[i] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return array;
    }
    //No se como implementar esto

    public int size(){
        return cantidadNodos;
    }

    @Override
    public String toString(){
        if (this.size() != 0){
            StringBuilder info = new StringBuilder();
            Nodo<T> actual = cabeza.getSiguiente();
            int i = 0;
            while (actual.getSiguiente() != null){
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
