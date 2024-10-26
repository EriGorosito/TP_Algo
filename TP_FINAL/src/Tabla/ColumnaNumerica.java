package Tabla;

public class ColumnaNumerica extends Columna<Number>{

    public ColumnaNumerica(String encabezado) {
        super(encabezado);
        
    }

    // Constructor de copia profunda
    public ColumnaNumerica(ColumnaNumerica otraColumna) {
        super(otraColumna.getEncabezado());
        for (Number valor : otraColumna.getColumna()) {
            this.columna.add(valor); // Copia cada valor
        }
    }
    
    @Override
    public void agregarDato(Number celda) {
        if (validarDato(celda)) {
            columna.add(celda);
        } else {
            throw new IllegalArgumentException("El dato no es un número válido.");
        }
    }

    @Override
    public boolean validarDato(Number celda) {
        // En este caso simplemente comprobamos que el dato no sea nulo
        return celda != null;
    }
}
