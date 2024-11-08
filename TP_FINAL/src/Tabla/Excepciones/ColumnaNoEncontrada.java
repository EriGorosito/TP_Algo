package Tabla.Excepciones;

public class ColumnaNoEncontrada extends RuntimeException {
    public ColumnaNoEncontrada(String mensaje) {
        super(mensaje);
    }
}
