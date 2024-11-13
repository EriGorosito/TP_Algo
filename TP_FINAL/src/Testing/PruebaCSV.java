package Testing;

import Tabla.Tabla;
import Tabla.OperadorLogico.OperadorLogico;

import java.util.List;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.Arrays;

public class PruebaCSV {
    public static void main(String[] args) {

        String rutaArchivo = "./insurance.csv";
        Tabla tabla = new Tabla(rutaArchivo, true, ",");
        Tabla tabla2 = new Tabla(rutaArchivo, true, ",");

        tabla.info();

        tabla.eliminarFilaPorEtiqueta("1");
        System.out.println(tabla);

        Tabla copia = tabla.copiaProfunda();
        System.out.println(copia);

        // descargar a csv
        String descarga_rutaArchivo = "tabla_salida.csv";
        tabla.descargarACSV(descarga_rutaArchivo, true, ",");

        // Acceso a la fila 10
        List<Object> fila = tabla.indexFila("10"); // Accede a la fila con etiqueta "1"
        System.out.println("Fila 10: " + fila);

        // Acceso a una columna completa
        List<Object> columna = tabla.indexColumna("age"); // Accede a la columna "Nombre"
        System.out.println("Columna 'age': " + columna);

        // Acceso a una celda específica
        Object celda = tabla.indexCelda("10", "sex"); // Accede a la celda en fila "1" y columna "Nombre"
        System.out.println("Celda en (10, Nombre): " + celda);
        // Agregar columna a partir de otra
        List columnita = tabla.indexColumna("age");
        // tabla.agregarColumna("hola", columnita);
        System.out.println(tabla);

        // Ordenamiento
        List<String> l = new ArrayList<>();
        l.add("age");
        l.add("bmi");
        System.out.println(tabla.ordenarFilas(l, false));

        // Muestreo
        System.out.println(tabla.muestreo(5));

        // Seleccion
        Tabla tabla_seleccion = tabla.seleccionar(Arrays.asList("7", "2", "50"), Arrays.asList("age", "bmi"));
        System.out.println(tabla_seleccion);

        // Tail y Head
        Tabla head = tabla.head(4);
        Tabla tail = tabla.tail(4);
        System.out.println(tail);
        System.out.println(head);

        Tabla tablaConcatenada = tabla.concatenarTablas(tabla2);
        tablaConcatenada.info();
        tabla.info();

        List<String> columnas = Arrays.asList("age", "region");
        List<Predicate<Object>> predicados = Arrays.asList(
                valor -> (Double) valor > 30,
                // valor -> (Double) valor >20

                // valor -> (Boolean) valor.equals(true)
                valor -> valor.equals("northwest".trim()));

        Tabla tablaFiltrada = tabla.filtrar(columnas, predicados, OperadorLogico.OR);
        tablaFiltrada.info();

    }
}
