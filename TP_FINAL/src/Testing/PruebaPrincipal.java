package Testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import Tabla.Tabla;
import Tabla.OperadorLogico.OperadorLogico;
import Tabla.Cronometro;

public class PruebaPrincipal {

     public static void main(String[] args) {

        Cronometro cronometro = new Cronometro();
            //Creamos una tabla desde un archivo csv 	
        String rutaArchivo = "./insurance.csv"; 	
        Tabla tablaCSV = new Tabla(rutaArchivo, true, ","); 	 	

        //Visualizar la fila 100 	
        System.out.println("Fila 100: " + tablaCSV.indexFila("100")); 	
        System.out.println("Celda en fila 100 columna age: "+ tablaCSV.indexCelda("100", "age")); 	

        //Obtener las primeras 5 filas 
            Tabla tabla_head = tablaCSV.head(5);
            System.out.println(tabla_head); 	

         //Solo visualizar las ultimas 3 filas
            System.out.println(tablaCSV.tail(3)); 
        
        // Seleccion columnas age y bmy y filas 7,2,50
        cronometro.iniciar();
        Tabla tabla_seleccion = tablaCSV.seleccionar(Arrays.asList("7", "2", "50"), Arrays.asList("age", "bmi"));
        System.out.println(tabla_seleccion);
        System.out.println(cronometro.parcial());

        // Muestreo
        System.out.println(tablaCSV.muestreo(5));
        System.out.println(cronometro.detener());

        //Crear una tabla desde una matriz sin encabezado 	
        Object[][] datos = {
            {null, "Romina", true},
            {20, null, false},
            {30, "Cristian", null} }; 	 

        //Matriz 	
        Tabla tabla_matriz = new Tabla(datos, false); 	

        //Hacemos una copia profunda 	
        Tabla copia = tabla_matriz.copiaProfunda(); 	 

        //Para probarlo agregamos una columna y modificamos una celda 	
        copia.agregarColumna(Arrays.asList(1, 0, 3)); 	
        copia.imputarNA(20); 	
        System.out.println("Tabla original:"); 	
        System.out.println(tabla_matriz); 	
        System.out.println("Copia luego de las modificaciones"); 	
        System.out.println(copia); 	 	
	 	 	

        //Tabla desde una secuencia líneal 	
        List<Object[]> filas = new ArrayList<>(); 	
        filas.add(new Object[]{"Edad", "Nombre", "Fumador"}); 	
        filas.add(new Object[]{10, "Romina", true}); 	
        filas.add(new Object[]{20, "Sofia", false}); 	
        filas.add(new Object[]{30, "Crsitian", true}); 	

        Tabla tabla_secLineal = new Tabla(filas, true); 	 	
        
        //Combinar filtrar y descargar a csv 	
        List<String> columnas = List.of("Edad", "Nombre"); 
        List<Predicate<Object>> predicados = List.of(
            valor -> (Integer) valor < 20,
            valor -> valor.equals("Sofia".trim())
            ); 	
        Tabla tabla_filtrada = tabla_secLineal.filtrar(columnas, predicados, OperadorLogico.OR); 	
        tabla_filtrada.descargarACSV("tabla_secLineal.csv", true, "jj");
    }
}
