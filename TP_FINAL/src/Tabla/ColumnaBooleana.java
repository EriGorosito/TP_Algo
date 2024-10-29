package Tabla;

import java.util.ArrayList;

public class ColumnaBooleana extends Columna<Boolean>{
    public ColumnaBooleana(String encabezado) {
        super(encabezado);
    }

       // Constructor de copia profunda
       public ColumnaBooleana(ColumnaBooleana otraColumna) {
        super(otraColumna.getEncabezado());
        for (Boolean valor : otraColumna.getColumna()) {
            this.columna.add(valor); // Copia cada valor
        }
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

    @Override
    public String getTipoDato() {
        return "Booleana";
    }

    @Override
    public Columna<Boolean> clone() {
        ColumnaBooleana nuevaColumna = new ColumnaBooleana(this.getEncabezado());
        nuevaColumna.columna = new ArrayList<>(this.columna); // Copia los datos
        return nuevaColumna;
    }
}
