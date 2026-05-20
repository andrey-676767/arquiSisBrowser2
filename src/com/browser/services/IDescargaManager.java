package com.browser.services;

import com.browser.model.Descarga;

/**
 * Contrato para la gestión del administrador de descargas del navegador.
 *
 * <p>Define las operaciones que permiten iniciar descargas, eliminarlas y
 * recuperar la lista de descargas activas. Las clases que implementen esta
 * interfaz son libres de elegir la estructura interna de almacenamiento,
 * facilitando la extensión sin modificar el contrato (principio Open/Closed).</p>
 *
 * <p>El uso de esta interfaz permite al {@code BrowserController} depender
 * de una abstracción en lugar de una implementación concreta, en línea con
 * el principio de Inversión de Dependencias (DIP).</p>
 *
 * @author Refactorización Fase 5
 * @version 1.0
 * @see DescargaManager
 * @see Descarga
 */
public interface IDescargaManager {

    /**
     * Inicia una nueva descarga a partir de la URL indicada.
     *
     * <p>Se crea un objeto {@link Descarga} con el nombre derivado de la URL
     * y se añade a la cola de descargas activas.</p>
     *
     * @param url URL del recurso a descargar. No debe ser {@code null}.
     */
    void descargar(String url);

    /**
     * Elimina de la lista de descargas activas aquella cuyo nombre coincide
     * con el indicado.
     *
     * <p>Si no existe ninguna descarga con ese nombre, la operación no tiene
     * efecto.</p>
     *
     * @param nombre Nombre de la descarga a eliminar. No debe ser {@code null}.
     */
    void borrarDescarga(String nombre);

    /**
     * Retorna la descarga más reciente (la que se encuentra al frente de la
     * cola), sin eliminarla.
     *
     * @return La {@link Descarga} más reciente; {@code null} si no hay
     *         descargas activas.
     */
    Descarga obtenerDescarga();

    /**
     * Indica si la cola de descargas está vacía.
     *
     * @return {@code true} si no hay ninguna descarga activa; {@code false}
     *         en caso contrario.
     */
    boolean estaVacia();
}
