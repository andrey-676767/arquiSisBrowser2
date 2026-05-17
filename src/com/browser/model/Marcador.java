package com.browser.model;

/**
 * Entidad que representa un marcador de navegación guardado por el usuario.
 *
 * <p>Encapsula la URL, el título y la categoría del sitio web marcado.
 * El constructor vacío crea un marcador "en blanco" con cadenas vacías,
 * siguiendo el comportamiento original del código legado que usaba
 * {@code Marcador[]} inicializados con instancias vacías para representar
 * espacios libres en favoritos.</p>
 *
 * <p>Esta clase no depende de ninguna colección nativa ni de la capa
 * de persistencia.</p>
 *
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Marcador {

    /** URL del sitio web marcado. */
    private String url;

    /** Título descriptivo del marcador. */
    private String titulo;

    /** Categoría a la que pertenece el marcador. */
    private String categoria;

    // ── Constructores ────────────────────────────────────────────────────────

    /**
     * Construye un marcador vacío con todos los campos como cadena vacía.
     * Se usa como marcador de "posición libre" en la lista de favoritos.
     */
    public Marcador() {
        this.url = "";
        this.titulo = "";
        this.categoria = "";
    }

    /**
     * Construye un marcador con todos sus atributos definidos.
     *
     * @param url       URL del sitio web.
     * @param titulo    Título descriptivo del marcador.
     * @param categoria Categoría a la que pertenece.
     */
    public Marcador(String url, String titulo, String categoria) {
        this.url = url;
        this.titulo = titulo;
        this.categoria = categoria;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Retorna la URL del marcador.
     *
     * @return La URL almacenada.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retorna el título del marcador.
     *
     * @return El título almacenado.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Retorna la categoría del marcador.
     *
     * @return La categoría almacenada.
     */
    public String getCategoria() {
        return categoria;
    }

    // ── Setters ──────────────────────────────────────────────────────────────

    /**
     * Establece la URL del marcador.
     *
     * @param url Nueva URL.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Establece el título del marcador.
     *
     * @param titulo Nuevo título.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Establece la categoría del marcador.
     *
     * @param categoria Nueva categoría.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    /**
     * Imprime en consola la información resumida del marcador en formato
     * {@code Nombre: X URL: Y Categoria: Z}.
     */
    public void mostrarInfo() {
        System.out.print("Nombre: " + titulo + " URL: " + url + " Categoria: " + categoria);
    }

    /**
     * Retorna el título como representación textual del marcador.
     * Compatibilidad con {@code Arrays.toString(marcadores[])} del controlador original.
     *
     * @return El título del marcador.
     */
    @Override
    public String toString() {
        return titulo;
    }
}