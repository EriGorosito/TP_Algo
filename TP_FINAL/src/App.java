import Tabla.*;

import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String rutaArchivo = "./insurance.csv";
        Tabla tabla = new Tabla(rutaArchivo, true);
        //tabla.eliminarFilaPorEtiqueta("1");
        System.out.println(tabla);
        // tabla.cargarCSV(rutaArchivo);
        // tabla.infoTabla();
        // // tabla.imprimirTabla();
        //List columnita = tabla.indexColumna("age");
        //tabla.agregarColumna("hola", columnita);
        //System.out.println(tabla);
        // List<Object> fila = tabla.indexFila("1"); // Accede a la fila con etiqueta
        // "1"
        // System.out.println("Fila 1: " + fila);

        // // Acceso a una columna completa
        //List<Object> columna = tabla.indexColumna("age");
        //System.out.println("Columna 'age': " + columna);

        //tabla.modificarCelda("age", "1", 1);
        //System.out.println(tabla);
        //HashMap<String, Object> valores = new HashMap<>(); //Imputar NA
        //valores.put("sex", "macho");
        //valores.put("children", 2);
        //tabla.imputarNA(valores);
        //System.out.println(tabla);

        // Acceso a una celda específica
        // Object celda = tabla.indexCelda("1", "sex");
        // System.out.println("Celda en (1, Nombre): " + celda);

        // // Ordenamiento
        // List<String> l = new ArrayList<>();
        // l.add("age");
        // // tabla.ordenarFilas(l, true).imprimirTabla();
        // ;

        // // Muestreo
        // tabla.muestreo(5).imprimirTabla();
        // System.out.println(tabla);

        // String filtro = "children > 1 and bmi < 20";
        // Tabla tablaFiltrada = tabla.filtrar(filtro);
        // tablaFiltrada.imprimirTabla();

    }

}