package Tabla.Excepciones;

public class FilaVaciaException extends RuntimeException {
    public FilaVaciaException(String mensaje) {
        super(mensaje);
    }
}
