package Tabla;

import java.util.ArrayList;

public class ColumnaCadena extends Columna<String>{

    public ColumnaCadena(String encabezado) {
        super(encabezado);
    }

      // Constructor de copia profunda
      public ColumnaCadena(ColumnaCadena otraColumna) {
        super(otraColumna.getEncabezado());
        for (String valor : otraColumna.getColumna()) {
            this.columna.add(valor); // Copia cada valor
        }
    }
    
    @Override
    public void agregarDato(String celda) {
    
        if (validarDato(celda)) {
            columna.add(celda);
        } else {
            throw new IllegalArgumentException("El dato no es una cadena válida.");
        }
    }

    @Override
    public boolean validarDato(String celda) {
        return celda != null && !celda.trim().isEmpty(); 
    }

    @Override
    public String getTipoDato() {
        return "String";
    }

    @Override
    public Columna<String> clone() {
        ColumnaCadena nuevaColumna = new ColumnaCadena(this.getEncabezado());
        nuevaColumna.columna = new ArrayList<>(this.columna); // Copia los datos
        return nuevaColumna;
    }

    @Override
    public Columna<String> copia() {
        return new ColumnaCadena(this.getEncabezado());
    }


}
