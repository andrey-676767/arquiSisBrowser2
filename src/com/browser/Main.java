package com.browser;

import com.browser.app.App;

/**
 * Clase de entrada convencional del simulador de navegador.
 *
 * <h3>Responsabilidad</h3>
 * <p>Su único trabajo es proporcionar el método {@code main} estándar que
 * la JVM busca al ejecutar el JAR, y delegar inmediatamente en
 * {@link App}, que es quien realmente conoce el proceso de arranque
 * (creación de repositorios, servicios e inyección de dependencias).</p>
 *
 * <h3>Por qué existe esta clase además de App</h3>
 * <p>Mantener separados el punto de entrada de la JVM y la lógica de
 * bootstrap permite:</p>
 * <ul>
 *   <li>Indicar esta clase como {@code Main-Class} en el {@code MANIFEST.MF}
 *       sin acoplar el empaquetado a un paquete interno.</li>
 *   <li>Sustituir {@link App} en pruebas de integración sin tocar el
 *       punto de entrada.</li>
 * </ul>
 *
 * @author Refactorización Fase 6
 * @version 1.0
 * @see App
 */
public class Main {

    /**
     * Punto de entrada de la JVM.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        App.main(args);
    }
}
