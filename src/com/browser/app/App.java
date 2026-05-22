package com.browser.app;

import com.browser.auth.IAutenticador;
import com.browser.auth.UValidator;
import com.browser.controller.BrowserController;
import com.browser.repository.IRepositorio;
import com.browser.repository.JSONRepo;
import com.browser.repository.SQLiteRepository;
import com.browser.services.DescargaManager;
import com.browser.services.IDescargaManager;
import com.browser.services.MarcadorService;
import com.browser.services.TabService;
import com.browser.services.UsuarioService;
import com.browser.model.Marcador;
import com.browser.model.Usuario;
import com.browser.validator.HTTPValidator;
import com.browser.validator.IURLValidator;

/**
 * Punto de entrada de la aplicación del simulador de navegador.
 *
 * <h3>Responsabilidad única</h3>
 * <p>{@code App} es la única clase autorizada a conocer las implementaciones
 * concretas de todos los módulos. Su único trabajo es:</p>
 * <ol>
 *   <li>Crear los repositorios ({@link #crearRepos()}).</li>
 *   <li>Crear los servicios de negocio ({@link #crearServicios()}).</li>
 *   <li>Inyectar las dependencias en el controlador ({@link #inyectarDeps()})
 *       y arrancarlo.</li>
 * </ol>
 *
 * <p>Ningún otro componente del sistema crea instancias concretas; todos
 * reciben sus colaboradores a través de interfaces. Esto implementa el
 * patrón <em>Dependency Injection</em> descrito en el documento de
 * arquitectura y garantiza que cambiar una implementación (por ejemplo,
 * sustituir {@link JSONRepo} por otro repositorio) solo requiere modificar
 * este archivo.</p>
 *
 * @author Refactorización Fase 6
 * @version 1.0
 * @see BrowserController
 */
public class App {

    // ── Repositorios ──────────────────────────────────────────────────────────

    /** Repositorio de marcadores (JSON). */
    private IRepositorio<Marcador> repoMarcadores;

    /** Repositorio de usuarios (SQLite). */
    private IRepositorio<Usuario> repoUsuarios;

    // ── Servicios ─────────────────────────────────────────────────────────────

    /** Servicio de gestión de marcadores. */
    private MarcadorService marcadorService;

    /** Servicio de gestión de usuarios y autenticación. */
    private UsuarioService usuarioService;

    /** Servicio de pestañas (implementa ITabManager e ITabHistory). */
    private TabService tabService;

    /** Servicio de descargas. */
    private IDescargaManager descargaManager;

    /** Validador de URLs externas. */
    private IURLValidator urlValidator;

    // ── Bootstrap ─────────────────────────────────────────────────────────────

    /**
     * Método de entrada de la JVM. Delega en una instancia de {@code App}
     * para separar la lógica de arranque del método estático.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        new App().arrancar();
    }

    /**
     * Coordina el arranque completo de la aplicación en tres fases:
     * creación de repositorios, creación de servicios e inyección de
     * dependencias en el controlador.
     */
    private void arrancar() {
        crearRepos();
        crearServicios();
        inyectarDeps();
    }

    /**
     * Fase 1 — Instancia los repositorios concretos.
     *
     * <p>Solo esta fase conoce las clases concretas {@link JSONRepo} y
     * {@link SQLiteRepository}. El resto del sistema opera sobre
     * {@link IRepositorio}.</p>
     */
    @SuppressWarnings("unchecked")
    private void crearRepos() {
        repoMarcadores = new JSONRepo("marcadores.json");
        repoUsuarios   = new SQLiteRepository("jdbc:sqlite:navegador.db");
    }

    /**
     * Fase 2 — Instancia los servicios de negocio inyectando los repositorios.
     *
     * <p>Cada servicio recibe únicamente las dependencias que necesita a
     * través de su constructor, sin saber qué implementación concreta está
     * recibiendo.</p>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void crearServicios() {
        IAutenticador autenticador = new UValidator(repoUsuarios);

        marcadorService = new MarcadorService((IRepositorio) repoMarcadores);
        usuarioService  = new UsuarioService((IRepositorio) repoUsuarios, autenticador);
        tabService      = new TabService();
        descargaManager = new DescargaManager();
        urlValidator    = new HTTPValidator();
    }

    /**
     * Fase 3 — Construye el {@link BrowserController} con todos sus
     * colaboradores y lo pone en marcha.
     *
     * <p>A partir de este punto {@code App} cede el control completamente
     * al controlador; el método retorna cuando el usuario elige salir.</p>
     */
    private void inyectarDeps() {
        BrowserController controller = new BrowserController(
                marcadorService,
                usuarioService,
                tabService,     // ITabManager
                tabService,     // ITabHistory (mismo objeto, doble rol)
                descargaManager,
                urlValidator
        );
        controller.ejecutar();
    }
}
