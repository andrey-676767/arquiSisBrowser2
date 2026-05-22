package com.browser.model;

/**
 * Entidad que representa un marcador (bookmark) guardado por el usuario.
 *
 * <p>Almacena la URL destino, un título legible y la categoría bajo la cual
 * se agrupa en el {@code ArbolBinario<Categoria>}. Es la unidad mínima de
 * información que persiste {@code JSONRepo} y que gestiona {@code MarcadorService}.</p>
 */
public class Marcador {

    /** URL del sitio marcado. */
    private String url;

    /** Título o nombre descriptivo del marcador. */
    private String titulo;

    /** Nombre de la categoría a la que pertenece este marcador. */
    private String categoria;

    /* Usuario al que le pertenece*/
    private String usuario;

    /**
     * Construye un marcador vacío con cadenas vacías en todos sus campos.
     * Útil para inicializar arreglos o colecciones antes de asignar valores.
     */
    public Marcador() {
        this.url = "";
        this.titulo = "";
        this.categoria = "";
        this.usuario = "";
    }

    /**
     * Construye un marcador con todos sus campos especificados.
     *
     * @param url       URL del sitio; no debe ser {@code null}
     * @param titulo    nombre o título del marcador; no debe ser {@code null}
     * @param categoria nombre de la categoría a la que pertenece; no debe ser {@code null}
     */
    public Marcador(String url, String titulo, String categoria, String usuario) {
        this.url = url;
        this.titulo = titulo;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    // -------------------------------------------------------------------------
    // Getters y setters
    // -------------------------------------------------------------------------

    /**
     * Retorna la URL del marcador.
     *
     * @return URL del sitio
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la URL del marcador.
     *
     * @param url nueva URL; no debe ser {@code null}
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Retorna el título del marcador.
     *
     * @return título o nombre descriptivo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del marcador.
     *
     * @param titulo nuevo título; no debe ser {@code null}
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Retorna el nombre de la categoría a la que pertenece este marcador.
     *
     * @return nombre de la categoría
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del marcador.
     *
     * @param categoria nuevo nombre de categoría; no debe ser {@code null}
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

    /**
     * Imprime en consola la información del marcador en formato legible.
     */
    public void mostrarInfo() {
        System.out.print("Nombre: " + titulo + " | URL: " + url + " | Categoria: " + categoria);
    }

    /**
     * Retorna el título del marcador como representación en cadena,
     * coherente con cómo se muestra en listas y árboles.
     *
     * @return título del marcador
     */
    @Override
    public String toString() {
        return this.titulo;
    }
}
