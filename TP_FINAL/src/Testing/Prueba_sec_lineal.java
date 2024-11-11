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
        Tabla tablaDesdeLista = new Tabla(filas);

    //     // Imprimir la tabla
    //     // tablaDesdeLista.imprimirTabla();

    //     //Acceso indexado
    //     List<Object> fila = tablaDesdeLista.indexFila("1");  // Accede a la fila con etiqueta "1"
    //     System.out.println("Fila 1: " + fila);

    //     // Acceso a una columna completa
    //     List<Object> columna = tablaDesdeLista.indexColumna("Columna 1");  // Accede a la columna "Nombre"
    //     System.out.println("Columna 'Columna 1': " + columna);

    //     // Acceso a una celda específica
    //     Object celda = tablaDesdeLista.indexCelda("1", "Columna 1");  // Accede a la celda en fila "1" y columna "Nombre"
    //     System.out.println("Celda en (1, Columna 1): " + celda);

    //     //eliminar fila
    //     Tabla tablita = tablaDesdeLista.eliminarFilaPorEtiqueta("1");
    //     System.out.println(tablaDesdeLista);
    //     System.out.println(tablita);

    //     Tabla copia = tablaDesdeLista.copiaIndependiente();
    //     System.out.println(copia);

    //     //descargar a csv
    //     String descarga_rutaArchivo = "tabla_salida2.csv";
    //     tablaDesdeLista.descargarACSV(descarga_rutaArchivo);



    //     // // Ordenamiento NO ANDA 
    //     // List<String> l = new ArrayList<>();
    //     // l.add("Columna 1");
    //     // tablaDesdeLista.ordenarFilas(l, true).imprimirTabla();
        

    //     // // Muestreo
    //     // tablaDesdeLista.muestreo(2).imprimirTabla();
    //     // System.out.println(tablaDesdeLista);

    //     // //Sseleccion NO FUNCIONA BIEN EL IMPRIMIR TABLA
    //     // Tabla tabla_seleccion = tablaDesdeLista.seleccionar(Arrays.asList("1", "2"), Arrays.asList("Columna 1"));
    //     // tabla_seleccion.imprimirTabla();
    //     // System.out.println(tabla_seleccion);

    //     // //ESTA MAL HECHO TAIL
    //     // Tabla head = tablaDesdeLista.head(1);
    //     // Tabla tail = tablaDesdeLista.tail(2);
    //     // System.out.println(head);
    //     // System.out.println(tail);

        //concatenar
        List<Object[]> otrasfilas = new ArrayList<>();
        otrasfilas.add(new Object[]{13, "grupo", true});
        otrasfilas.add(new Object[]{24, "numero", false});
        otrasfilas.add(new Object[]{70, "10", true});

        // Crear una tabla a partir de la lista de filas
        Tabla tabla2 = new Tabla(otrasfilas);

    
        Tabla tablaConcatenada = Tabla.concatenarTablas(tablaDesdeLista, tabla2);
        tablaConcatenada.info();
        tablaDesdeLista.info();

    //      List<String> columnas = List.of("Columna 1", "Columna 2");
    //     List<Predicate<Object>> predicados = List.of(
    //         valor -> (Integer) valor  < 30,   
    //         //valor -> (Double) valor >20

    //         //valor -> (Boolean) valor.equals(true)
    //         valor -> valor.equals("Hola".trim())
    //     );
        

    //     Tabla tablaFiltrada = tablaDesdeLista.filtrar(columnas, predicados, OperadorLogico.OR);
    //     tablaFiltrada.info();


    
    }
}
