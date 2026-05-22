package com.browser.model;

import java.util.Random;

/**
 * Entidad que representa una descarga activa en el simulador de navegador.
 *
 * <p>Cada descarga tiene un nombre asignado por el usuario y un peso
 * generado aleatoriamente en megabytes. Las descargas son administradas por
 * {@link com.browser.services.DescargaManager}, que las organiza en una
 * {@link com.browser.structures.ListaDoble}.</p>
 *
 * <p>La clase es inmutable en sus atributos de instancia (nombre y peso
 * no pueden cambiarse tras la construcción), lo que la hace segura para
 * almacenamiento en colecciones.</p>
 *
 * @author Refactorización Fase 5
 * @version 2.0
 */
public class Descarga {

    /** Nombre descriptivo de la descarga proporcionado por el usuario. */
    private final String nombre;

    /** Tamaño simulado de la descarga en megabytes (generado aleatoriamente). */
    private final int peso;

    /** Generador de números aleatorios compartido entre instancias. */
    private static final Random RANDOM = new Random();

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Construye una nueva descarga con el nombre indicado y un peso aleatorio
     * entre 0 y 999 MB.
     *
     * @param nombre Nombre descriptivo de la descarga; generalmente derivado
     *               de la URL del recurso descargado.
     */
    public Descarga(String nombre) {
        this.nombre = nombre;
        this.peso = RANDOM.nextInt(1000);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

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
     * @return El peso en MB (valor entre 0 y 999 inclusive).
     */
    public int getPeso() {
        return peso;
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    /**
     * Retorna una representación legible en formato {@code nombre - pesoMB}.
     *
     * @return Cadena descriptiva de la descarga.
     */
    @Override
    public String toString() {
        return this.nombre + " - " + this.peso + "mb";
    }
}
