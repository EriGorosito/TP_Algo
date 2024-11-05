package Tabla.excepciones;

public class FilaVaciaException extends RuntimeException {
    public FilaVaciaException(String mensaje) {
        super(mensaje);
    }
}
