import Tabla.Tabla;
import java.util.List;
public class App {
    public static void main(String[] args) {
        //String rutaArchivo = "C:/Users/Lucio/Documents/Algoritmos/TP_Algo./insurance.csv"; // Cambia esto por la ruta de
                                                                                           // tu archivo CSV
        String rutaArchivo = "./insurance.csv";
        Tabla tabla = new Tabla(rutaArchivo, true, ",");
        Tabla tablita = tabla.eliminarFilaPorEtiqueta("1");
        System.out.println(tabla);
        System.out.println(tablita);
        // tabla.cargarCSV(rutaArchivo);
        //  tabla.infoTabla();
        // // tabla.imprimirTabla();

        //List<Object> fila = tabla.indexFila("1"); // Accede a la fila con etiqueta "1"
        //System.out.println("Fila 1: " + fila);

        // // Acceso a una columna completa
        // List<Object> columna = tabla.indexColumna("age"); // Accede a la columna "Nombre"
        // // System.out.println("Columna 'age': " + columna);

        // // Acceso a una celda específica
        // Object celda = tabla.indexCelda("1", "sex"); // Accede a la celda en fila "1" y columna "Nombre"
        // System.out.println("Celda en (1, Nombre): " + celda);

        // // Ordenamiento
        // List<String> l = new ArrayList<>();
        // l.add("age");
        // // tabla.ordenarFilas(l, true).imprimirTabla();
        // ;

        // // Muestreo
        // tabla.muestreo(5).imprimirTabla();
        //System.out.println(tabla);

        // String filtro = "children > 1 and bmi < 20";
        // Tabla tablaFiltrada = tabla.filtrar(filtro);
        // tablaFiltrada.imprimirTabla();

    }

}