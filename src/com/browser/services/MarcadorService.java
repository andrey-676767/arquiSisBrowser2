package com.browser.services;

import com.browser.model.Categoria;
import com.browser.model.Marcador;
import com.browser.repository.IRepositorio;
import com.browser.structures.ArbolBinario;
import com.browser.structures.ListaDoble;

/**
 * Servicio que encapsula la lógica de negocio para la gestión de marcadores.
 *
 * <p>Mantiene un {@code ArbolBinario<Categoria>} como estructura principal
 * en memoria: las categorías se ordenan alfabéticamente por inicial y cada
 * nodo agrupa los marcadores de esa categoría. El árbol se reconstruye
 * al iniciar desde el repositorio persistente.</p>
 *
 * <p>La persistencia se delega en un {@link IRepositorio}{@code <Marcador>},
 * inyectado por constructor, lo que permite cambiar la fuente de datos
 * (JSON, SQLite, etc.) sin modificar este servicio.</p>
 */
public class MarcadorService {

    /** Repositorio donde se persisten los marcadores. */
    private final IRepositorio<Marcador> db;

    /**
     * Árbol binario de búsqueda que organiza las categorías en orden
     * alfabético por inicial de nombre.
     */
    private ArbolBinario<Categoria> marcadores;

    /**
     * Construye el servicio con el repositorio inyectado y reconstruye
     * el árbol de categorías cargando todos los marcadores persistidos.
     *
     * @param db repositorio de marcadores; no debe ser {@code null}
     */
    public MarcadorService(IRepositorio<Marcador> db) {
        this.db = db;
        cargarEnArbol();
    }

    // -------------------------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------------------------

    /**
     * Carga todos los marcadores del repositorio y los inserta en el árbol
     * de categorías. Si el repositorio está vacío se inicializa el árbol
     * con una categoría vacía por defecto.
     */
    private void cargarEnArbol() {
        ListaDoble<Marcador> todos = db.cargarTodos();

        if (todos.size() == 0) {
            Marcador placeholder = new Marcador("", "inicio", "General");
            marcadores = new ArbolBinario<>(new Categoria("G", placeholder));
            return;
        }

        // Insertar el primer elemento como raíz
        Marcador primero = todos.obtener(0);
        Categoria raizCat = new Categoria(
                primero.getCategoria().substring(0, 1).toUpperCase(), primero);
        marcadores = new ArbolBinario<>(raizCat);

        // Insertar el resto
        for (int i = 1; i < todos.size(); i++) {
            insertarEnArbol(todos.obtener(i));
        }
        System.out.println("-> [MarcadorService] Árbol reconstruido con " + todos.size() + " marcadores.");
    }

