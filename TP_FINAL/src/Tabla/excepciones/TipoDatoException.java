package Tabla.excepciones;

public class TipoDatoException extends RuntimeException{
    public TipoDatoException(String mensaje){
        super(mensaje);
    }
}
