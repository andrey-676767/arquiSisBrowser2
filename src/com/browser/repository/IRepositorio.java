package com.browser.repository;

import com.browser.structures.interfaces.IEstructuraDeDatos;

/**
 * Contrato genérico para el acceso a datos de cualquier entidad del sistema.
 *
 * <p>Aplica el patrón <em>Repository</em>: abstrae los detalles de la fuente
 * de persistencia (SQLite, JSON, en memoria, etc.) detrás de una interfaz
 * uniforme, permitiendo intercambiar implementaciones sin modificar los
 * servicios que las consumen.</p>
 *
 * <p>Junto con la inyección de dependencias realizada en {@code App}, este
 * diseño satisface el principio Open/Closed: agregar una nueva fuente de datos
 * (p. ej. un repositorio REST) solo requiere una nueva clase que implemente
 * esta interfaz, sin tocar el código existente.</p>
 *
 * @param <T> el tipo de entidad gestionada por el repositorio
 */
public interface IRepositorio<T> {

    /**
     * Persiste una nueva entidad en el almacenamiento subyacente.
     *
     * @param dato la entidad a guardar; no debe ser {@code null}
     */
    void guardar(T dato);

    /**
     * Recupera todas las entidades disponibles en el almacenamiento.
     *
     * @return una {@link IEstructuraDeDatos} con todas las entidades; nunca {@code null}
     *         (puede estar vacía)
     */
    IEstructuraDeDatos<T> cargarTodos();

    /**
     * Recupera las entidades disponibles en el almacenamiento según un parámetro.
     *
     * @param dato la entidad que se busca, normalmente strings.
     * 
     * @return una {@link IEstructuraDeDatos} con todas las entidades; nunca {@code null}
     *         (puede estar vacía)
     */
    IEstructuraDeDatos<T> cargarSegun(T dato);

    /**
     * Elimina la entidad identificada por {@code id} del almacenamiento.
     * Si el identificador no existe, la operación no tiene efecto.
     *
     * @param id identificador único de la entidad a eliminar
     */
    void borrar(String id);

    /**
     * Reemplaza los datos de una entidad ya existente.
     * La entidad se localiza mediante su identificador interno.
     *
     * @param dato la entidad con los nuevos valores; su identificador debe
     *             coincidir con un registro existente
     */
    void actualizar(T dato);

}
