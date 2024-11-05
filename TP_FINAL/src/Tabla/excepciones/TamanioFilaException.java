package Tabla.excepciones;

public class TamanioFilaException extends RuntimeException {
    public TamanioFilaException(String mensaje) {
        super(mensaje);
    }
}
