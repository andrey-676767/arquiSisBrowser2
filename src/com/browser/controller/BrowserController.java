package com.browser.controller;

import com.browser.model.Marcador;
import com.browser.model.Tab;
import com.browser.services.DescargaManager;
import com.browser.services.IDescargaManager;
import com.browser.services.MarcadorService;
import com.browser.services.UsuarioService;
import com.browser.model.tabs.ITabHistory;
import com.browser.model.tabs.ITabManager;
import com.browser.repository.IRepositorio;
import com.browser.validator.IURLValidator;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Controlador principal del simulador de navegador.
 *
 * <h3>Rol en la arquitectura</h3>
 * <p>{@code BrowserController} implementa la capa de presentación/control
 * de la arquitectura por capas. Traduce las entradas del usuario (consola)
 * en llamadas a los servicios de negocio, y muestra los resultados de
 * vuelta en la consola.</p>
 *
 * <h3>Principios aplicados</h3>
 * <ul>
 *   <li><b>DIP</b>: recibe todas sus dependencias como interfaces por
 *       constructor; nunca instancia servicios concretos.</li>
 *   <li><b>SRP</b>: cada método privado gestiona exactamente un menú o
 *       submenú del flujo de la aplicación.</li>
 *   <li><b>Facade</b>: actúa como fachada entre la vista (consola) y el
 *       conjunto de servicios de negocio.</li>
 * </ul>
 *
 * <p>La instancia se construye y se inicia desde {@link com.browser.app.App},
 * que es la única clase que conoce las implementaciones concretas.</p>
 *
 * @author Refactorización Fase 6
 * @version 1.0
 * @see com.browser.app.App
 */
public class BrowserController {

    // ── Dependencias (inyectadas por constructor) ──────────────────────────────

    /** Servicio de gestión de marcadores. */
    private final MarcadorService marcadores;

    /** Servicio de gestión de usuarios y autenticación. */
    private final UsuarioService usuarios;

    /** Gestor de ciclo de vida de pestañas y grupos. */
    private final ITabManager tabManager;

    /** Acceso al historial de navegación de la pestaña activa. */
    private final ITabHistory tabHistory;

    /** Gestor de descargas activas. */
    private final IDescargaManager descargaManager;

    /** Validador y consultador de URLs externas. */
    private final IURLValidator validator;

    /** Scanner compartido; se crea una sola vez para todo el ciclo de vida. */
    private final Scanner scanner;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Construye el controlador con todas sus dependencias ya resueltas.
     *
     * <p>El mismo objeto {@code TabService} puede pasarse tanto como
     * {@code ITabManager} como {@code ITabHistory}, ya que implementa
     * ambas interfaces.</p>
     *
     * @param marcadores     Servicio de marcadores.
     * @param usuarios       Servicio de usuarios.
     * @param tabManager     Gestor de pestañas y grupos.
     * @param tabHistory     Historial de navegación de la pestaña activa.
     * @param descargaManager Gestor de descargas.
     * @param validator      Validador de URLs.
     */
    public BrowserController(
            MarcadorService marcadores,
            UsuarioService usuarios,
            ITabManager tabManager,
            ITabHistory tabHistory,
            IDescargaManager descargaManager,
            IURLValidator validator,
            IRepositorio<Marcador> repoMarcadores) {

        this.marcadores      = marcadores;
        this.usuarios        = usuarios;
        this.tabManager      = tabManager;
        this.tabHistory      = tabHistory;
        this.descargaManager = descargaManager;
        this.validator       = validator;
        this.scanner         = new Scanner(System.in);
    }

    // ── Ciclo principal ───────────────────────────────────────────────────────

