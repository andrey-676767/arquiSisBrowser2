package com.browser.model;

import java.util.Random;

/**
 * Entidad que representa una descarga activa en el simulador de navegador.
 *
 * <p>Cada descarga tiene un nombre asignado por el usuario y un peso
 * generado aleatoriamente en megabytes. Las descargas se administran
 * mediante una {@link infraestructura.estructuras.ListaDoble} en el
 * controlador: las nuevas descargas se insertan al frente (como si fueran
 * las más recientes) y las finalizadas se eliminan desde el final.</p>
 *
 * <p>La clase es inmutable en sus atributos de instancia (nombre y peso
 * no pueden cambiarse tras la creación), lo que la hace segura para
 * almacenamiento en colecciones.</p>
 *
 * @author Refactorización Fase 1
 * @version 1.0
 */
public class Descarga {

    /** Nombre descriptivo de la descarga proporcionado por el usuario. */
    private final String nombre;

    /** Tamaño simulado de la descarga en megabytes (generado aleatoriamente). */
    private final int peso;

    /** Generador de números aleatorios compartido entre instancias. */
    private static final Random RANDOM = new Random();

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Construye una nueva descarga con el nombre indicado y un peso aleatorio
     * entre 0 y 999 MB.
     *
     * @param nombre Nombre descriptivo de la descarga.
     */
    public Descarga(String nombre) {
        this.nombre = nombre;
        this.peso = RANDOM.nextInt(1000);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Retorna el nombre de la descarga.
     *
     * @return El nombre asignado al crear la descarga.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna el peso simulado de la descarga en megabytes.
     *
     * @return El peso en MB.
     */
    public int getPeso() {
        return peso;
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    /**
     * Retorna una representación en formato {@code nombre - pesoMB}.
     *
     * @return Cadena descriptiva de la descarga.
     */
    @Override
    public String toString() {
        return this.nombre + " - " + this.peso + "mb";
    }
}