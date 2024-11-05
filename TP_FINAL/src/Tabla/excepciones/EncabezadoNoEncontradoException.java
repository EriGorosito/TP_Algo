package Tabla.excepciones;

public class EncabezadoNoEncontradoException extends RuntimeException {
    public EncabezadoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
