import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Tabla.Tabla;

public class App {
    public static void main(String[] args) {
        String rutaArchivo = "C:\\Users\\usuario\\Downloads\\Prueba(1).csv"; // Cambia esto por la ruta de tu archivo CSV
        
        Tabla tabla = new Tabla(rutaArchivo, true, ",");
        //tabla.cargarCSV(rutaArchivo);
        tabla.imprimirTabla();
    }

    
}