    /**
     * Inserta un {@link Marcador} en el árbol de categorías.
     * Si la categoría ya existe, agrega el marcador a su lista;
     * si no, crea una nueva categoría y la inserta en el árbol.
     *
     * @param m el marcador a insertar
     */
    private void insertarEnArbol(Marcador m) {
        String inicial = m.getCategoria().substring(0, 1).toUpperCase();
        Categoria aux = new Categoria(inicial, null);

        if (!marcadores.buscar(aux)) {
            marcadores.insertarOrdenado(new Categoria(inicial, m));
        } else {
            marcadores.obtener(aux).contenido.insertarAlFinal(m);
        }
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Agrega un nuevo marcador al árbol en memoria y lo persiste en el repositorio.
     * Si ya existe un marcador con el mismo título en la misma categoría,
     * la operación no tiene efecto.
     *
     * @param titulo    título del marcador; actúa como identificador natural
     * @param url       URL del sitio
     * @param categoria nombre de la categoría destino
     */
    public void agregar(String titulo, String url, String categoria) {
        String inicial = categoria.substring(0, 1).toUpperCase();
        Categoria aux = new Categoria(inicial, null);

        // Verificar duplicado
        if (marcadores.buscar(aux)) {
            Categoria existente = marcadores.obtener(aux);
            if (existente.obtener(titulo) != null) {
                System.out.println("-> [MarcadorService] El marcador ya existe: " + titulo);
                return;
            }
        }

        Marcador nuevo = new Marcador(url, titulo, categoria);
        db.guardar(nuevo);
        insertarEnArbol(nuevo);
        System.out.println("-> [MarcadorService] Marcador agregado: " + titulo);
    }

    /**
     * Elimina el marcador identificado por {@code titulo} dentro de la
     * categoría indicada, tanto del árbol en memoria como del repositorio.
     * Si la categoría queda vacía tras la eliminación, se elimina del árbol.
     *
     * @param titulo    título del marcador a eliminar
     * @param categoria nombre de la categoría que contiene el marcador
     */
    public void borrar(String titulo, String categoria) {
        String inicial = categoria.substring(0, 1).toUpperCase();
        Categoria aux = new Categoria(inicial, null);

        if (!marcadores.buscar(aux)) {
            System.out.println("-> [MarcadorService] Categoría no encontrada: " + categoria);
            return;
        }

        Categoria cat = marcadores.obtener(aux);
        if (cat.obtener(titulo) == null) {
            System.out.println("-> [MarcadorService] Marcador no encontrado: " + titulo);
            return;
        }

        cat.borrar(titulo);
        db.borrar(titulo);

        if (cat.contenido.size() == 0) {
            marcadores.eliminarOrdenadoA(aux);
            System.out.println("-> [MarcadorService] Categoría vacía eliminada: " + categoria);
        }
        System.out.println("-> [MarcadorService] Marcador eliminado: " + titulo);
    }

    /**
     * Edita los atributos de un marcador existente identificado por su título actual.
     * Si el marcador no existe, la operación no tiene efecto.
     *
     * <p>Si se cambia la categoría, el marcador se mueve al nodo correspondiente
     * del árbol (creando la categoría destino si no existe) y se elimina de la
     * categoría original.</p>
     *
     * @param tituloActual  título actual del marcador (clave de búsqueda)
     * @param nuevoTitulo   nuevo título; puede ser el mismo si solo se editan otros campos
     * @param nuevaUrl      nueva URL
     * @param nuevaCategoria nueva categoría destino
     */
    public void editar(String tituloActual, String nuevoTitulo,
                       String nuevaUrl, String nuevaCategoria) {
        // Buscar en qué categoría está actualmente
        Categoria catActual = buscarCategoriaDeMarcador(tituloActual);
        if (catActual == null) {
            System.out.println("-> [MarcadorService] Marcador no encontrado: " + tituloActual);
            return;
        }

        // Extraer, actualizar y reinsertar
        Marcador m = catActual.pop(tituloActual);
        if (m == null) return;

        db.borrar(tituloActual);

        m.setTitulo(nuevoTitulo);
        m.setUrl(nuevaUrl);
        m.setCategoria(nuevaCategoria);

        // Limpiar categoría original si quedó vacía
        if (catActual.contenido.size() == 0) {
            String inicialAnterior = catActual.getNombre().substring(0, 1).toUpperCase();
            marcadores.eliminarOrdenadoA(new Categoria(inicialAnterior, null));
        }

        db.guardar(m);
        insertarEnArbol(m);
        System.out.println("-> [MarcadorService] Marcador editado: " + nuevoTitulo);
    }

    // -------------------------------------------------------------------------
    // Consultas
    // -------------------------------------------------------------------------

    /**
     * Retorna el {@code ArbolBinario<Categoria>} completo para ser recorrido
     * o consultado por el controlador (p. ej. para listar o filtrar).
     *
     * @return árbol de categorías con todos los marcadores en memoria
     */
    public ArbolBinario<Categoria> getArbol() {
        return marcadores;
    }

    /**
     * Busca el marcador con el título dado recorriendo todas las categorías
     * del árbol en in-orden.
     *
     * @param titulo el título a buscar
     * @return el {@link Marcador} encontrado, o {@code null} si no existe
     */
    public Marcador buscarMarcador(String titulo) {
        return buscarEnArbol(marcadores.raiz, titulo);
    }

    /**
     * Recorre el árbol en in-orden buscando el marcador con el título dado.
     *
     * @param nodo  nodo actual del recorrido recursivo
     * @param titulo título a buscar
     * @return el marcador encontrado, o {@code null}
     */
    private Marcador buscarEnArbol(
            com.browser.structures.NodoArbol<Categoria> nodo, String titulo) {
        if (nodo == null) return null;
        Marcador izq = buscarEnArbol(nodo.getHijoIzquierda(), titulo);
        if (izq != null) return izq;
        Marcador encontrado = nodo.getInfo().obtener(titulo);
        if (encontrado != null) return encontrado;
        return buscarEnArbol(nodo.getHijoDerecha(), titulo);
    }

    /**
     * Busca la {@link Categoria} que contiene el marcador con el título dado,
     * recorriendo el árbol en in-orden.
     *
     * @param titulo título del marcador a localizar
     * @return la {@link Categoria} que lo contiene, o {@code null}
     */
    private Categoria buscarCategoriaDeMarcador(String titulo) {
        return buscarCatEnArbol(marcadores.raiz, titulo);
    }

    /**
     * Recorre el árbol en in-orden buscando la categoría que contiene
     * el marcador con el título dado.
     *
     * @param nodo  nodo actual del recorrido recursivo
     * @param titulo título a buscar
     * @return la categoría que contiene el marcador, o {@code null}
     */
    private Categoria buscarCatEnArbol(
            com.browser.structures.NodoArbol<Categoria> nodo, String titulo) {
        if (nodo == null) return null;
        Categoria izq = buscarCatEnArbol(nodo.getHijoIzquierda(), titulo);
        if (izq != null) return izq;
        if (nodo.getInfo().obtener(titulo) != null) return nodo.getInfo();
        return buscarCatEnArbol(nodo.getHijoDerecha(), titulo);
    }

    public void mostrarMarcadores() {
        marcadores.recorrerInOrden(marcadores.raiz);
    }
}
