package com.browser.auth;

import com.browser.model.Usuario;
import com.browser.repository.IRepositorio;
import com.browser.structures.ListaDoble;

/**
 * Implementación concreta de {@link IAutenticador} que valida credenciales
 * contra el repositorio de usuarios registrado en el sistema.
 *
 * <p>Recibe un {@link IRepositorio}{@code <Usuario>} por inyección de dependencias,
 * lo que le permite funcionar con cualquier fuente de datos (SQLite, JSON, memoria)
 * sin acoplarse a una implementación concreta.</p>
 *
 * <p>Mantiene internamente una referencia al {@link Usuario} que tiene sesión
 * activa; {@code null} indica que no hay sesión.</p>
 */
public class UValidator implements IAutenticador {

    /** Repositorio del que se cargan los usuarios para validación. */
    private final IRepositorio<Usuario> repositorio;

    /** Usuario con sesión activa, o {@code null} si no hay sesión. */
    private Usuario usuarioActual;

    /**
     * Construye el validador con el repositorio de usuarios inyectado.
     *
     * @param repositorio fuente de datos de usuarios; no debe ser {@code null}
     */
    public UValidator(IRepositorio<Usuario> repositorio) {
        this.repositorio = repositorio;
        this.usuarioActual = null;
    }

    @Override
    public void registrar(String user, String pass) {
        Usuario usuario = new Usuario(user, pass);
        this.repositorio.guardar(usuario);
    }

    /**
     * Inicia sesión verificando que exista un usuario con el nombre y
     * contraseña indicados en el repositorio.
     *
     * <p>Si las credenciales son correctas, {@link #getUsuarioActual()} pasa
     * a retornar el usuario autenticado. Si son incorrectas, imprime un
     * mensaje de error y {@code usuarioActual} permanece como {@code null}.</p>
     *
     * @param user nombre de usuario a autenticar
     * @param pass contraseña en texto plano a verificar
     */
    @Override
    public void login(String user, String pass) {
        ListaDoble<Usuario> usuarios = repositorio.cargarTodos();
        if (usuarios.empty()) {
            System.out.println("-> [AUTH] ¡No hay usuarios registrados!");
        }
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.obtener(i);
            if (u.getNombre().equals(user) && u.getContrasena().equals(pass)) {
                this.usuarioActual = u;
                System.out.println("-> [AUTH] Sesión iniciada: " + u.getNombre());
                return;
            }
        }
        System.out.println("-> [AUTH] Credenciales incorrectas o inexistentes.");
    }

    /**
     * Cierra la sesión del usuario actual.
     * Después de este llamado, {@link #getUsuarioActual()} retorna {@code null}.
     */
    @Override
    public void logout() {
        if (usuarioActual != null) {
            System.out.println("-> [AUTH] Sesión cerrada: " + usuarioActual.getNombre());
            usuarioActual = null;
        } else {
            System.out.println("-> [AUTH] No hay sesión activa.");
        }
    }

    /**
     * Indica si hay una sesión activa en este momento.
     *
     * @return {@code true} si hay un usuario autenticado
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }

    /**
     * Retorna el usuario con sesión activa.
     *
     * @return el {@link Usuario} autenticado, o {@code null} si no hay sesión
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
