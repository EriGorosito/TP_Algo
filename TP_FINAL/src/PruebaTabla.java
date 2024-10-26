import Tabla.ColumnaBooleana;
import Tabla.ColumnaCadena;
import Tabla.ColumnaNumerica;
import Tabla.Tabla;

public class PruebaTabla {
    
    public static void main(String[] args) {
        // Crear la tabla original y agregar columnas
        Tabla tablaOriginal = new Tabla();
        ColumnaNumerica colNumerica = new ColumnaNumerica("Columna Numerica");
        colNumerica.agregarDato(10);
        colNumerica.agregarDato(20);
        
        ColumnaCadena colCadena = new ColumnaCadena("Columna Cadena");
        colCadena.agregarDato("Hola");
        colCadena.agregarDato("Mundo");
        
        ColumnaBooleana colBooleana = new ColumnaBooleana("Columna Booleana");
        colBooleana.agregarDato(true);
        colBooleana.agregarDato(false);
        
        tablaOriginal.agregarColumna(colNumerica);
        tablaOriginal.agregarColumna(colCadena);
        tablaOriginal.agregarColumna(colBooleana);

        // Imprimir la tabla original
        System.out.println("Tabla Original:");
        tablaOriginal.imprimirTabla();

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
    }
}


