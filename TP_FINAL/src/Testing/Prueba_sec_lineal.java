package Testing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import Tabla.OperadorLogico;
import Tabla.Tabla;

public class Prueba_sec_lineal {

    public static void main(String[] args) {
    // //PARA CONTRUCTOR SECUENCIA LINEAL
        List<Object[]> filas = new ArrayList<>();
        filas.add(new Object[]{10, "Hola", true});
        filas.add(new Object[]{20, "Mundo", false});
        filas.add(new Object[]{30, "Java", true});

        // Crear una tabla a partir de la lista de filas
        Tabla tablaDesdeLista = new Tabla(filas, false);

        //Acceso indexado
        List<Object> fila = tablaDesdeLista.indexFila("1");  // Accede a la fila con etiqueta "1"
        System.out.println("Fila 1: " + fila);

        // Acceso a una columna completa
        List<Object> columna = tablaDesdeLista.indexColumna("Columna1");  // Accede a la columna "Nombre"
        System.out.println("Columna 'Columna 1': " + columna);

        // Acceso a una celda específica
        Object celda = tablaDesdeLista.indexCelda("1", "Columna1");  // Accede a la celda en fila "1" y columna "Nombre"
        System.out.println("Celda en (1, Columna 1): " + celda);

        //eliminar fila
        tablaDesdeLista.eliminarFilaPorEtiqueta("1");
        System.out.println(tablaDesdeLista);

        //Eliminar Columna
        tablaDesdeLista.eliminarColumna("Columna2");
        System.out.println(tablaDesdeLista);

        //Copia profunda
        Tabla copia = tablaDesdeLista.copiaProfunda();
        System.out.println(copia);

        //descargar a csv
        String descarga_rutaArchivo = "tabla_salida2.csv";
        tablaDesdeLista.descargarACSV(descarga_rutaArchivo, false, ",");



        // Ordenamiento 
        List<String> l = new ArrayList<>();
        l.add("Columna1");
        tablaDesdeLista.ordenarFilas(l, true);
        

        // Muestreo
        tablaDesdeLista.muestreo(2);
        System.out.println(tablaDesdeLista);

        //Seleccion 
        Tabla tabla_seleccion = tablaDesdeLista.seleccionar(Arrays.asList("1", "2"), Arrays.asList("Columna1"));
        System.out.println(tabla_seleccion);

        //TAIL y head
        Tabla head = tablaDesdeLista.head(1);
        Tabla tail = tablaDesdeLista.tail(2);
        System.out.println(head);
        System.out.println(tail);

        //concatenar
        List<Object[]> otrasfilas = new ArrayList<>();
        otrasfilas.add(new Object[]{13, "grupo", true});
        otrasfilas.add(new Object[]{24, "numero", false});
        otrasfilas.add(new Object[]{70, "10", true});

        // Crear una tabla a partir de la lista de filas
        Tabla tabla2 = new Tabla(otrasfilas, false);

    
        Tabla tablaConcatenada = Tabla.concatenarTablas(tablaDesdeLista, tabla2);
        tablaConcatenada.info();
        tablaDesdeLista.info();

         List<String> columnas = Arrays.asList("Columna1", "Columna2");
        List<Predicate<Object>> predicados = Arrays.asList(
            valor -> (Integer) valor  < 30,   
            //valor -> (Double) valor >20

            //valor -> (Boolean) valor.equals(true)
            valor -> valor.equals("Hola".trim())
        );
        

        Tabla tablaFiltrada = tablaDesdeLista.filtrar(columnas, predicados, OperadorLogico.AND);
        tablaFiltrada.info();


    
    }
}
