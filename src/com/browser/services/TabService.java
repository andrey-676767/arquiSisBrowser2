package com.browser.services;

import com.browser.model.Tab;
import com.browser.structures.ListaDoble;
import com.browser.structures.interfaces.IEstructuraDeDatos;
import com.browser.model.tabs.ITabHistory;
import com.browser.model.tabs.ITabManager;

/**
 * Servicio que gestiona las pestañas y grupos de pestañas del navegador.
 *
 * <p>Implementa simultáneamente {@link ITabManager} e {@link ITabHistory},
 * de modo que el controlador puede trabajar con la pestaña activa a través
 * de {@code ITabHistory} sin necesidad de extraer la instancia de {@link Tab}
 * explícitamente.</p>
 *
 * <h3>Modelo de datos</h3>
 * <ul>
 *   <li>Los grupos se representan como una {@link ListaDoble} de
 *       {@code ListaDoble<Tab>}.</li>
 *   <li>Cada grupo es a su vez una {@link ListaDoble} de {@link Tab}.</li>
 *   <li>Se mantiene un puntero al grupo activo ({@code grupoActual}) y al
 *       índice de la pestaña activa dentro de ese grupo
 *       ({@code indexTabActual}).</li>
 * </ul>
 *
 * <p>Las dependencias se reciben por constructor (inyección de dependencias),
 * lo que facilita las pruebas unitarias y el mantenimiento.</p>
 *
 * @author Refactorización Fase 4
 * @version 1.0
 * @see ITabManager
 * @see ITabHistory
 * @see Tab
 */
public class TabService implements ITabManager, ITabHistory {

    // ── Estado ────────────────────────────────────────────────────────────────

    /**
     * Colección de grupos de pestañas. Cada elemento es un grupo
     * ({@code IEstructuraDeDatos<Tab>}).
     */
    private IEstructuraDeDatos<IEstructuraDeDatos<Tab>> grupos;

    /**
     * Grupo de pestañas activo en este momento.
     */
    private IEstructuraDeDatos<Tab> grupoActual;

    /**
     * Índice (base 0) de la pestaña activa dentro de {@code grupoActual}.
     */
    private int indexTabActual;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Construye el servicio con un primer grupo que contiene una pestaña
     * inicial vacía. El servicio queda listo para usarse sin ninguna
     * configuración adicional.
     */
    public TabService() {
        this.grupos = new ListaDoble<>();
        this.grupoActual = new ListaDoble<>();

        // Pestaña inicial obligatoria para que el estado nunca sea vacío
        Tab tabInicial = new Tab();
        this.grupoActual.insertar(tabInicial);
        this.grupos.insertar(grupoActual);
        this.indexTabActual = 0;
    }

    // ── ITabManager ───────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>La nueva pestaña se inserta al final del grupo activo y se convierte
     * en la pestaña activa.</p>
     */
    @Override
    public void agregar() {
        Tab nuevaTab = new Tab();
        grupoActual.insertar(nuevaTab);
        indexTabActual = grupoActual.cantidad() - 1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Si el grupo activo queda vacío tras el cierre, se inserta
     * automáticamente una nueva pestaña para mantener el invariante de que
     * siempre existe al menos una pestaña.</p>
     */
    @Override
    public void cerrar() {
        if (grupoActual.empty()) return;

        grupoActual.remover(indexTabActual);

        // Invariante: el grupo nunca queda vacío
        if (grupoActual.cantidad() == 0) {
            grupoActual.insertar(new Tab());
            indexTabActual = 0;
            return;
        }

        // Ajustar el índice si apuntaba al último elemento
        if (indexTabActual >= grupoActual.cantidad()) {
            indexTabActual = grupoActual.cantidad() - 1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>El nuevo grupo se inicializa con una pestaña vacía y se convierte en
     * el grupo activo, con {@code indexTabActual} apuntando a esa primera
     * pestaña.</p>
     */
    @Override
    public void nuevoGrupo() {
        ListaDoble<Tab> nuevoGrupo = new ListaDoble<>();
        nuevoGrupo.insertar(new Tab());
        grupos.insertar(nuevoGrupo);
        grupoActual = nuevoGrupo;
        indexTabActual = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tab getTabActual() {
        return grupoActual.obtener(indexTabActual);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTabActual(int index) {
        if (index < 0 || index >= grupoActual.cantidad()) {
            throw new IndexOutOfBoundsException(
                    "Índice de pestaña fuera de rango: " + index +
                    " (tamaño del grupo: " + grupoActual.cantidad() + ")"
            );
        }
        indexTabActual = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IEstructuraDeDatos<Tab> getTabs() {
        return grupoActual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCantidadGrupos() {
        return grupos.cantidad();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGrupoActual(int index) {
        if (index < 0 || index >= grupos.cantidad()) {
            throw new IndexOutOfBoundsException(
                    "Índice de grupo fuera de rango: " + index +
                    " (total grupos: " + grupos.cantidad() + ")"
            );
        }
        grupoActual = grupos.obtener(index);
        // La pestaña activa dentro del grupo nuevo siempre empieza en la primera
        indexTabActual = 0;
    }

    // ── ITabHistory (delegación a la Tab activa) ──────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Delega en la pestaña activa del grupo actual.</p>
     */
    @Override
    public void setUrl(String url) {
        getTabActual().setUrl(url);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega en la pestaña activa del grupo actual.</p>
     */
    @Override
    public String getUrl() {
        return getTabActual().getUrl();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega en la pestaña activa del grupo actual.</p>
     */
    @Override
    public String getTitulo() {
        return getTabActual().getTitulo();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega en la pestaña activa del grupo actual.</p>
     */
    @Override
    public void atras() {
        getTabActual().atras();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delega en la pestaña activa del grupo actual.</p>
     */
    @Override
    public void adelante() {
        getTabActual().adelante();
    }
}
