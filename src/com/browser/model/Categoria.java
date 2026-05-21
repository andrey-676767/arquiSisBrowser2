package com.browser.model;

import com.browser.structures.ListaDoble;
import com.browser.structures.interfaces.IEstructuraDeDatos;

/**
 * Agrupación de {@link Marcador} bajo un nombre común.
 *
 * <p>Implementa {@link Comparable} usando el orden alfabético de la primera
 * letra del nombre, lo que permite mantener las categorías ordenadas dentro
 * del {@code ArbolBinario<Categoria>} que gestiona {@code MarcadorService}.</p>
 *
 * <p>El campo {@code contenido} es público para permitir que el árbol y el
 * servicio inserten marcadores directamente, manteniendo compatibilidad con
 * el diseño original.</p>
 */
public class Categoria implements Comparable<Categoria> {

    /** Nombre de la categoría (p. ej. "Tecnología", "Noticias"). */
    private String nombre;

    /**
     * Lista de marcadores que pertenecen a esta categoría.
     * Acceso público para compatibilidad con operaciones del árbol.
     */
    public IEstructuraDeDatos<Marcador> contenido;

    /**
     * Alfabeto en mayúsculas usado para calcular el índice de comparación.
     * Permite ordenar categorías por la inicial de su nombre.
     */
    public static final String[] abcd = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    /**
     * Construye una categoría vacía con nombre "Por defecto" y lista vacía.
     */
    public Categoria() {
        this.nombre = "Por defecto";
        this.contenido = new ListaDoble<>();
    }

    /**
     * Construye una categoría con nombre dado e inserta {@code inicial} como
     * primer marcador.
     *
     * @param nombre  nombre de la categoría; se usa su primera letra (mayúscula)
     *                como clave de ordenamiento en el árbol
     * @param inicial primer {@link Marcador} de la categoría; puede ser
     *                {@code null} si solo se crea el contenedor
     */
    public Categoria(String nombre, Marcador inicial) {
        this.nombre = nombre;
        this.contenido = new ListaDoble<>();
        if (inicial != null) {
            contenido.insertarAlPrincipio(inicial);
        }
    }

    // -------------------------------------------------------------------------
    // Comparable
    // -------------------------------------------------------------------------

    /**
     * Compara esta categoría con {@code o} según el índice alfabético de su
     * primera letra, produciendo el orden ascendente A → Z que requiere el BST.
     *
     * @param o categoría con la que comparar
     * @return negativo si esta categoría va antes, cero si son iguales,
     *         positivo si va después
     */
    @Override
    public int compareTo(Categoria o) {
        int oInt = buscarIndex(o.nombre);
        int thisInt = buscarIndex(this.nombre);
        return Integer.compare(thisInt, oInt);
    }

    /**
     * Retorna el índice (0–25) de la primera letra de {@code categoria}
     * dentro del alfabeto {@link #abcd}.
     *
     * @param categoria nombre cuya inicial se busca
     * @return índice de 0 a 25, o {@code -1} si no se encuentra
     */
    private static int buscarIndex(String categoria) {
        String letra = categoria.substring(0, 1).toUpperCase();
        for (int index = 0; index < abcd.length; index++) {
            if (abcd[index].equals(letra)) {
                return index;
            }
        }
        return -1;
    }

    // -------------------------------------------------------------------------
    // Operaciones sobre marcadores
    // -------------------------------------------------------------------------

    /**
     * Agrega un {@link Marcador} al final del contenido de esta categoría.
     *
     * @param marcador el marcador a agregar; no debe ser {@code null}
     */
    public void agregar(Marcador marcador) {
        contenido.insertar(marcador);
    }

    /**
     * Elimina el primer marcador cuyo título coincida con {@code titulo}.
     * Si no existe ninguno con ese título, la operación no tiene efecto.
     *
     * @param titulo título del marcador a eliminar
     */
    public void borrar(String titulo) {
        for (int i = 0; i < contenido.cantidad(); i++) {
            if (contenido.obtener(i).getTitulo().equals(titulo)) {
                contenido.remover(i);
                return;
            }
        }
    }

    /**
     * Elimina y retorna el marcador cuyo título coincida con {@code titulo}.
     * Equivalente a un "pop" selectivo; útil para mover marcadores entre
     * categorías o a favoritos.
     *
     * @param titulo título del marcador a extraer
     * @return el {@link Marcador} extraído, o {@code null} si no se encontró
     */
    public Marcador pop(String titulo) {
        for (int i = 0; i < contenido.cantidad(); i++) {
            if (contenido.obtener(i).getTitulo().equals(titulo)) {
                return contenido.remover(i);
            }
        }
        return null;
    }

    /**
     * Busca y retorna el marcador cuyo título coincida con {@code titulo}
     * sin eliminarlo de la lista.
     *
     * @param titulo título del marcador a buscar
     * @return el {@link Marcador} encontrado, o {@code null} si no existe
     */
    public Marcador obtener(String titulo) {
        for (int i = 0; i < contenido.cantidad(); i++) {
            Marcador aux = contenido.obtener(i);
            if (aux.getTitulo().equals(titulo)) {
                return aux;
            }
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Retorna el nombre de la categoría.
     *
     * @return nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna una representación en cadena con el nombre y el contenido
     * de la categoría.
     *
     * @return cadena con formato {@code nombre\n(contenido)}
     */
    @Override
    public String toString() {
        return this.nombre + "\n" + contenido.toString();
    }
}
