import Tabla.ColumnaBooleana;
import Tabla.ColumnaCadena;
import Tabla.ColumnaNumerica;
import Tabla.Tabla;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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

        //Acceso indexado
        List<Object> fila = tablaDesdeLista.indexFila("1");  // Accede a la fila con etiqueta "1"
        System.out.println("Fila 1: " + fila);

        // Acceso a una columna completa
        List<Object> columna = tablaDesdeLista.indexColumna("Columna 1");  // Accede a la columna "Nombre"
        System.out.println("Columna 'Columna 1': " + columna);

        // Acceso a una celda específica
        Object celda = tablaDesdeLista.indexCelda("1", "Columna 1");  // Accede a la celda en fila "1" y columna "Nombre"
        System.out.println("Celda en (1, Columna 1): " + celda);


       
        // String rutaArchivo = "tabla_salida.csv";
        // tablaDesdeLista.descargarACSV(rutaArchivo);

        // System.out.println("Archivo CSV generado en: " + rutaArchivo);

        // String filtro = "Columna 1 > 10 and Columna 3 = true";
        // Tabla tablaFiltrada = tablaDesdeLista.filtrar(filtro);
        // tablaFiltrada.imprimirTabla();

        Tabla nuevaTabla = tablaDesdeLista.seleccionar(Arrays.asList("1", "2"), Arrays.asList("Columna 3"));
        nuevaTabla.imprimirTabla();
        nuevaTabla.infoTabla();
        

// Crear tablas de prueba CONCATENAR
/* 
        List<Object[]> filas1 = List.of(
        new Object[]{10, "Hola", true},
        new Object[]{20, "Mundo", false}
        );
        List<Object[]> filas2 = List.of(
        new Object[]{30, "Java", true},
        new Object[]{40, "Tabla", false}
        );

        Tabla tabla1 = new Tabla(filas1);
        Tabla tabla2 = new Tabla(filas2);

        // Imprimir tablas originales
        System.out.println("Tabla 1:");
        tabla1.imprimirTabla();
        System.out.println("Tabla 2:");
        tabla2.imprimirTabla();

        // Concatenar tablas
        Tabla tablaConcatenada = Tabla.concatenarTablas(tabla1, tabla2);

        // Imprimir tabla concatenada
        System.out.println("Tabla Concatenada:");
        tablaConcatenada.imprimirTabla();
*/
    }
}


