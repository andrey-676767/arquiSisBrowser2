package com.browser.model;

import com.browser.structures.Pila;
import com.browser.structures.interfaces.IEstructuraDeDatos;
import com.browser.model.tabs.ITabHistory;

/**
 * Entidad que representa una pestaña del navegador simulado.
 *
 * <p>Cada pestaña mantiene su estado actual (título y URL) y dos historiales
 * de navegación implementados con {@link Pila}:
 * uno para retroceder ({@code historialAtras}) y otro para avanzar
 * ({@code historialAdelante}).</p>
 *
 * <p>Cada entrada del historial es un arreglo {@code String[2]} donde la
 * posición 0 guarda el título y la posición 1 guarda la URL, preservando el
 * comportamiento del código original.</p>
 *
 * <p>Implementa {@link ITabHistory} para que el controlador interactúe con
 * la pestaña a través del contrato en lugar de la implementación concreta,
 * conforme al principio de Inversión de Dependencias (DIP).</p>
 *
 * <p>No depende de ninguna colección nativa de Java.</p>
 *
 * @author Refactorización Fase 4
 * @version 2.0
 * @see ITabHistory
 * @see Pila
 */
public class Tab implements ITabHistory {

    /**
     * Estado actual de la pestaña: {@code info[0]} = título,
     * {@code info[1]} = URL.
     */
    private String[] info;

    /** Pila de estados anteriores para la función "atrás". */
    private IEstructuraDeDatos<String[]> historialAtras;

    /** Pila de estados siguientes para la función "adelante". */
    private IEstructuraDeDatos<String[]> historialAdelante;

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Construye una pestaña nueva con el título "Nueva Pestana" y URL vacía.
     * Ambos historiales comienzan vacíos.
     */
    public Tab() {
        this.info = new String[]{"Nueva Pestana", ""};
        this.historialAtras = new Pila<>();
        this.historialAdelante = new Pila<>();
    }

    // ── ITabHistory ──────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>El título se genera automáticamente como la URL en mayúsculas,
     * replicando el comportamiento original.</p>
     */
    @Override
    public void setUrl(String url) {
        String[] estadoActual = this.info.clone();
        this.historialAtras.push(estadoActual);
        this.historialAdelante.pop();
        this.info[1] = url;
        this.info[0] = url.toUpperCase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return this.info[1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitulo() {
        return this.info[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void atras() {
        if (this.historialAtras.empty()) return;
        String[] estadoPrevio = this.historialAtras.pop();
        String[] estadoActual = this.info.clone();
        this.historialAdelante.push(estadoActual);
        this.info[0] = estadoPrevio[0];
        this.info[1] = estadoPrevio[1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void adelante() {
        if (this.historialAdelante.empty()) return;
        String[] estadoSiguiente = this.historialAdelante.pop();
        String[] estadoActual = this.info.clone();
        this.historialAtras.push(estadoActual);
        this.info[0] = estadoSiguiente[0];
        this.info[1] = estadoSiguiente[1];
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    /**
     * Retorna el título de la pestaña como representación textual.
     * Utilizado por {@link com.browser.structures.ListaDoble#mostrarLista()}.
     *
     * @return El título de la pestaña.
     */
    @Override
    public String toString() {
        return this.info[0];
    }
}
