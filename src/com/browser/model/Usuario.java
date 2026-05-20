package com.browser.model;

/**
 * Entidad que representa a un usuario registrado en el simulador de navegación.
 *
 * <p>Almacena las credenciales mínimas necesarias para autenticación
 * (nombre de usuario y contraseña). La contraseña debe manejarse hasheada
 * en capas superiores; esta clase no impone restricciones sobre el formato.</p>
 *
 * <p>Implementa {@link Comparable} sobre el nombre para permitir su uso
 * en estructuras ordenadas como {@code ArbolBinario}.</p>
 */
public class Usuario implements Comparable<Usuario> {

    /** Nombre de usuario; actúa como identificador único. */
    private String nombre;

    /** Contraseña del usuario (debe almacenarse hasheada). */
    private String contrasena;

    /**
     * Construye un {@code Usuario} vacío.
     * Los campos quedan como cadenas vacías para evitar {@code null}.
     */
    public Usuario() {
        this.nombre = "";
        this.contrasena = "";
    }

    /**
     * Construye un {@code Usuario} con nombre y contraseña especificados.
     *
     * @param nombre    nombre de usuario; no debe ser {@code null}
     * @param contrasena contraseña del usuario; no debe ser {@code null}
     */
    public Usuario(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    // -------------------------------------------------------------------------
    // Getters y setters
    // -------------------------------------------------------------------------

    /**
     * Retorna el nombre de usuario.
     *
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param nombre el nuevo nombre; no debe ser {@code null}
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna la contraseña del usuario.
     *
     * @return contraseña (idealmente hasheada)
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasena la nueva contraseña; no debe ser {@code null}
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // -------------------------------------------------------------------------
    // Comparable / Object
    // -------------------------------------------------------------------------

    /**
     * Compara este usuario con otro por nombre en orden lexicográfico.
     *
     * @param otro el usuario con el que comparar
     * @return valor negativo, cero o positivo según el orden de los nombres
     */
    @Override
    public int compareTo(Usuario otro) {
        return this.nombre.compareTo(otro.nombre);
    }

    /**
     * Retorna una representación legible del usuario (sin exponer la contraseña).
     *
     * @return cadena con el formato {@code Usuario{nombre='...'}}
     */
    @Override
    public String toString() {
        return "Usuario{nombre='" + nombre + "'}";
    }
}
