package Testing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import Tabla.OperadorLogico;
import Tabla.Tabla;

public class Prueba_matriz {
    public static void  main(String[] args){
        Object[][] datos = {
            {10, "Hola", true},
            {20, "Mundo", false},
            {30, "Java", true}
        };

        Tabla tablaDesdeMatriz = new Tabla(datos, false);
        tablaDesdeMatriz.imprimirTabla();
    
         
        //  //Acceso indexado
        // List<Object> fila = tablaDesdeMatriz.indexFila("1");  // Accede a la fila con etiqueta "1"
        // System.out.println("Fila 1: " + fila);

        // // Acceso a una columna completa
        // List<Object> columna = tablaDesdeMatriz.indexColumna("Columna1");  // Accede a la columna "Nombre"
        // System.out.println("Columna 'Columna 1': " + columna);

        // // Acceso a una celda específica
        // Object celda = tablaDesdeMatriz.indexCelda("1", "Columna1");  // Accede a la celda en fila "1" y columna "Nombre"
        // System.out.println("Celda en (1, Columna1): " + celda);

        // //eliminar fila
        // tablaDesdeMatriz.eliminarFilaPorEtiqueta("1");
        // System.out.println(tablaDesdeMatriz);

        // Tabla copia = tablaDesdeMatriz.copiaProfunda();
        // System.out.println(copia);

        // //descargar a csv
        // String descarga_rutaArchivo = "tabla_salida4.csv";
        // tablaDesdeMatriz.descargarACSV(descarga_rutaArchivo);
        


        // // Ordenamiento NO ANDA 
        // List<String> l = new ArrayList<>();
        // l.add("Columna1");
        // tablaDesdeMatriz.ordenarFilas(l, false).imprimirTabla();
         

        // // Muestreo
        // tablaDesdeMatriz.muestreo(2).imprimirTabla();
        // System.out.println(tablaDesdeMatriz);

        // //Seleccion 

        // Tabla tabla_seleccion = tablaDesdeMatriz.seleccionar(Arrays.asList("1", "2"), Arrays.asList("Columna1"));
        // //tabla_seleccion.imprimirTabla();
        // System.out.println(tabla_seleccion);

         
        // Tabla head = tablaDesdeMatriz.head(1);
        // Tabla tail = tablaDesdeMatriz.tail(2);
        // System.out.println(head);
        // System.out.println(tail);


        // //concatenar

        // Object[][] otrosdatos = {
        //     {100, "grupo", true},
        //     {200, "numeor", false},
        //     {300, "10", true}
        // };

        // Tabla tabla2 = new Tabla(otrosdatos);
        // Tabla tablaConcatenada = Tabla.concatenarTablas(tablaDesdeMatriz, tabla2);
        // tablaConcatenada.info();
        // tablaDesdeMatriz.info();

        //          List<String> columnas = List.of("Columna1", "Columna2");
        // List<Predicate<Object>> predicados = List.of(
        //     valor -> (Integer) valor  < 30,   
        //     //valor -> (Double) valor >20

        //     //valor -> (Boolean) valor.equals(true)
        //     valor -> valor.equals("Hola".trim())
        // );
        

        // Tabla tablaFiltrada = tablaDesdeMatriz.filtrar(columnas, predicados, OperadorLogico.OR);
        // tablaFiltrada.info();
    }
    
}
