package Tabla.excepciones;

public class DiferenteCantidadColumnasException extends RuntimeException {
    public DiferenteCantidadColumnasException(String mensaje) {
        super(mensaje);
    }
}
