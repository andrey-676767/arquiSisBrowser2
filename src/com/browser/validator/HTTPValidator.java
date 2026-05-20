package com.browser.validator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Implementación de {@link IURLValidator} que realiza peticiones HTTP reales
 * a la API pública {@code ip-api.com} para obtener información del servidor.
 *
 * <h3>Estrategia de validación</h3>
 * <ol>
 *   <li>Se extrae el dominio de la URL recibida eliminando el esquema y
 *       cualquier ruta.</li>
 *   <li>Se consulta {@code http://ip-api.com/json/{dominio}?fields=status,country,isp}.</li>
 *   <li>Si la respuesta HTTP es 200 y el campo {@code "status"} del JSON vale
 *       {@code "success"}, la URL se considera accesible.</li>
 * </ol>
 *
 * <p>El cliente HTTP se construye una sola vez en el constructor y se
 * reutiliza en todas las llamadas, evitando la sobrecarga de crear una nueva
 * instancia por petición.</p>
 *
 * <p>Esta clase no depende de librerías externas: usa únicamente
 * {@code java.net.http.HttpClient} (disponible desde Java 11).</p>
 *
 * @author Refactorización Fase 5
 * @version 1.0
 * @see IURLValidator
 */
public class HTTPValidator implements IURLValidator {

    /** Tiempo máximo de espera para establecer la conexión con el servidor externo. */
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    /** URL base de la API de geolocalización/información de dominios. */
    private static final String API_BASE = "http://ip-api.com/json/";

    /** Campos solicitados a la API (minimiza el tamaño de la respuesta). */
    private static final String API_FIELDS = "?fields=status,country,isp";

    /** Fragmento JSON que indica respuesta exitosa de la API. */
    private static final String STATUS_SUCCESS = "\"status\":\"success\"";

    /** Cliente HTTP reutilizable, configurado con el timeout definido. */
    private final HttpClient client;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Construye el validador inicializando el cliente HTTP compartido.
     */
    public HTTPValidator() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    // ── IURLValidator ─────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Extrae el dominio de {@code url}, lo consulta en {@code ip-api.com}
     * y retorna el cuerpo JSON de la respuesta. Si ocurre cualquier error de
     * red o de parseo, retorna un JSON de error con el mensaje correspondiente.</p>
     *
     * @param url URL completa a consultar.
     * @return JSON con {@code status}, {@code country} e {@code isp},
     *         o un JSON {@code {"error": "..."}} en caso de fallo.
     */
    @Override
    public String obtenerInfoServer(String url) {
        String dominio = extraerDominio(url);
        String apiUrl = API_BASE + dominio + API_FIELDS;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            }
            return "{\"error\": \"El servicio externo respondió con código " +
                    response.statusCode() + "\"}";

        } catch (Exception e) {
            return "{\"error\": \"Fallo de red: " + e.getMessage() + "\"}";
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Considera la URL accesible únicamente si la respuesta de la API
     * contiene el campo {@code "status":"success"}.</p>
     *
     * @param url URL completa a verificar.
     * @return {@code true} si el dominio es alcanzable; {@code false} en
     *         cualquier otro caso.
     */
    @Override
    public boolean esAccesible(String url) {
        String info = obtenerInfoServer(url);
        return info.contains(STATUS_SUCCESS);
    }

    // ── Utilidades privadas ───────────────────────────────────────────────────

    /**
     * Extrae el nombre de dominio a partir de una URL completa, eliminando
     * el esquema ({@code http://}, {@code https://}) y cualquier ruta posterior.
     *
     * <p>Ejemplos:
     * <pre>
     *   "https://www.google.com/search?q=java"  →  "www.google.com"
     *   "http://example.com"                    →  "example.com"
     * </pre>
     * </p>
     *
     * @param url URL completa de entrada.
     * @return Solo el nombre de dominio, sin esquema ni ruta.
     */
    private String extraerDominio(String url) {
        return url.replace("https://", "")
                  .replace("http://", "")
                  .split("/")[0];
    }
}
