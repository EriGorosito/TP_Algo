import Tabla.Tabla;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PruebaCSV {
    public static void main(String[] args) {

        String rutaArchivo = "./insurance.csv";
        Tabla tabla = new Tabla(rutaArchivo, true, ",");
        Tabla tabla2 = new Tabla(rutaArchivo, true, ",");
        
        // tabla.info();

        // Tabla tablita = tabla.eliminarFilaPorEtiqueta("1");
        // System.out.println(tabla);
        // System.out.println(tablita);

        // Tabla copia = tabla.copiaIndependiente();
        // System.out.println(copia);

        // //descargar a csv
        // String descarga_rutaArchivo = "tabla_salida.csv";
        // tabla.descargarACSV(descarga_rutaArchivo);

        
         
        // tabla.imprimirTabla();

        // List<Object> fila = tabla.indexFila("1"); // Accede a la fila con etiqueta "1"
        // System.out.println("Fila 1: " + fila);

        // // // Acceso a una columna completa
        // List<Object> columna = tabla.indexColumna("age"); // Accede a la columna "Nombre"
        // System.out.println("Columna 'age': " + columna);

        // // // Acceso a una celda específica
        // Object celda = tabla.indexCelda("1", "sex"); // Accede a la celda en fila "1" y columna "Nombre"
        // System.out.println("Celda en (1, Nombre): " + celda);

        // Ordenamiento

        List<String> l = new ArrayList<>();
        l.add("age");
        l.add("bmi");
        tabla.ordenarFilas(l, false).imprimirTabla();
        

        // // Muestreo
        // tabla.muestreo(5).imprimirTabla();
        // System.out.println(tabla);

        // // String filtro = "children > 1 and bmi < 20";
        // // Tabla tablaFiltrada = tabla.filtrar(filtro);
        // // tablaFiltrada.imprimirTabla();

        // Tabla tabla_seleccion = tabla.seleccionar(Arrays.asList("1", "2", "50"), Arrays.asList("age", "bmi"));
        // tabla_seleccion.imprimirTabla();

        // //ESTA MAL HECHO TAIL
        // Tabla head = tabla.head(4);
        // Tabla tail = tabla.tail(4);
        // System.out.println(tail);
        // System.out.println(head);

        // Tabla tablaConcatenada = Tabla.concatenarTablas(tabla, tabla2);
        // tablaConcatenada.info();
        // tabla.info();

    }
}