    /**
     * Inicia el bucle principal de la aplicación.
     *
     * <p>Muestra el menú principal repetidamente hasta que el usuario
     * seleccione la opción de salida. Cada iteración renderiza el estado
     * actual (grupo, pestañas, URL activa) antes de leer la elección.</p>
     */
    public void ejecutar() {
        boolean salir = false;

        while (!salir) {
            mostrarEncabezado();
            mostrarMenuPrincipal();
            int eleccion = leerInt(1, 9);

            switch (eleccion) {
                case 1 -> navegar();
                case 2 -> tabHistory.atras();
                case 3 -> tabHistory.adelante();
                case 4 -> gestionarMarcadores();
                case 5 -> gestionarPestanas();
                case 6 -> gestionarGrupos();
                case 7 -> autenticar();
                case 8 -> gestionarDescargas();
                case 9 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }

        System.out.println("Cerrando el navegador...");
        scanner.close();
    }

    // ── Navegación ────────────────────────────────────────────────────────────

    /**
     * Solicita una URL al usuario, la valida contra la API externa y, si es
     * accesible, navega a ella en la pestaña activa.
     */
    private void navegar() {
        System.out.print("URL: ");
        String url = scanner.nextLine().trim();

        System.out.println("-> [RED] Consultando información del servidor...");
        String infoJson = validator.obtenerInfoServer(url);
        System.out.println("-> [RESPUESTA API]: " + infoJson);

        if (validator.esAccesible(url)) {
            System.out.println("-> [OK] Conexión establecida. Navegando...");
            tabHistory.setUrl(url);
        } else {
            System.out.println("-> [ERROR] No se pudo verificar la procedencia del sitio.");
        }
    }

    // ── Marcadores ────────────────────────────────────────────────────────────

    /**
     * Muestra y gestiona el menú de marcadores.
     *
     * <p>Delega en {@link MarcadorService} todas las operaciones de
     * persistencia y lógica de negocio sobre marcadores.</p>
     */
    private void gestionarMarcadores() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n    === Administrador de Marcadores ===");
            marcadores.mostrarMarcadores();
            System.out.println("""
                    
                    0. Volver.
                    1. Agregar marcador.
                    2. Borrar marcador.
                    3. Editar marcador.
                    4. Ver todos los marcadores.
                    5. Visitar un marcador.
                    """);

            int op = leerInt(0, 5);
            switch (op) {
                case 0 -> volver = true;
                case 1 -> agregarMarcador();
                case 2 -> borrarMarcador();
                case 3 -> editarMarcador();
                case 4 -> marcadores.mostrarMarcadores();
                case 5 -> visitarMarcador();
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Recoge los datos del usuario y delega en {@link MarcadorService} la
     * creación y persistencia de un nuevo marcador.
     */
    private void agregarMarcador() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("URL: ");
        String url = scanner.nextLine().trim();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine().trim();

        marcadores.agregar(titulo, url, categoria);
        System.out.println("Marcador guardado.");
    }

    /**
     * Recoge los datos de identificación y delega en {@link MarcadorService}
     * la eliminación del marcador indicado.
     */
    private void borrarMarcador() {
        System.out.println("Mostrando marcadores (agrupados por categoría):");
        marcadores.mostrarMarcadores();

        System.out.print("Categoría del marcador a borrar: ");
        String cat = scanner.nextLine().trim();
        System.out.print("Título del marcador a borrar: ");
        String titulo = scanner.nextLine().trim();

        marcadores.borrar(titulo, cat);
    }

    /**
     * Recoge los nuevos datos y delega en {@link MarcadorService} la
     * modificación del marcador indicado.
     */
    private void editarMarcador() {
        System.out.println("Mostrando marcadores (agrupados por categoría):");
        marcadores.mostrarMarcadores();

        System.out.print("Categoría del marcador a editar: ");
        String cat = scanner.nextLine().trim();
        System.out.print("Título del marcador a editar: ");
        String titulo = scanner.nextLine().trim();

        System.out.println("""
                ¿Qué desea editar?
                1. Título.
                2. URL.
                3. Categoría.
                """);
        int op = leerInt(1, 3);
        switch (op) {
            case 1 -> {
                System.out.print("Nuevo título: ");
                marcadores.editar(titulo, cat, "titulo", scanner.nextLine().trim());
            }
            case 2 -> {
                System.out.print("Nueva URL: ");
                marcadores.editar(titulo, cat, "url", scanner.nextLine().trim());
            }
            case 3 -> {
                System.out.print("Nueva categoría: ");
                marcadores.editar(titulo, cat, "categoria", scanner.nextLine().trim());
            }
        }
    }

    /**
     * Permite al usuario elegir un marcador y navegar a su URL en la
     * pestaña activa.
     */
    private void visitarMarcador() {
        System.out.println("Mostrando marcadores (agrupados por categoría):");
        marcadores.mostrarMarcadores();

        System.out.print("Título del marcador a visitar: ");
        String titulo = scanner.nextLine().trim();

        Marcador m = marcadores.buscarMarcador(titulo);
        if (m == null) {
            System.out.println("Marcador no encontrado.");
            return;
        }
        tabHistory.setUrl(m.getUrl());
        System.out.println("Navegando a: " + m.getUrl());
    }

    // ── Pestañas ──────────────────────────────────────────────────────────────

    /**
     * Muestra y gestiona el menú de pestañas del grupo activo.
     */
    private void gestionarPestanas() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n    === Administrador de Pestañas ===");
            tabManager.getTabs().toString();
            System.out.println("""
                    
                    0. Volver.
                    1. Abrir nueva pestaña.
                    2. Cerrar pestaña actual.
                    3. Cambiar de pestaña.
                    4. Guardar URL actual en marcadores.
                    """);

            int op = leerInt(0, 4);
            switch (op) {
                case 0 -> volver = true;
                case 1 -> {
                    tabManager.agregar();
                    System.out.println("Nueva pestaña abierta.");
                    volver = true;
                }
                case 2 -> {
                    tabManager.cerrar();
                    System.out.println("Pestaña cerrada.");
                    volver = true;
                }
                case 3 -> {
                    tabManager.getTabs().toString();
                    System.out.print("Número de pestaña (base 0): ");
                    int idx = leerIntRango(0, tabManager.getTabs().cantidad() - 1);
                    tabManager.setTabActual(idx);
                    volver = true;
                }
                case 4 -> guardarTabEnMarcadores();
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Guarda la URL de la pestaña activa como un marcador nuevo,
     * solicitando título y categoría al usuario.
     */
    private void guardarTabEnMarcadores() {
        String url = tabHistory.getUrl();
        if (url == null || url.isBlank()) {
            System.out.println("La pestaña actual no tiene URL guardada.");
            return;
        }
        System.out.println("URL: " + url);
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine().trim();
        marcadores.agregar(titulo, url, categoria);
        System.out.println("Marcador guardado.");
    }

    // ── Grupos de pestañas ────────────────────────────────────────────────────

    /**
     * Muestra y gestiona el menú de grupos de pestañas.
     */
    private void gestionarGrupos() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n    === Administrador de Grupos ===");
            System.out.println("Grupos activos: " + tabManager.getCantidadGrupos());
            System.out.println("""
                    
                    0. Volver.
                    1. Crear nuevo grupo.
                    2. Cambiar de grupo.
                    3. Cambiar pestaña dentro del grupo actual.
                    """);

            int op = leerInt(0, 3);
            switch (op) {
                case 0 -> volver = true;
                case 1 -> {
                    tabManager.nuevoGrupo();
                    System.out.println("Nuevo grupo creado.");
                    volver = true;
                }
                case 2 -> {
                    System.out.print("Número de grupo (base 0): ");
                    int idx = leerIntRango(0, tabManager.getCantidadGrupos() - 1);
                    tabManager.setGrupoActual(idx);
                    volver = true;
                }
                case 3 -> {
                    tabManager.getTabs().toString();
                    System.out.print("Número de pestaña (base 0): ");
                    int idx = leerIntRango(0, tabManager.getTabs().cantidad() - 1);
                    tabManager.setTabActual(idx);
                    volver = true;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    // ── Autenticación ─────────────────────────────────────────────────────────

    /**
     * Muestra el menú de inicio de sesión y, si las credenciales son
     * válidas, carga los marcadores del usuario en memoria.
     */
    private void autenticar() {
        boolean exito = false;

        while (!exito) {
            System.out.println("\n    === Inicio de Sesión ===");
            System.out.print("Usuario: ");
            String user = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine().trim();

            try {
                usuarios.iniciarSesion(user, pass);
                System.out.println("Sesión iniciada correctamente.");
                marcadores.cargarEnArbol();
                exito = true;
            } catch (Exception e) {
                System.out.println("Credenciales incorrectas. Intente de nuevo.");
                System.out.println("(Escriba 'cancelar' en usuario para volver)");
                if (user.equalsIgnoreCase("cancelar")) return;
            }
        }
    }

    // ── Descargas ─────────────────────────────────────────────────────────────

    /**
     * Muestra y gestiona el menú de descargas activas.
     */
    private void gestionarDescargas() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n    === Administrador de Descargas ===");
            mostrarDescargas();
            System.out.println("""
                    
                    0. Volver.
                    1. Iniciar nueva descarga.
                    2. Borrar una descarga.
                    """);

            int op = leerInt(0, 2);
            switch (op) {
                case 0 -> volver = true;
                case 1 -> {
                    System.out.print("URL a descargar: ");
                    String url = scanner.nextLine().trim();
                    descargaManager.descargar(url);
                    System.out.println("Descarga iniciada: " + descargaManager.obtenerDescarga());
                }
                case 2 -> {
                    if (descargaManager.estaVacia()) {
                        System.out.println("No hay descargas activas.");
                        break;
                    }
                    System.out.print("Nombre de la descarga a borrar: ");
                    String nombre = scanner.nextLine().trim();
                    descargaManager.borrarDescarga(nombre);
                    System.out.println("Descarga eliminada.");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Imprime la lista de descargas activas o un mensaje si está vacía.
     */
    private void mostrarDescargas() {
        if (descargaManager.estaVacia()) {
            System.out.println("No hay descargas activas.");
            return;
        }
        // DescargaManager expone la lista para iteración; usamos el cast seguro
        if (descargaManager instanceof DescargaManager dm) {
            dm.getDescargas().mostrarVertical();
        }
    }

    // ── Utilidades de UI ──────────────────────────────────────────────────────

    /**
     * Muestra el encabezado de estado con el grupo activo, las pestañas
     * abiertas y la URL de la pestaña actual.
     */
    private void mostrarEncabezado() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("Grupo activo | Pestañas abiertas:");
        tabManager.getTabs().toString();
        Tab actual = tabManager.getTabActual();
        System.out.println("URL actual : " + actual.getUrl());
        System.out.println("Título     : " + actual.getTitulo());
        System.out.println("══════════════════════════════════════════");
    }

    /**
     * Imprime el menú principal de opciones.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("""
                
                Menú Principal:
                1. Navegar (buscar URL).
                2. Ir hacia atrás en la pestaña.
                3. Ir hacia adelante en la pestaña.
                4. Administrador de marcadores.
                5. Administrador de pestañas.
                6. Administrador de grupos.
                7. Iniciar sesión.
                8. Administrador de descargas.
                9. Salir.
                """);
    }

    /**
     * Lee un entero del scanner con manejo de errores.
     * Repite la solicitud hasta recibir un entero válido dentro del rango
     * [{@code min}, {@code max}].
     *
     * @param min Valor mínimo aceptado (inclusive).
     * @param max Valor máximo aceptado (inclusive).
     * @return El entero leído.
     */
    private int leerInt(int min, int max) {
        while (true) {
            try {
                int val = scanner.nextInt();
                scanner.nextLine();
                if (val >= min && val <= max) return val;
                System.out.printf("Seleccione un número entre %d y %d: %n", min, max);
            } catch (InputMismatchException e) {
                System.out.printf("Carácter no válido. Ingrese un número entre %d y %d: %n", min, max);
                scanner.nextLine();
            }
        }
    }

    /**
     * Variante de {@link #leerInt(int, int)} pensada para rangos que
     * se calculan dinámicamente (por ejemplo, el tamaño de una lista).
     *
     * @param min Valor mínimo aceptado (inclusive).
     * @param max Valor máximo aceptado (inclusive).
     * @return El entero leído.
     */
    private int leerIntRango(int min, int max) {
        return leerInt(min, max);
    }
}
