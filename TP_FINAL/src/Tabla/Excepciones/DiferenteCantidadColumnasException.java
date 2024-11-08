package Tabla.Excepciones;

public class DiferenteCantidadColumnasException extends RuntimeException {
    public DiferenteCantidadColumnasException(String mensaje) {
        super(mensaje);
    }
}
