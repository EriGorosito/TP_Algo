package Tabla.Excepciones;

public class CronometroException extends RuntimeException {
    public CronometroException(String mensaje) {
        super(mensaje);
    }
}
