package Tabla.Excepciones;

public class TipoDatoException extends RuntimeException{
    public TipoDatoException(String mensaje){
        super(mensaje);
    }
}
