package Tabla;

public class ColumnaCadena extends Columna<String>{

    public ColumnaCadena(String encabezado) {
        super(encabezado);
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
