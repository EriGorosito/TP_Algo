package Tabla;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tabla {
    private List<Columna<?>> tabla;
    private String delimitador;
    private boolean tieneEncabezado;


    public Tabla(){
        this.tabla = new ArrayList<>();
    }
    
    public Tabla(String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        this.delimitador = delimitador;
        this.tieneEncabezado = tieneEncabezado;
        this.tabla = new ArrayList<>();
        cargarCSV(rutaArchivo); 
    }

    //Para generar una estructura tavular a través de copia profunda de otra estructura del mismo tipo.
    public Tabla(Tabla otraTabla) {
        this.tabla = new ArrayList<>();
        this.delimitador = otraTabla.delimitador;
        this.tieneEncabezado = otraTabla.tieneEncabezado;
    
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
    
    public void imprimirTabla(){
        for(Columna c: tabla){
            System.out.println(c );
            
        }
    }

}

