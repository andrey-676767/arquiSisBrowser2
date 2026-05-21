package com.browser.repository;

import com.browser.model.Marcador;
import com.browser.structures.ListaDoble;
import com.browser.structures.interfaces.IEstructuraDeDatos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Implementación de {@link IRepositorio} que persiste entidades {@link Marcador}
 * en un archivo JSON local.
 *
 * <p>El archivo contiene un arreglo JSON donde cada objeto representa un
 * marcador con los campos {@code titulo}, {@code url} y {@code categoria}.
 * La serialización y deserialización se realizan manualmente para evitar
 * dependencias externas, siguiendo el estilo del proyecto original.</p>
 *
 * <p>El identificador usado en {@link #borrar(String)} y {@link #actualizar(Marcador)}
 * es el <strong>título</strong> del marcador, ya que actúa como clave natural
 * en el dominio del simulador.</p>
 *
 * <p>Cada operación de escritura reescribe el archivo completo a partir de
 * la lista en memoria, lo que es adecuado para el volumen de datos del
 * simulador y evita la complejidad de una edición parcial de JSON.</p>
 */
public class JSONRepo implements IRepositorio<Marcador> {

    /** Ruta al archivo JSON donde se persisten los marcadores. */
    private final String loc;

    /** Caché en memoria sincronizada con el archivo en cada operación. */
    private IEstructuraDeDatos<Marcador> cache;

    /**
     * Construye el repositorio apuntando a la ubicación indicada.
     * Si el archivo no existe, se crea vacío. Si existe, se cargan
     * los marcadores al iniciar.
     *
     * @param loc ruta al archivo JSON, p. ej. {@code "marcadores.json"}
     */
    public JSONRepo(String loc) {
        this.loc = loc;
        this.cache = new ListaDoble<>();
        inicializar();
    }

    /**
     * Crea el archivo JSON si no existe, o carga su contenido en la caché
     * si ya existe.
     */
    private void inicializar() {
        File archivo = new File(loc);
        if (!archivo.exists()) {
            escribirArchivo(new ListaDoble<>());
            System.out.println("-> [JSON] Archivo creado: " + loc);
        } else {
            this.cache = leerArchivo();
            System.out.println("-> [JSON] Marcadores cargados desde: " + loc);
        }
    }

    // -------------------------------------------------------------------------
    // IRepositorio
    // -------------------------------------------------------------------------

    /**
     * Agrega un nuevo {@link Marcador} al archivo JSON y a la caché.
     * Si ya existe un marcador con el mismo título, la operación no tiene efecto.
     *
     * @param dato el marcador a persistir; no debe ser {@code null}
     */
    @Override
    public void guardar(Marcador dato) {
        for (int i = 0; i < cache.cantidad(); i++) {
            if (cache.obtener(i).getTitulo().equals(dato.getTitulo())) {
                System.out.println("-> [JSON] Marcador ya existe: " + dato.getTitulo());
                return;
            }
        }
        cache.insertar(dato);
        IEstructuraDeDatos<Marcador> todos_marcadores = this.leerArchivo();
        todos_marcadores.insertar(dato);
        escribirArchivo(todos_marcadores);
        System.out.println("-> [JSON] Marcador guardado: " + dato.getTitulo());
    }

    /**
     * Retorna todos los marcadores almacenados en el archivo JSON.
     *
     * @return {@link ListaDoble} con todos los {@link Marcador}; nunca {@code null}
     */
    @Override
    public IEstructuraDeDatos<Marcador> cargarTodos() {
        this.cache = leerArchivo();
        return cache;
    }


    /* 
    *Carga según un usuario
    *
    */
    @Override
    public IEstructuraDeDatos<Marcador> cargarSegun(Marcador dato) {
        IEstructuraDeDatos<Marcador> lista = new ListaDoble<>();
        try {
            String contenido = new String(
                    Files.readAllBytes(Paths.get(loc)), StandardCharsets.UTF_8);

            // Extraer cada bloque { ... }
            int pos = 0;
            while (pos < contenido.length()) {
                int inicio = contenido.indexOf('{', pos);
                int fin = contenido.indexOf('}', inicio);
                if (inicio == -1 || fin == -1) break;

                String bloque = contenido.substring(inicio + 1, fin);
                String titulo = extraerValor(bloque, "titulo");
                String url = extraerValor(bloque, "url");
                String categoria = extraerValor(bloque, "categoria");
                String usuario = extraerValor(bloque, "user");

                if (titulo != null && url != null && categoria != null && usuario.equals(dato.getUsuario())) {
                    lista.insertar(new Marcador(url, titulo, categoria, usuario));
                }
                pos = fin + 1;
            }
        } catch (IOException e) {
            System.out.println("-> [JSON] Error al leer archivo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina el marcador cuyo título coincide con {@code id}.
     * Si no existe, la operación no tiene efecto.
     *
     * @param id título del marcador a eliminar (clave natural)
     */
    @Override
    public void borrar(String id) {
        for (int i = 0; i < cache.cantidad(); i++) {
            if (cache.obtener(i).getTitulo().equals(id)) {
                cache.remover(i);
                escribirArchivo(cache);
                System.out.println("-> [JSON] Marcador eliminado: " + id);
                return;
            }
        }
        System.out.println("-> [JSON] Marcador no encontrado: " + id);
    }

    /**
     * Reemplaza los datos del marcador cuyo título coincide con
     * {@code dato.getTitulo()}.
     * Si no existe, la operación no tiene efecto.
     *
     * @param dato marcador con el título (clave) y los nuevos valores
     */
    @Override
    public void actualizar(Marcador dato) {
        for (int i = 0; i < cache.cantidad(); i++) {
            Marcador m = cache.obtener(i);
            if (m.getTitulo().equals(dato.getTitulo())) {
                m.setUrl(dato.getUrl());
                m.setCategoria(dato.getCategoria());
                escribirArchivo(cache);
                System.out.println("-> [JSON] Marcador actualizado: " + dato.getTitulo());
                return;
            }
        }
        System.out.println("-> [JSON] Marcador no encontrado para actualizar: " + dato.getTitulo());
    }

    // -------------------------------------------------------------------------
    // Serialización / deserialización manual
    // -------------------------------------------------------------------------

    /**
     * Serializa la lista de marcadores y la escribe en el archivo JSON.
     * El formato es un arreglo de objetos JSON con los campos
     * {@code titulo}, {@code url} y {@code categoria}.
     *
     * @param lista lista de marcadores a serializar
     */
    private void escribirArchivo(IEstructuraDeDatos<Marcador> lista) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < lista.cantidad(); i++) {
            Marcador m = lista.obtener(i);
            sb.append("  {\n")
              .append("    \"titulo\": \"").append(escapar(m.getTitulo())).append("\",\n")
              .append("    \"url\": \"").append(escapar(m.getUrl())).append("\",\n")
              .append("    \"categoria\": \"").append(escapar(m.getCategoria())).append("\",\n")
              .append("    \"user\": \"").append(escapar(m.getUsuario())).append("\"\n")
              .append("  }");
            if (i < lista.cantidad() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try {
            Files.write(Paths.get(loc),
                    sb.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("-> [JSON] Error al escribir archivo: " + e.getMessage());
        }
    }

    /**
     * Lee y deserializa el archivo JSON, devolviendo una lista de marcadores.
     * El parser es manual y simple: asume el formato producido por
     * {@link #escribirArchivo}.
     *
     * @return lista de {@link Marcador} deserializados; vacía si hay error
     *         o el archivo está vacío
     */
    private IEstructuraDeDatos<Marcador> leerArchivo() {
        
        IEstructuraDeDatos<Marcador> lista = new ListaDoble<>();
        try {
            String contenido = new String(
                    Files.readAllBytes(Paths.get(loc)), StandardCharsets.UTF_8);

            // Extraer cada bloque { ... }
            int pos = 0;
            while (pos < contenido.length()) {
                int inicio = contenido.indexOf('{', pos);
                int fin = contenido.indexOf('}', inicio);
                if (inicio == -1 || fin == -1) break;

                String bloque = contenido.substring(inicio + 1, fin);
                String titulo = extraerValor(bloque, "titulo");
                String url = extraerValor(bloque, "url");
                String categoria = extraerValor(bloque, "categoria");
                String usuario = extraerValor(bloque, "user");

                if (titulo != null && url != null && categoria != null && usuario != null) {
                    lista.insertar(new Marcador(url, titulo, categoria, usuario));
                }
                pos = fin + 1;
            }
        } catch (IOException e) {
            System.out.println("-> [JSON] Error al leer archivo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Extrae el valor de una clave JSON del formato {@code "clave": "valor"}
     * dentro de un bloque de texto.
     *
     * @param bloque fragmento de texto JSON de un objeto
     * @param clave  nombre del campo a extraer
     * @return el valor del campo, o {@code null} si no se encuentra
     */
    private String extraerValor(String bloque, String clave) {
        String patron = "\"" + clave + "\": \"";
        int inicio = bloque.indexOf(patron);
        if (inicio == -1) return null;
        inicio += patron.length();
        int fin = bloque.indexOf("\"", inicio);
        if (fin == -1) return null;
        return bloque.substring(inicio, fin);
    }

    /**
     * Escapa caracteres especiales JSON en una cadena de texto.
     *
     * @param valor cadena a escapar
     * @return cadena con {@code "} y {@code \} escapados
     */
    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
