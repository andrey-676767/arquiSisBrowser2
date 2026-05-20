package com.browser.repository;

import com.browser.model.Usuario;
import com.browser.structures.ListaDoble;

import java.sql.*;

/**
 * Implementación de {@link IRepositorio} que persiste entidades {@link Usuario}
 * en una base de datos SQLite local.
 *
 * <p>Gestiona la tabla {@code usuarios} con las columnas {@code nombre} (PK)
 * y {@code contrasena}. El constructor crea la tabla automáticamente si no
 * existe, siguiendo el mismo patrón que el {@code DatabaseManager} original.</p>
 *
 * <p>Cada método abre y cierra su propia conexión mediante
 * <em>try-with-resources</em> para garantizar la liberación del recurso
 * incluso ante excepciones.</p>
 */
public class SQLiteRepository implements IRepositorio<Usuario> {

    /** URL de conexión JDBC al archivo SQLite. */
    private final String url;

    /** Sentencia DDL para crear la tabla de usuarios si no existe. */
    private static final String SQL_CREAR_TABLA =
            "CREATE TABLE IF NOT EXISTS usuarios (" +
            "nombre TEXT PRIMARY KEY, " +
            "contrasena TEXT NOT NULL);";

    /**
     * Construye el repositorio apuntando al archivo SQLite indicado y crea
     * la tabla {@code usuarios} si no existe.
     *
     * @param url URL JDBC del archivo SQLite, p. ej.
     *            {@code "jdbc:sqlite:navegador.db"}
     */
    public SQLiteRepository(String url) {
        this.url = url;
        inicializarTabla();
    }

    /**
     * Crea la tabla {@code usuarios} en la base de datos si todavía no existe.
     * Se invoca una sola vez desde el constructor.
     */
    private void inicializarTabla() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(SQL_CREAR_TABLA);
        } catch (SQLException e) {
            System.out.println("-> [DB] Error al inicializar tabla usuarios: " + e.getMessage());
        }
    }

    /**
     * Inserta un nuevo {@link Usuario} en la base de datos.
     * Si ya existe un usuario con el mismo nombre, la operación no tiene efecto
     * (usa {@code INSERT OR IGNORE}).
     *
     * @param dato el usuario a persistir; {@code nombre} actúa como clave primaria
     */
    @Override
    public void guardar(Usuario dato) {
        String sql = "INSERT OR IGNORE INTO usuarios(nombre, contrasena) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dato.getNombre());
            pstmt.setString(2, dato.getContrasena());
            pstmt.executeUpdate();
            System.out.println("-> [DB] Usuario guardado: " + dato.getNombre());
        } catch (SQLException e) {
            System.out.println("-> [DB] Error al guardar usuario: " + e.getMessage());
        }
    }

    /**
     * Carga todos los usuarios registrados en la base de datos.
     *
     * @return {@link ListaDoble} con todos los {@link Usuario}; vacía si no hay registros
     */
    @Override
    public ListaDoble<Usuario> cargarTodos() {
        ListaDoble<Usuario> lista = new ListaDoble<>();
        String sql = "SELECT nombre, contrasena FROM usuarios";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String contrasena = rs.getString("contrasena");
                lista.insertar(new Usuario(nombre, contrasena));
            }
        } catch (SQLException e) {
            System.out.println("-> [DB] Error al cargar usuarios: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina el usuario cuyo nombre coincide con {@code id}.
     * Si no existe, la operación no tiene efecto.
     *
     * @param id nombre del usuario a eliminar (clave primaria)
     */
    @Override
    public void borrar(String id) {
        String sql = "DELETE FROM usuarios WHERE nombre = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                System.out.println("-> [DB] Usuario eliminado: " + id);
            } else {
                System.out.println("-> [DB] Usuario no encontrado: " + id);
            }
        } catch (SQLException e) {
            System.out.println("-> [DB] Error al borrar usuario: " + e.getMessage());
        }
    }

    /**
     * Actualiza la contraseña del usuario existente cuyo nombre coincide
     * con el del objeto {@code dato}.
     * Si el usuario no existe, la operación no tiene efecto.
     *
     * @param dato usuario con el nombre (clave) y los nuevos valores a persistir
     */
    @Override
    public void actualizar(Usuario dato) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE nombre = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dato.getContrasena());
            pstmt.setString(2, dato.getNombre());
            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                System.out.println("-> [DB] Usuario actualizado: " + dato.getNombre());
            } else {
                System.out.println("-> [DB] Usuario no encontrado para actualizar: " + dato.getNombre());
            }
        } catch (SQLException e) {
            System.out.println("-> [DB] Error al actualizar usuario: " + e.getMessage());
        }
    }
}
