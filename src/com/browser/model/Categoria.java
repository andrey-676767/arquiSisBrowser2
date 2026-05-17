package com.browser.model;

import com.browser.structures.ListaDoble;

/**
 * Entidad que agrupa {@link Marcador}es bajo una letra inicial común.
 *
 * <p>Implementa {@link Comparable} para poder ser almacenada de forma
 * ordenada en el {@code ArbolBinario<Categoria>}. El orden se establece
 * según la posición de la letra inicial del nombre de la categoría en el
 * alfabeto (A=0, B=1, … Z=25).</p>
 *
 * <p>El atributo {@code contenido} usa exclusivamente {@link ListaDoble}
 * — prohibido el uso de {@code java.util.List} o derivados.</p>
 *
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Categoria implements Comparable<Categoria> {

    /** Nombre o letra inicial que identifica la categoría. */
    private String nombre;

    /**
     * Lista de marcadores pertenecientes a esta categoría.
     * Se usa {@link ListaDoble} como implementación exclusiva.
     */
    private ListaDoble<Marcador> contenido;

    /** Tabla del alfabeto en mayúsculas para calcular el orden de comparación. */
    private static final String[] ALFABETO = {
        "A","B","C","D","E","F","G","H","I","J",
        "K","L","M","N","O","P","Q","R","S","T",
        "U","V","W","X","Y","Z"
    };

    // ── Constructores ────────────────────────────────────────────────────────

    /**
     * Construye una categoría vacía con el nombre "Por defecto".
     */
    public Categoria() {
        this.nombre = "Por defecto";
        this.contenido = new ListaDoble<>();
    }

    /**
     * Construye una categoría con el nombre indicado e inserta el primer
     * marcador si no es {@code null}.
     *
     * <p>Si {@code inicial} es {@code null} la categoría queda vacía pero
     * ya identificada por su nombre — útil para crear categorías de búsqueda
     * temporales dentro del árbol.</p>
     *
     * @param nombre  Nombre (o letra inicial) de la categoría.
     * @param inicial Primer marcador a incluir, o {@code null} si ninguno.
     */
    public Categoria(String nombre, Marcador inicial) {
        this.nombre = nombre;
        this.contenido = new ListaDoble<>();
        if (inicial != null) {
            this.contenido.insertarAlPrincipio(inicial);
        }
    }

    // ── Comparable ───────────────────────────────────────────────────────────

    /**
     * Compara esta categoría con otra usando la posición de su letra inicial
     * en el alfabeto.
     *
     * @param otra La categoría con la que se compara.
     * @return Valor negativo si esta precede a {@code otra}, 0 si son iguales,
     *         positivo si esta sucede a {@code otra}.
     */
    @Override
    public int compareTo(Categoria otra) {
        int indiceThis = buscarIndiceAlfabeto(this.nombre);
        int indiceOtra = buscarIndiceAlfabeto(otra.nombre);
        return Integer.compare(indiceThis, indiceOtra);
    }

    /**
     * Busca la posición en el alfabeto de la primera letra del nombre de
     * la categoría.
     *
     * @param categoria Nombre de la categoría.
     * @return Índice 0-based en {@link #ALFABETO}, o -1 si no se encuentra.
     */
    private static int buscarIndiceAlfabeto(String categoria) {
        if (categoria == null || categoria.isEmpty()) return -1;
		
        String letra = categoria.substring(0, 1).toUpperCase();
		
        for (int i = 0; i < ALFABETO.length; i++) {
            if (ALFABETO[i].equals(letra)) return i;
        }
        return -1;
    }

    // ── Operaciones sobre el contenido ───────────────────────────────────────

    /**
     * Agrega un marcador al final del contenido de esta categoría.
     *
     * @param marcador Marcador a agregar.
     */
    public void agregarMarcador(Marcador marcador) {
        this.contenido.insertarAlFinal(marcador);
    }

    /**
     * Elimina de la lista todos los marcadores cuyo título coincida
     * (exactamente) con el nombre proporcionado.
     *
     * @param nombre Título del marcador a eliminar.
     */
    public void borrarMarcador(String nombre) {
        for (int i = 0; i < this.contenido.size(); i++) {
            Marcador aux = this.contenido.obtener(i);
            if (aux.getTitulo().equals(nombre)) {
                this.contenido.remover(i);
                // Ajustamos el índice porque la lista se contrajo
                i--;
            }
        }
    }

    /**
     * Extrae (elimina y retorna) el primer marcador con el título indicado.
     *
     * <p>Se usa para mover un marcador desde "otros" hacia "favoritos".</p>
     *
     * @param nombre Título del marcador a extraer.
     * @return El marcador extraído, o {@code null} si no se encontró.
     */
    public Marcador pop(String nombre) {
        for (int i = 0; i < this.contenido.size(); i++) {
            Marcador aux = this.contenido.obtener(i);
            if (aux.getTitulo().equals(nombre)) {
                return this.contenido.remover(i);
            }
        }
        return null;
    }

    /**
     * Retorna (sin extraer) el primer marcador con el título indicado.
     *
     * @param nombre Título del marcador a buscar.
     * @return El marcador encontrado, o {@code null} si no existe.
     */
    public Marcador obtenerMarcador(String nombre) {
        for (int i = 0; i < this.contenido.size(); i++) {
            Marcador aux = this.contenido.obtener(i);
            if (aux.getTitulo().equals(nombre)) {
                return aux;
            }
        }
        return null;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Retorna el nombre (letra inicial) de la categoría.
     *
     * @return El nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna la lista de marcadores de esta categoría.
     *
     * <p>Se expone para que {@code DatabaseManager} pueda invocar
     * {@code insertarAlFinal} directamente al reconstruir el árbol
     * desde la base de datos, manteniendo compatibilidad con el
     * código original.</p>
     *
     * @return La {@link ListaDoble} de {@link Marcador}es.
     */
    public ListaDoble<Marcador> getContenido() {
        return contenido;
    }

    /**
     * Retorna el número de marcadores en esta categoría.
     *
     * @return Tamaño del contenido.
     */
    public int cantidadMarcadores() {
        return contenido.size();
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    /**
     * Retorna una representación textual de la categoría que incluye su nombre
     * y el listado de sus marcadores.
     *
     * @return Cadena con nombre y contenido de la categoría.
     */
    @Override
    public String toString() {
        String contenidoStr = contenido.toString();
        return this.nombre + "\n" + (contenidoStr != null ? contenidoStr : "(vacía)");
    }
}