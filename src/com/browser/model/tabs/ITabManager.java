package com.browser.model.tabs;

import com.browser.model.Tab;
import com.browser.services.TabService;
import com.browser.structures.ListaDoble;
import com.browser.structures.interfaces.IEstructuraDeDatos;

/**
 * Contrato para la gestión del ciclo de vida de pestañas y grupos de pestañas.
 *
 * <p>Define las operaciones necesarias para abrir, cerrar y cambiar entre
 * pestañas, así como para organizar pestañas en grupos (tab groups).
 * Un grupo es una colección lógica de pestañas relacionadas.</p>
 *
 * <p>La separación de este contrato del servicio concreto ({@link TabService})
 * permite sustituir la implementación sin afectar al controlador, en
 * concordancia con el principio de Inversión de Dependencias (DIP).</p>
 *
 * @author Refactorización Fase 4
 * @version 1.0
 * @see TabService
 * @see Tab
 */
public interface ITabManager {

    /**
     * Abre una nueva pestaña en el grupo activo y la establece como pestaña
     * actual.
     */
    void agregar();

    /**
     * Cierra la pestaña actualmente activa del grupo en uso.
     * Si el grupo queda vacío tras el cierre, se crea automáticamente una
     * pestaña nueva para evitar un estado inconsistente.
     */
    void cerrar();

    /**
     * Crea un nuevo grupo de pestañas vacío (con una pestaña inicial) y lo
     * establece como grupo activo.
     */
    void nuevoGrupo();

    /**
     * Retorna la pestaña activa dentro del grupo actual.
     *
     * @return La pestaña actualmente en uso; nunca {@code null}.
     */
    Tab getTabActual();

    /**
     * Establece la pestaña activa dentro del grupo actual a partir de su
     * índice (base 0).
     *
     * @param index Índice de la pestaña a activar.
     * @throws IndexOutOfBoundsException si el índice no corresponde a ninguna
     *                                   pestaña del grupo activo.
     */
    void setTabActual(int index);

    /**
     * Retorna la lista completa de pestañas del grupo activo.
     *
     * @return {@link ListaDoble} con todas las pestañas abiertas en el grupo
     *         actual; nunca {@code null}.
     */
    IEstructuraDeDatos<Tab> getTabs();

    /**
     * Retorna la lista completa de grupos de pestañas.
     *
     * @return {@link ListaDoble} con todos los grupos; nunca {@code null}.
     */
    IEstructuraDeDatos<Tab> getGrupos();

    /**
     * Retorna el número de grupos de pestañas existentes.
     *
     * @return Cantidad de grupos abiertos (≥ 1).
     */
    int getCantidadGrupos();

    /**
     * Cambia el grupo activo al indicado por su índice (base 0).
     *
     * @param index Índice del grupo a activar.
     * @throws IndexOutOfBoundsException si el índice no corresponde a ningún
     *                                   grupo.
     */
    void setGrupoActual(int index);
}
