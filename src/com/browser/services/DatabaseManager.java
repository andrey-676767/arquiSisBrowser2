package com.browser.services;

import com.browser.model.Categoria;
import com.browser.model.Marcador;
import com.browser.structures.ArbolBinario;

import java.sql.*;

public class DatabaseManager {
    // La URL de conexión al archivo local
    private static final String URL = "jdbc:sqlite:navegador.db";

    public DatabaseManager() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                // Creamos la tabla si no existe
                String sql = "CREATE TABLE IF NOT EXISTS marcadores (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT NOT NULL," +
                        "url TEXT NOT NULL," +
                        "categoria TEXT);";
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error DB Inicial: " + e.getMessage());
        }
    }

    public void guardarMarcador(String titulo, String url, String categoria) {
        String sql = "INSERT INTO marcadores(titulo, url, categoria) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, url);
            pstmt.setString(3, categoria);
            pstmt.executeUpdate();
            System.out.println("-> [DB] com.browser.model.Marcador guardado físicamente.");
        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public void cargarDatosEnArbol(ArbolBinario<Categoria> categorias) {
        String sql = "SELECT titulo, url, categoria FROM marcadores";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Sacamos los datos de la DB
                String tit = rs.getString("titulo");
                String u = rs.getString("url");
                String cat = rs.getString("categoria");

                // Los insertamos en tu estructura actual
                Marcador m = new Marcador(u, tit, cat);
                Categoria aux = new Categoria(cat.substring(0, 1).toUpperCase(), m);

                // Aquí usas tu lógica original para reconstruir el árbol
                if (!categorias.buscar(aux)) {
                    categorias.insertarOrdenado(aux);
                } else {
                    categorias.obtener(aux).contenido.insertarAlFinal(m);
                }
            }
            System.out.println("-> [DB] Datos recuperados y cargados en el Árbol.");
        } catch (SQLException e) {
            System.out.println("Error al recuperar: " + e.getMessage());
        }
    }
}