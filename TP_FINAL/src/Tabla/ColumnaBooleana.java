package Tabla;

public class ColumnaBooleana extends Columna<Boolean>{
    public ColumnaBooleana(String encabezado) {
        super(encabezado);
    }

    @Override
    public void agregarDato(Boolean celda) {
        if (validarDato(celda)) {
            columna.add(celda);
        } else {
            throw new IllegalArgumentException("El dato no es un booleano válido.");
        }
    }

    @Override
    public boolean validarDato(Boolean celda) {
        return celda != null;
    }
}
