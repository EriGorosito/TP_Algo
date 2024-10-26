package Tabla;

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


}
