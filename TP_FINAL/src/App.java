import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import Tabla.Tabla;

public class App {
    public static void main(String[] args) {
        String rutaArchivo = "C:\\Users\\usuario\\Downloads\\Prueba(1).csv"; // Cambia esto por la ruta de tu archivo CSV
        
        Tabla tabla = new Tabla(rutaArchivo, true, ",");
        //tabla.cargarCSV(rutaArchivo);
        tabla.infoTabla();
        tabla.imprimirTabla();


        List<Object> fila = tabla.indexFila("1");  // Accede a la fila con etiqueta "1"
        System.out.println("Fila 1: " + fila);

        // Acceso a una columna completa
        List<Object> columna = tabla.indexColumna("Nombre");  // Accede a la columna "Nombre"
        System.out.println("Columna 'Nombre': " + columna);

        // Acceso a una celda específica
        Object celda = tabla.indexCelda("1", "Materia");  // Accede a la celda en fila "1" y columna "Nombre"
        System.out.println("Celda en (1, Nombre): " + celda);
    }

    
}