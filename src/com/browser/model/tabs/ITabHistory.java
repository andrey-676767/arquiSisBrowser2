package com.browser.model.tabs;

/**
 * Contrato para la gestión del historial de navegación de una pestaña.
 *
 * <p>Define las operaciones fundamentales para navegar hacia atrás y adelante
 * en el historial de URLs visitadas por una pestaña, así como la lectura y
 * escritura de la URL actual.</p>
 *
 * <p>Las clases que implementen esta interfaz son libres de elegir la
 * estructura de datos interna (pilas, listas, etc.), favoreciendo la
 * adherencia al principio Open/Closed: el comportamiento puede extenderse
 * sin modificar el contrato.</p>
 *
 * @author Refactorización Fase 4
 * @version 1.0
 * @see TabService
 */
public interface ITabHistory {

    /**
     * Navega a la URL indicada, guardando el estado anterior en el historial
     * de retroceso y limpiando el historial de avance.
     *
     * @param url Nueva URL a visitar. No debe ser {@code null}.
     */
    void setUrl(String url);

    /**
     * Retorna la URL actualmente cargada en la pestaña.
     *
     * @return La URL actual; cadena vacía si aún no se ha navegado a ningún sitio.
     */
    String getUrl();

    /**
     * Retorna el título de la página actualmente cargada.
     *
     * @return El título actual de la pestaña.
     */
    String getTitulo();

    /**
     * Retrocede al estado de navegación anterior, si existe.
     * Si no hay historial de retroceso, la operación no tiene efecto.
     */
    void atras();

    /**
     * Avanza al estado de navegación siguiente, si existe.
     * Si no hay historial de avance, la operación no tiene efecto.
     */
    void adelante();
}
