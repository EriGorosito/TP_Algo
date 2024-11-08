package Tabla.Excepciones;

public class EncabezadoNoEncontradoException extends RuntimeException {
    public EncabezadoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
