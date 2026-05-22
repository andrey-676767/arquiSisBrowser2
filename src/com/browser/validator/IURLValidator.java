package com.browser.validator;

/**
 * Contrato para la validación y consulta de información de URLs externas.
 *
 * <p>Define las operaciones necesarias para verificar si una URL es
 * accesible desde la red y para obtener metadatos del servidor al que
 * apunta (país, ISP, estado HTTP, etc.).</p>
 *
 * <p>La existencia de esta interfaz desacopla al {@code BrowserController}
 * de cualquier implementación concreta (HTTP real, mock de pruebas, etc.),
 * en línea con el principio de Inversión de Dependencias (DIP).</p>
 *
 * @author Refactorización Fase 5
 * @version 1.0
 * @see HTTPValidator
 */
public interface IURLValidator {

    /**
     * Consulta información del servidor al que apunta la URL indicada.
     *
     * <p>La respuesta es un JSON con campos como {@code status},
     * {@code country} e {@code isp}, o un objeto de error si la consulta
     * falla.</p>
     *
     * @param url URL completa (con esquema {@code http://} o {@code https://})
     *            cuyo servidor se desea consultar. No debe ser {@code null}.
     * @return Cadena JSON con la información del servidor; nunca {@code null}.
     */
    String obtenerInfoServer(String url);

    /**
     * Determina si la URL indicada es accesible en este momento.
     *
     * <p>Una URL se considera accesible cuando la consulta al servidor
     * retorna un estado {@code success} sin errores de red.</p>
     *
     * @param url URL completa a verificar. No debe ser {@code null}.
     * @return {@code true} si el servidor responde correctamente;
     *         {@code false} en cualquier otro caso (error de red,
     *         dominio inexistente, timeout, etc.).
     */
    boolean esAccesible(String url);
}
