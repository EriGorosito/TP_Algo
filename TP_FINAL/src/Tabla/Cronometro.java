package Tabla;

import java.time.Duration;
import java.time.Instant;

import Tabla.Excepciones.CronometroException;

public class Cronometro {
    private Instant tiempoInicio;
    private Instant tiempoFin;

    public Cronometro() {
        tiempoInicio = Instant.now();
        tiempoFin = null;
    }

    // Inicia el cronómetro
    public void iniciar() {
        tiempoInicio = Instant.now();
        tiempoFin = null; // Resetea el tiempo de fin en caso de reutilización
    }

    public void reiniciar() {
        tiempoInicio = Instant.now();
        tiempoFin = null;
    }

    public double parcial() {
        if (tiempoInicio == null) {
            throw new CronometroException("El cronómetro no se ha iniciado.");
        }
        tiempoFin = Instant.now();
        Duration duracion = Duration.between(tiempoInicio, tiempoFin);
        return duracion.toMillis() / 1000.0;
    }

    public double detener() {
        if (tiempoInicio == null) {
            throw new CronometroException("El cronómetro no se ha iniciado.");
        }
        tiempoFin = Instant.now();
        Duration duracion = Duration.between(tiempoInicio, tiempoFin);
        tiempoInicio = null;

        return duracion.toMillis() / 1000.0;
    }
}
