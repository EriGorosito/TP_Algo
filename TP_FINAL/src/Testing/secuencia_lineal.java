package Testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import Tabla.Tabla;
import Tabla.OperadorLogico.OperadorLogico;
public class secuencia_lineal {

    public static void main(String[] args) {
        // //PARA CONTRUCTOR SECUENCIA LINEAL
            List<Object[]> filas = new ArrayList<>();
            filas.add(new Object[]{10, "Hola", true});
            filas.add(new Object[]{20, "Mundo", false});
            filas.add(new Object[]{30, "Java", true});
    
            // Crear una tabla a partir de la lista de filas
            Tabla tablaDesdeLista = new Tabla(filas, false);
            tablaDesdeLista.info();

    
            // //Acceso indexado
            // List<Object> fila = tablaDesdeLista.indexFila("1");  // Accede a la fila con etiqueta "1"
            // System.out.println("Fila 1: " + fila);
    
            // // Acceso a una columna completa
            // List<Object> columna = tablaDesdeLista.indexColumna("Columna1");  // Accede a la columna "Nombre"
            // System.out.println("Columna 'Columna 1': " + columna);
    
            // // Acceso a una celda específica
            // Object celda = tablaDesdeLista.indexCelda("1", "Columna1");  // Accede a la celda en fila "1" y columna "Nombre"
            // System.out.println("Celda en (1, Columna 1): " + celda);
    
            // //eliminar fila
            // tablaDesdeLista.eliminarFilaPorEtiqueta("1");
            // System.out.println(tablaDesdeLista);
           
    
            // Tabla copia = tablaDesdeLista.copiaProfunda();
            // System.out.println(copia);
    
            // //descargar a csv
            // String descarga_rutaArchivo = "tabla_salida2.csv";
            // tablaDesdeLista.descargarACSV(descarga_rutaArchivo, true, ",");
    
    
    
            // // Ordenamiento
            // List<String> l = new ArrayList<>();
            // l.add("Columna1");
            // tablaDesdeLista.ordenarFilas(l, true);
            
    
            // // Muestreo
            // System.out.println(tablaDesdeLista.muestreo(2));
    
            // //Seleccion NO FUNCIONA BIEN EL IMPRIMIR TABLA
            // Tabla tabla_seleccion = tablaDesdeLista.seleccionar(Arrays.asList("1", "2"), Arrays.asList("Columna1"));
            // System.out.println(tabla_seleccion);
    
            // // Tail y Head
            // Tabla head = tablaDesdeLista.head(1);
            // Tabla tail = tablaDesdeLista.tail(2);
            // System.out.println(head);
            // System.out.println(tail);
    
            // //concatenar
            // List<Object[]> otrasfilas = new ArrayList<>();
            // otrasfilas.add(new Object[]{13, "grupo", true});
            // otrasfilas.add(new Object[]{24, "numero", false});
            // otrasfilas.add(new Object[]{70, "10", true});
    
            // // Crear una tabla a partir de la lista de filas
            // Tabla tabla2 = new Tabla(otrasfilas, false);

            // //Concatenar
            // Tabla tablaConcatenada = tablaDesdeLista.concatenarTablas(tabla2);
            // tablaConcatenada.info();
            // tablaDesdeLista.info();
    
            // List<String> columnas = List.of("Columna1", "Columna2");
            // List<Predicate<Object>> predicados = List.of(
            //     valor -> (Integer) valor  < 30,   
            //     //valor -> (Double) valor >20
    
            //     //valor -> (Boolean) valor.equals(true)
            //     valor -> valor.equals("Hola".trim())
            // );
            
    
            // Tabla tablaFiltrada = tablaDesdeLista.filtrar(columnas, predicados, OperadorLogico.OR);
            // tablaFiltrada.info();
            // System.out.println(tablaFiltrada);

    }
}
