package com.browser.services;

import com.browser.model.Descarga;
import com.browser.structures.ListaDoble;
import com.browser.structures.interfaces.IEstructuraDeDatos;

/**
 * Servicio que gestiona la cola de descargas activas del navegador.
 *
 * <p>Implementa {@link IDescargaManager} almacenando las descargas en una
 * {@link ListaDoble}. Las nuevas descargas se insertan al frente de la lista
 * (las más recientes primero) y las finalizadas se eliminan por nombre.</p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *   <li>Crear objetos {@link Descarga} a partir de una URL.</li>
 *   <li>Mantener la lista de descargas activas.</li>
 *   <li>Exponer la descarga más reciente sin consumirla.</li>
 *   <li>Eliminar una descarga por nombre cuando el usuario lo solicite.</li>
 * </ul>
 *
 * <p>El nombre de cada descarga se deriva del último segmento de la URL
 * (la parte posterior a la última barra {@code /}), o de la URL completa
 * si no contiene barras.</p>
 *
 * <p>Las dependencias se reciben por constructor (inyección de dependencias),
 * lo que facilita las pruebas unitarias y el reemplazo de la estructura
 * interna sin modificar esta clase.</p>
 *
 * @author Refactorización Fase 5
 * @version 1.0
 * @see IDescargaManager
 * @see Descarga
 */
public class DescargaManager implements IDescargaManager {

    /**
     * Lista de descargas activas. El elemento en la posición 0 es siempre
     * la descarga más reciente.
     */
    private final IEstructuraDeDatos<Descarga> descargas;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Construye el administrador con una cola de descargas vacía.
     */
    public DescargaManager() {
        this.descargas = new ListaDoble<>();
    }

    // ── IDescargaManager ──────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Crea un objeto {@link Descarga} cuyo nombre es el último segmento
     * de {@code url} (parte tras la última {@code /}) e inserta la descarga
     * al frente de la lista para que aparezca como la más reciente.</p>
     *
     * @param url URL del recurso a descargar.
     */
    @Override
    public void descargar(String url) {
        String nombre = extraerNombre(url);
        Descarga descarga = new Descarga(nombre);
        descargas.insertarAlPrincipio(descarga);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Recorre la lista buscando la primera descarga cuyo nombre coincida
     * exactamente con {@code nombre} y la elimina. Si no existe ninguna
     * coincidencia, la operación no tiene ningún efecto.</p>
     *
     * @param nombre Nombre de la descarga a eliminar.
     */
    @Override
    public void borrarDescarga(String nombre) {
        for (int i = 0; i < descargas.cantidad(); i++) {
            Descarga d = descargas.obtener(i);
            if (d != null && d.getNombre().equals(nombre)) {
                descargas.remover(i);
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Retorna el elemento en la posición 0 (la descarga más reciente)
     * sin eliminarlo de la lista.</p>
     *
     * @return La descarga más reciente, o {@code null} si la cola está vacía.
     */
    @Override
    public Descarga obtenerDescarga() {
        if (descargas.cantidad() == 0) return null;
        return descargas.obtener(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean estaVacia() {
        return descargas.cantidad() == 0;
    }

    /**
     * Retorna la lista completa de descargas activas.
     *
     * <p>Útil para que el controlador pueda mostrar todas las descargas
     * al usuario sin necesidad de iterar manualmente con {@link #obtenerDescarga()}.</p>
     *
     * @return La {@link ListaDoble} interna de descargas; nunca {@code null}.
     */
    public IEstructuraDeDatos<Descarga> getDescargas() {
        return descargas;
    }

    // ── Utilidades privadas ───────────────────────────────────────────────────

    /**
     * Extrae el nombre del archivo o recurso a partir de la URL, tomando el
     * fragmento posterior a la última barra {@code /}.
     *
     * <p>Si la URL no contiene barras (o el último segmento está vacío),
     * se devuelve la URL completa como nombre.</p>
     *
     * <p>Ejemplos:
     * <pre>
     *   "https://example.com/files/report.pdf"  →  "report.pdf"
     *   "http://example.com"                    →  "http://example.com"
     *   "https://example.com/carpeta/"          →  "https://example.com/carpeta/"
     * </pre>
     * </p>
     *
     * @param url URL completa del recurso.
     * @return Nombre derivado de la URL.
     */
    private String extraerNombre(String url) {
        String[] partes = url.split("/");
        String ultimo = partes[partes.length - 1];
        return (ultimo == null || ultimo.isBlank()) ? url : ultimo;
    }
}
