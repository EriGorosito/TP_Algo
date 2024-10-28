package Tabla;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabla {
    private List<Columna<?>> tabla;
    private String delimitador;
    private boolean tieneEncabezado;
    private List<String> etiquetasFilas;


    public Tabla(){
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
    }
    
    public Tabla(String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        this.delimitador = delimitador;
        this.tieneEncabezado = tieneEncabezado;
        this.etiquetasFilas = new ArrayList<>();this.etiquetasFilas = new ArrayList<>();
        this.tabla = new ArrayList<>();
        cargarCSV(rutaArchivo); 
    }

    //CONSTRUCTOR para generar una estructura tavular a través de copia profunda de otra estructura del mismo tipo.
    public Tabla(Tabla otraTabla) {
        this.tabla = new ArrayList<>();
        this.delimitador = otraTabla.delimitador;
        this.tieneEncabezado = otraTabla.tieneEncabezado;
        this.etiquetasFilas = new ArrayList<>();
    
        for (Columna<?> columna : otraTabla.tabla) {
            // Crea una nueva columna según el tipo de columna original
            if (columna instanceof ColumnaNumerica) {
                this.tabla.add(new ColumnaNumerica((ColumnaNumerica) columna));
            } else if (columna instanceof ColumnaBooleana) {
                this.tabla.add(new ColumnaBooleana((ColumnaBooleana) columna));
            } else if (columna instanceof ColumnaCadena) {
                this.tabla.add(new ColumnaCadena((ColumnaCadena) columna));
            }
        }
    }

    // CONSTRUCTOR que toma una matriz bidimensional para crear la tabla.(estructura de dos dimensiones nativa de Java)
    public <T> Tabla(T[][] datos) {
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        
        // Inicializar columnas según el número de columnas en la matriz
        int numColumnas = datos[0].length;
        for (int col = 0; col < numColumnas; col++) {
            // Detectar el tipo de dato en la primera fila
            T primerValor = datos[0][col];
            
            if (primerValor instanceof Number) {
                tabla.add(new ColumnaNumerica("Columna " + col));
            } else if (primerValor instanceof Boolean) {
                tabla.add(new ColumnaBooleana("Columna " + col));
            } else {
                tabla.add(new ColumnaCadena("Columna " + col));
            }
        }

        // Agregar cada fila de la matriz a la tabla
        for (T[] fila : datos) {
            agregarFila(fila);
        }
    }
    //CONSTRUCTOR para una secuencia lineal nativa de Java.
    
    public Tabla(List<Object[]> filas) {
        this(); // Llama al constructor vacío para inicializar la tabla
    
        // Suponemos que todas las filas tienen el mismo número de columnas
        if (filas.isEmpty()) {
            throw new IllegalArgumentException("La lista de filas no puede estar vacía.");
        }
    
        // Agregar las columnas a la tabla, basándose en la primera fila
        for (int col = 0; col < filas.get(0).length; col++) {
            // Determinar el tipo de la columna a partir de los valores de la primera fila
            Object valor = filas.get(0)[col];
            if (valor instanceof Number) {
                tabla.add(new ColumnaNumerica("Columna " + (col + 1)));
            } else if (valor instanceof Boolean) {
                tabla.add(new ColumnaBooleana("Columna " + (col + 1)));
            } else {
                tabla.add(new ColumnaCadena("Columna " + (col + 1)));
            }
        }
    
        // Agregar cada fila a la tabla
        for (Object[] fila : filas) {
            agregarFila(fila);
        }
    }

    
    public void cargarCSV(String rutaArchivo){
        List<String> encabezados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraFila = true;
            boolean columnasdefinidas = false;

            while ((linea = reader.readLine()) != null) {
                String[] valores= linea.split(delimitador);

                if (primeraFila && tieneEncabezado) {
                    // Si hay encabezado, lo leemos
                    encabezados.addAll(Arrays.asList(valores));
                    primeraFila = false;
                    continue;
                } 
                if (!columnasdefinidas){
                    for (int i = 0; i < valores.length; i++) {
                        String valor = valores[i].trim();
                       
                        if (esNumerico(valor)) {
                            tabla.add(new ColumnaNumerica(encabezados.get(i)));
                        } else if (esBooleano(valor)) {
                            tabla.add(new ColumnaBooleana(encabezados.get(i)));
                        } else {
                            tabla.add(new ColumnaCadena(encabezados.get(i)));
                        }
                    } 
                    columnasdefinidas = true;   
                } 
                Object[] fila = new Object[valores.length];

                for (int i = 0; i < valores.length; i++) {
                    String valor = valores[i].trim();
                    // Validamos y convertimos el valor según el tipo de la columna
                    Columna<?> columna = tabla.get(i);
                    //System.out.println(columna);
                    if (valor.equals("NA") || valor.isEmpty()) {
                        fila[i] = null; // Valor faltante 
                    } else if (columna instanceof ColumnaNumerica) {
                        fila[i] = Double.parseDouble(valor); // Convertir a número
                    } else if (columna instanceof ColumnaBooleana) {
                        fila[i] = Boolean.parseBoolean(valor); // Convertir a booleano
                    } else if (columna instanceof ColumnaCadena) {
                        fila[i] = valor; // Es un string
                    }
                        
                    
                }
                
                // Agregamos la fila a la tabla con valores validados
                this.agregarFila(fila);
                primeraFila = false;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean esNumerico(String valor) {
        try {
            Double.parseDouble(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean esBooleano(String valor) {
        return valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false");
    }

    private void inicializarEtiquetas() {
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < getNumeroFilas(); i++) {
                etiquetasFilas.add(String.valueOf(i));
            }
        }
    }

    public Columna<?> getColumna(int posicion) {
        return tabla.get(posicion);
    }

    //para permitir la adición de columnas individuales.
    public void agregarColumna(Columna<?> columna) {
        tabla.add(columna);
    }

    public void agregarFila(Object... valores) {
        if (valores.length != tabla.size()) {
            throw new IllegalArgumentException("Número de valores no coincide con el número de columnas.");
        }

        for (int i = 0; i < valores.length; i++) {
            Columna columna = tabla.get(i);
    
            if (valores[i] == null) {
                columna.agregarNA(); // Si el valor es nulo, agregamos "NA"
            } else {
                columna.agregarDato(valores[i]);
            }
        }
    }

    public int getNumeroFilas() {
        return tabla.isEmpty() ? 0 : tabla.get(0).getColumna().size();
    }

    public int getCantColumna(){
        return tabla.size();
    }

    public List<String> getEncabezados(){
        List<String> encabezados = new ArrayList<>();
        for (Columna columna: tabla){
            encabezados.add(columna.getEncabezado().trim());
        }
        return encabezados;
    }

    public Map<String, String> getTipoDatos(){
        Map<String, String> tipodeDatos = new HashMap<>();
        for (Columna columna: tabla){
            String encabezado = columna.getEncabezado();
            String tipoDato = columna.getTipoDato();
            tipodeDatos.put(encabezado, tipoDato);
        }
        return tipodeDatos;
    }

    public void infoTabla(){
        System.out.println("Información de la tabla:");
        System.out.println("Cantidad de filas " + this.getNumeroFilas());
        System.out.println("Cantidad de columnas: " + this.getCantColumna());
        Map<String, String> tipoDeDatos = this.getTipoDatos();
        for (Map.Entry<String, String> entry : tipoDeDatos.entrySet()) {
            System.out.println("Columna: " + entry.getKey() + " - Tipo de Dato: " + entry.getValue());
        }

    }

    public List<Object> indexFila(String etiquetaFila) {
        inicializarEtiquetas();
        int indiceFila = etiquetasFilas.indexOf(etiquetaFila);
        if (indiceFila == -1) {
            throw new IllegalArgumentException("Etiqueta de fila no encontrada: " + etiquetaFila);
        }
    
        List<Object> fila = new ArrayList<>();
        for (Columna<?> columna : tabla) {
            fila.add(columna.getCelda(indiceFila));
        }
        return fila;
    }
  

    public List<Object> indexColumna(String etiquetaColumna) {
        inicializarEtiquetas();  // Asegura que las etiquetas estén inicializadas
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);
        if (indiceColumna == -1) {
            System.out.println("Etiquetas de columnas disponibles: " + this.getEncabezados());
            throw new IllegalArgumentException("Etiqueta de columna no encontrada: " + etiquetaColumna);
        }
    
        Columna<?> columna = tabla.get(indiceColumna);
        return new ArrayList<>(columna.getColumna()); // Retorna una copia de la columna
    }

    public Object indexCelda(String etiquetaFila, String etiquetaColumna) {
        inicializarEtiquetas();  // Asegura que las etiquetas estén inicializadas
        int indiceFila = etiquetasFilas.indexOf(etiquetaFila);
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);
    
        if (indiceFila == -1) {
            throw new IllegalArgumentException("Etiqueta de fila no encontrada: " + etiquetaFila);
        }
        if (indiceColumna == -1) {
            throw new IllegalArgumentException("Etiqueta de columna no encontrada: " + etiquetaColumna);
        }
    
        return tabla.get(indiceColumna).getCelda(indiceFila);
    }
    
    
    
    public void imprimirTabla(){
        // for(Columna c: tabla){
        //     System.out.println(c );
            
        // }

        //ESTE METODO ES PARA QUE IMPRIMA LOS DATOS DE LA COLUMNA UNA ABAJO DE LA OTRA
        // Obtener el número de filas
        int numFilas = getNumeroFilas(); 

        // Iterar sobre cada fila
        for (int fila = 0; fila < numFilas; fila++) {
            StringBuilder sb = new StringBuilder(); // Usamos StringBuilder para construir la línea de salida

            // Iterar sobre cada columna
            for (Columna<?> columna : tabla) {
                // Obtener el valor de la celda en la fila actual
                Object valor = columna.getCelda(fila);
                // Agregar el valor al StringBuilder, manejando valores nulos
                sb.append(valor != null ? valor.toString() : "NA").append(" "); // Concatenar valor y un espacio
            }

            // Imprimir la línea construida, eliminando el espacio extra al final
            System.out.println(sb.toString().trim());
        }
    }
}



