import Tabla.ColumnaBooleana;
import Tabla.ColumnaCadena;
import Tabla.ColumnaNumerica;
import Tabla.Tabla;
import java.util.List;
import java.util.ArrayList;

public class PruebaTabla {
    
    public static void main(String[] args) {
//CONSTRUCTOR PARA COPIA PROFUNDA
        // Crear la tabla original y agregar columnas
        // Tabla tablaOriginal = new Tabla();
        // ColumnaNumerica colNumerica = new ColumnaNumerica("Columna Numerica");
        // colNumerica.agregarDato(10);
        // colNumerica.agregarDato(20);
        
        // ColumnaCadena colCadena = new ColumnaCadena("Columna Cadena");
        // colCadena.agregarDato("Hola");
        // colCadena.agregarDato("Mundo");
        
        // ColumnaBooleana colBooleana = new ColumnaBooleana("Columna Booleana");
        // colBooleana.agregarDato(true);
        // colBooleana.agregarDato(false);
        
        // tablaOriginal.agregarColumna(colNumerica);
        // tablaOriginal.agregarColumna(colCadena);
        // tablaOriginal.agregarColumna(colBooleana);

        // // Imprimir la tabla original
        // System.out.println("Tabla Original:");
        // tablaOriginal.imprimirTabla();

        // // Crear una copia profunda de la tabla
        // Tabla tablaCopia = new Tabla(tablaOriginal);

        // // Imprimir la tabla copiada
        // System.out.println("\nTabla Copiada:");
        // tablaCopia.imprimirTabla();

        // // Modificar la tabla original para verificar que la copia no cambia
        // colNumerica.agregarDato(30);
        // colCadena.agregarDato("Prueba");
        // colBooleana.agregarDato(true);

        // // Imprimir ambas tablas para verificar la independencia
        // System.out.println("\nTabla Original después de modificaciones:");
        // tablaOriginal.imprimirTabla();

        // System.out.println("\nTabla Copiada después de modificaciones en la original:");
        // tablaCopia.imprimirTabla();
//PARA CONSTRUCTOR MATRICES
        // Object[][] datos = {
        //     {10, "Hola", true},
        //     {20, "Mundo", false},
        //     {30, "Java", true}
        // };

        // Tabla tablaDesdeMatriz = new Tabla(datos);
        // tablaDesdeMatriz.imprimirTabla();
    
//PARA CONTRUCTOR SECUENCIA LINEAL
        List<Object[]> filas = new ArrayList<>();
        filas.add(new Object[]{10, "Hola", true});
        filas.add(new Object[]{20, "Mundo", false});
        filas.add(new Object[]{30, "Java", true});

        // Crear una tabla a partir de la lista de filas
        Tabla tablaDesdeLista = new Tabla(filas);

        // Imprimir la tabla
        tablaDesdeLista.imprimirTabla();
    }
}


