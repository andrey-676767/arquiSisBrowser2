package com.browser.model;

/**
 * Entidad que representa a un usuario registrado en el simulador de navegador.
 *
 * <p>Aunque el código fuente original no incluía la clase {@code Usuario},
 * el diagrama de arquitectura objetivo ({@code BrowserController},
 * {@code UsuarioService}, {@code SQLiteRepository}) la requiere como entidad
 * de dominio para la Fase 2 (autenticación y persistencia de marcadores
 * por usuario). Se introduce aquí para completar la capa de dominio y
 * permitir que {@code ListaDoble<Usuario>} sea tipada correctamente.</p>
 *
 * <p>Encapsula nombre de usuario y contraseña. La contraseña debe ser
 * tratada como un hash en capas superiores — esta clase solo la almacena
 * como cadena.</p>
 *
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Usuario {

    /** Nombre único que identifica al usuario. */
    private String nombre;

    /**
     * Contraseña del usuario.
     * <strong>Nota:</strong> en la Fase 2 este campo deberá almacenar
     * un hash (BCrypt, SHA-256, etc.), nunca la contraseña en texto plano.
     */
    private String contrasena;

    // ── Constructores ────────────────────────────────────────────────────────

    /**
     * Construye un usuario con credenciales vacías.
     * Útil como placeholder en colecciones o para pruebas.
     */
    public Usuario() {
        this.nombre = "";
        this.contrasena = "";
    }

    /**
     * Construye un usuario con nombre y contraseña definidos.
     *
     * @param nombre    Nombre único del usuario.
     * @param contrasena Contraseña (idealmente hasheada antes de pasar aquí).
     */
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Retorna el nombre del usuario.
     *
     * @return El nombre almacenado.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna la contraseña del usuario.
     *
     * @return La contraseña almacenada.
     */
    public String getContrasena() {
        return contrasena;
    }

    // ── Setters ──────────────────────────────────────────────────────────────

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre Nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasena Nueva contraseña (debe ser hasheada antes de invocar).
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    /**
     * Retorna el nombre del usuario como representación textual.
     *
     * @return El nombre del usuario.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}