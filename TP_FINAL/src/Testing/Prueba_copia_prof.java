package Testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Tabla.ColumnaCadena;
import Tabla.Tabla;
import Tabla.Columna.ColumnaBooleana;
import Tabla.Columna.ColumnaNumerica;

public class Prueba_copia_prof {

    public static void main(String[] args) {
        
        //Crear la tabla original y agregar columnas
        Tabla tablaOriginal = new Tabla();
        ColumnaNumerica colNumerica = new ColumnaNumerica("Columna Numerica");
        colNumerica.agregarDato(50);
        colNumerica.agregarDato(100);
        
        ColumnaCadena colCadena = new ColumnaCadena("Columna Cadena");
        colCadena.agregarDato("Hola");
        colCadena.agregarDato("grupo");
        
        ColumnaBooleana colBooleana = new ColumnaBooleana("Columna Booleana");
        colBooleana.agregarDato(true);
        colBooleana.agregarDato(false);
        
        tablaOriginal.agregarColumna(colNumerica);
        tablaOriginal.agregarColumna(colCadena);
        tablaOriginal.agregarColumna(colBooleana);

        // Crear una copia profunda de la tabla
        Tabla tablaCopia = new Tabla(tablaOriginal);

        // Imprimir la tabla copiada
        System.out.println("\nTabla Copiada:");
        tablaCopia.imprimirTabla();

        // Modificar la tabla original para verificar que la copia no cambia
        colNumerica.agregarDato(30);
        colCadena.agregarDato("Prueba");
        colBooleana.agregarDato(true);

        // Imprimir ambas tablas para verificar la independencia
        System.out.println("\nTabla Original después de modificaciones:");
        tablaOriginal.imprimirTabla();

        System.out.println("\nTabla Copiada después de modificaciones en la original:");
        tablaCopia.imprimirTabla();
/////
 
         //Acceso indexado
         List<Object> fila = tablaCopia.indexFila("1");  // Accede a la fila con etiqueta "1"
         System.out.println("Fila 1: " + fila);
 
         // Acceso a una columna completa
         List<Object> columna = tablaCopia.indexColumna("Columna Numerica");  // Accede a la columna "Nombre"
         System.out.println("Columna 'Columna 1': " + columna);
 
         // Acceso a una celda específica
         Object celda = tablaCopia.indexCelda("1", "Columna Numerica");  // Accede a la celda en fila "1" y columna "Nombre"
         System.out.println("Celda en (1, Columna Numerica): " + celda);
 
         //eliminar filatablaCopia.eliminarFilaPorEtiqueta("0");
         System.out.println(tablaCopia);
 
         Tabla copia = tablaCopia.copiaProfunda();
         System.out.println(copia);
 
         //descargar a csv
         String descarga_rutaArchivo = "tabla_salida3.csv";
         tablaCopia.descargarACSV(descarga_rutaArchivo);
 
         // Ordenamiento NO ANDA 
         List<String> l = new ArrayList<>();
         l.add("Columna Numerica");
         tablaCopia.ordenarFilas(l, true).imprimirTabla();
         
 
         // Muestreo
         tablaCopia.muestreo(1).imprimirTabla();
         System.out.println(tablaCopia);
 
         //Seleccion NO FUNCIONA BIEN EL IMPRIMIR TABLA
         Tabla tabla_seleccion = tablaCopia.seleccionar(Arrays.asList("1"), Arrays.asList("Columna Numerica"));
         tabla_seleccion.imprimirTabla();
         System.out.println(tabla_seleccion);
 
         //ESTA MAL HECHO TAIL
         Tabla head = tablaCopia.head(1);
         Tabla tail = tablaCopia.tail(1);
         System.out.println(head);
         System.out.println(tail);

        //concatenar
        Tabla tablaConcatenada = Tabla.concatenarTablas(tablaOriginal, tablaCopia);
        tablaConcatenada.info();
        tablaOriginal.info();
    }
}
