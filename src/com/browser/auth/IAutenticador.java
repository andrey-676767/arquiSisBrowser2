package com.browser.auth;

/**
 * Contrato para los mecanismos de autenticación de usuarios.
 *
 * <p>Permite intercambiar estrategias de autenticación (validación en memoria,
 * contra base de datos, contra un servicio externo, etc.) sin modificar
 * los servicios que las consumen, siguiendo el principio Open/Closed.</p>
 *
 * <p>La implementación concreta del simulador es {@link UValidator}, que
 * valida credenciales contra el repositorio SQLite de usuarios.</p>
 */
public interface IAutenticador {

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * <p>La implementación debe verificar que el usuario exista y que la
     * contraseña coincida. En caso de error, se recomienda lanzar una
     * excepción de runtime o devolver retroalimentación al servicio llamante.</p>
     *
     * @param user nombre de usuario
     * @param pass contraseña en texto plano (el hashing es responsabilidad
     *             de la implementación)
     */
    void login(String user, String pass);

    /**
     * Cierra la sesión del usuario actualmente autenticado.
     *
     * <p>Tras este llamado, cualquier operación que requiera autenticación
     * debe rechazarse hasta que se invoque {@link #login} de nuevo.</p>
     */
    void logout();

    void registrar(String user, String pass);
}
