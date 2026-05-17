package com.browser.model;

import com.browser.structures.Pila;
package dominio.entidades;

/**
 * Entidad que representa una pestaña del navegador simulado.
 *
 * <p>Cada pestaña mantiene su estado actual (título y URL) y dos
 * historiales de navegación implementados con {@link Pila}:
 * uno para retroceder ({@code historialAtras}) y otro para avanzar
 * ({@code historialAdelante}).</p>
 *
 * <p>Cada entrada del historial es un arreglo {@code String[2]} donde
 * la posición 0 guarda el título y la posición 1 guarda la URL,
 * preservando exactamente el comportamiento del código original.</p>
 *
 * <p>No depende de ninguna colección nativa Java.</p>
 *
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Tab {

    /**
     * Estado actual de la pestaña: {@code info[0]} = título,
     * {@code info[1]} = URL.
     */
    private String[] info;

    /** Pila de estados anteriores para la función "atrás". */
    private Pila<String[]> historialAtras;

    /** Pila de estados siguientes para la función "adelante". */
    private Pila<String[]> historialAdelante;

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

    // ── Navegación ───────────────────────────────────────────────────────────

    /**
     * Navega a la URL indicada, guardando el estado actual en el historial
     * de retroceso y limpiando el historial de avance.
     *
     * <p>El título se genera automáticamente como la URL en mayúsculas,
     * replicando el comportamiento original.</p>
     *
     * @param url Nueva URL a visitar.
     */
    public void setUrl(String url) {
        String[] estadoActual = this.info.clone();
        this.historialAtras.push(estadoActual);
        this.historialAdelante.limpiar();
        this.info[1] = url;
        this.info[0] = url.toUpperCase();
    }

    /**
     * Retrocede al estado anterior si existe historial de retroceso.
     * El estado actual se guarda en el historial de avance.
     */
    public void atras() {
        if (this.historialAtras.empty()) return;
        String[] estadoPrevio = this.historialAtras.pop();
        String[] estadoActual = this.info.clone();
        this.historialAdelante.push(estadoActual);
        this.info[0] = estadoPrevio[0];
        this.info[1] = estadoPrevio[1];
    }

    /**
     * Avanza al estado siguiente si existe historial de avance.
     * El estado actual se guarda en el historial de retroceso.
     */
    public void adelante() {
        if (this.historialAdelante.empty()) return;
        String[] estadoSiguiente = this.historialAdelante.pop();
        String[] estadoActual = this.info.clone();
        this.historialAtras.push(estadoActual);
        this.info[0] = estadoSiguiente[0];
        this.info[1] = estadoSiguiente[1];
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Retorna el título actual de la pestaña.
     *
     * @return El título almacenado en {@code info[0]}.
     */
    public String getTitulo() {
        return this.info[0];
    }

    /**
     * Retorna la URL actual de la pestaña.
     *
     * @return La URL almacenada en {@code info[1]}.
     */
    public String getUrl() {
        return this.info[1];
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    /**
     * Retorna el título de la pestaña como representación textual.
     * Usado por {@link infraestructura.estructuras.ListaDoble#mostrarLista()}.
     *
     * @return El título de la pestaña.
     */
    @Override
    public String toString() {
        return this.info[0];
    }
}