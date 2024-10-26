package Tabla;

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
}
