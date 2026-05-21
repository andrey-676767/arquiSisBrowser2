package com.browser.services;

import com.browser.auth.IAutenticador;
import com.browser.model.Usuario;
import com.browser.repository.IRepositorio;
import com.browser.structures.ListaDoble;

/**
 * Servicio que encapsula la lógica de negocio relacionada con la gestión
 * de usuarios y la autenticación en el simulador de navegación.
 *
 * <p>Actúa como fachada entre el {@code BrowserController} y las capas
 * de repositorio y autenticación, delegando la persistencia en
 * {@link IRepositorio} y la validación de credenciales en {@link IAutenticador}.</p>
 *
 * <p>Las dependencias se reciben por constructor (patrón
 * <em>Dependency Injection</em>), lo que facilita el testeo y el intercambio
 * de implementaciones sin modificar esta clase.</p>
 */
public class UsuarioService {

    /** Repositorio donde se persisten los usuarios. */
    private final IRepositorio<Usuario> db;

    /** Mecanismo de autenticación (login / logout). */
    private final IAutenticador autenticador;

    /** Caché en memoria de los usuarios cargados del repositorio. */
    private ListaDoble<Usuario> usuarios;

    /** Usuario con sesión activa; {@code null} si no hay sesión. */
    private Usuario usuarioActual;

    /**
     * Construye el servicio con las dependencias inyectadas.
     * Carga inmediatamente todos los usuarios desde el repositorio.
     *
     * @param db           repositorio de usuarios; no debe ser {@code null}
     * @param autenticador implementación de autenticación; no debe ser {@code null}
     */
    public UsuarioService(IRepositorio<Usuario> db, IAutenticador autenticador) {
        this.db = db;
        this.autenticador = autenticador;
        this.usuarioActual = null;
        this.usuarios = db.cargarTodos();
    }

    // -------------------------------------------------------------------------
    // Autenticación
    // -------------------------------------------------------------------------

    public void registrarse(String user, String pass) {
        autenticador.registrar(user, pass);
    }

    /**
     * Delega el inicio de sesión al {@link IAutenticador} y,
     * si tiene éxito, actualiza la referencia de {@code usuarioActual}.
     *
     * @param nombre nombre de usuario
     * @param contrasena contraseña en texto plano
     */
    public void iniciarSesion(String nombre, String contrasena) {
        autenticador.login(nombre, contrasena);
        // Buscar el usuario en caché local para asignarlo como actual
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.obtener(i);
            if (u.getNombre().equals(nombre)) {
                this.usuarioActual = u;
                return;
            }
        }
    }

    /**
     * Delega el cierre de sesión al {@link IAutenticador} y limpia
     * la referencia al usuario actual.
     */
    public void cerrarSesion() {
        autenticador.logout();
        this.usuarioActual = null;
    }

    // -------------------------------------------------------------------------
    // CRUD de usuarios
    // -------------------------------------------------------------------------

    /**
     * Registra un nuevo usuario en el repositorio y en la caché local.
     * Si ya existe un usuario con el mismo nombre, la operación no tiene efecto.
     *
     * @param nombre     nombre de usuario; debe ser único
     * @param contrasena contraseña del nuevo usuario
     */
    public void agregar(String nombre, String contrasena) {
        // Verificar unicidad en caché antes de persistir
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.obtener(i).getNombre().equals(nombre)) {
                System.out.println("-> [UsuarioService] Ya existe un usuario con ese nombre.");
                return;
            }
        }
        Usuario nuevo = new Usuario(nombre, contrasena);
        db.guardar(nuevo);
        usuarios.insertar(nuevo);
        System.out.println("-> [UsuarioService] Usuario agregado: " + nombre);
    }

    /**
     * Elimina el usuario identificado por {@code nombre} del repositorio
     * y de la caché local.
     * Si el usuario eliminado tiene sesión activa, esta se cierra automáticamente.
     *
     * @param nombre identificador del usuario a eliminar
     */
    public void borrar(String nombre) {
        db.borrar(nombre);
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.obtener(i).getNombre().equals(nombre)) {
                usuarios.remover(i);
                break;
            }
        }
        if (usuarioActual != null && usuarioActual.getNombre().equals(nombre)) {
            System.out.println("-> [UsuarioService] El usuario activo fue eliminado; cerrando sesión.");
            cerrarSesion();
        }
        System.out.println("-> [UsuarioService] Usuario eliminado: " + nombre);
    }

    /**
     * Actualiza los datos del usuario con sesión activa.
     * Solo se puede editar el usuario que actualmente tiene sesión.
     *
     * @param nuevoNombre    nuevo nombre de usuario (puede ser el mismo para
     *                       cambiar solo la contraseña)
     * @param nuevaContrasena nueva contraseña
     */
    public void editar(String nuevoNombre, String nuevaContrasena) {
        if (usuarioActual == null) {
            System.out.println("-> [UsuarioService] No hay sesión activa para editar.");
            return;
        }
        String nombreAnterior = usuarioActual.getNombre();
        // Si cambia el nombre, borrar el registro anterior y crear uno nuevo
        if (!nombreAnterior.equals(nuevoNombre)) {
            db.borrar(nombreAnterior);
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.obtener(i).getNombre().equals(nombreAnterior)) {
                    usuarios.remover(i);
                    break;
                }
            }
            usuarioActual.setNombre(nuevoNombre);
            usuarioActual.setContrasena(nuevaContrasena);
            db.guardar(usuarioActual);
            usuarios.insertar(usuarioActual);
        } else {
            usuarioActual.setContrasena(nuevaContrasena);
            db.actualizar(usuarioActual);
        }
        System.out.println("-> [UsuarioService] Usuario actualizado: " + nuevoNombre);
    }

    // -------------------------------------------------------------------------
    // Consultas
    // -------------------------------------------------------------------------

    /**
     * Retorna el usuario con sesión activa.
     *
     * @return el {@link Usuario} autenticado, o {@code null} si no hay sesión
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Retorna la lista completa de usuarios cargada desde el repositorio.
     *
     * @return {@link ListaDoble} con todos los usuarios; nunca {@code null}
     */
    public ListaDoble<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Indica si hay una sesión activa en este momento.
     *
     * @return {@code true} si {@code usuarioActual} no es {@code null}
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
}
