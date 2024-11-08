package Tabla.Excepciones;

public class TamanioFilaException extends RuntimeException {
    public TamanioFilaException(String mensaje) {
        super(mensaje);
    }
}
