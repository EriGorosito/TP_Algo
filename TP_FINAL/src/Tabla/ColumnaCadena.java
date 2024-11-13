package Tabla;

import java.util.ArrayList;
import java.util.Objects;
import Tabla.Columna.Columna;

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
    public Columna<String> copiaEstructura() {
        return new ColumnaCadena(this.getEncabezado());
    }

    @Override
    public void modificarDato(int indice, Object nuevoValor) {
        this.columna.set(indice, nuevoValor.toString());
    }

        @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }

        if (otro == null || this.getClass() != otro.getClass()) {
            return false;
        }

        Columna otro2 = (ColumnaCadena) otro; 
        return  Objects.equals(this.getEncabezado(), otro2.getEncabezado());
    }
}
