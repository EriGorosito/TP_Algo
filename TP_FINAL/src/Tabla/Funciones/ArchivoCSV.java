package Tabla.Funciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Tabla.ColumnaCadena;
import Tabla.Tabla;
import Tabla.Columna.Columna;
import Tabla.Columna.ColumnaBooleana;
import Tabla.Columna.ColumnaNumerica;
import Tabla.Excepciones.TamanioFilaException;

public class ArchivoCSV {
    public static void cargarCSV(Tabla tabla, String rutaArchivo, String delimitador, boolean tieneEncabezado) {
        List<String> encabezados = new ArrayList<>();
        List<String[]> filas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraFila = true;
            boolean columnasDefinidas = false;

            // Leer el archivo completo para almacenar las filas
            while ((linea = reader.readLine()) != null) {
                String[] valores = linea.split(delimitador);

                if (primeraFila && tieneEncabezado) {
                    encabezados.addAll(Arrays.asList(valores));
                    primeraFila = false;
                    continue;
                }

                // Almacenar cada fila en una lista temporal para analizar los tipos
                filas.add(valores);
                primeraFila = false;
            }

            // Validar el tipo de cada columna recorriendo todas las filas
            int numColumnas = filas.get(0).length;
            for (int i = 0; i < numColumnas; i++) {
                boolean esNumerico = true;
                boolean esBooleano = true;

                for (String[] fila : filas) {
                    String valor = fila[i].trim();

                    // Saltar valores nulos o vacíos
                    if (valor.equals("NA") || valor.isEmpty()) {
                        continue;
                    }

                    if (!esNumerico(valor)) {
                        esNumerico = false;
                    }
                    if (!esBooleano(valor)) {
                        esBooleano = false;
                    }
                }

                // Crear la columna en la tabla según el tipo dominante
                String nombreColumna = tieneEncabezado ? encabezados.get(i) : "Columna " + (i + 1);
                if (esNumerico) {
                    tabla.agregarColumna(new ColumnaNumerica(nombreColumna));
                } else if (esBooleano) {
                    tabla.agregarColumna(new ColumnaBooleana(nombreColumna));
                } else {
                    tabla.agregarColumna(new ColumnaCadena(nombreColumna));
                }
            }

            // Ahora, agregamos cada fila a la tabla con los valores validados
            for (String[] valores : filas) {
                Object[] fila = new Object[valores.length];

                for (int i = 0; i < valores.length; i++) {
                    String valor = valores[i].trim();
                    Columna<?> columna = tabla.getColumna(i);

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

                // Agregar la fila a la tabla
                agregarFila(tabla, fila);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean esNumerico(String valor) {
        try {
            Double.parseDouble(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean esBooleano(String valor) {
        return valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false") ||
                valor.equalsIgnoreCase("yes") || valor.equalsIgnoreCase("no");
    }

    public static void agregarFila(Tabla tabla, Object... valores) {
        if (valores.length != tabla.getCantColumna()) {
            throw new TamanioFilaException("Número de valores no coincide con el número de columnas.");
        }

        for (int i = 0; i < valores.length; i++) {
            Columna columna = tabla.getColumna(i);

            if (valores[i] == null) {
                columna.agregarNA(); // Si el valor es nulo, agregamos "NA"
            } else {
                columna.agregarDato(valores[i]);
            }
        }
    }

    public static void descargarACSV(Tabla tabla, String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (tieneEncabezado) {
                // Escribir encabezado
                for (int i = 0; i < tabla.getCantColumna(); i++) {
                    writer.write(tabla.getColumna(i).getEncabezado());
                    if (i < tabla.getCantColumna() - 1) {
                        writer.write(delimitador);
                    }
                }
                writer.newLine();
            }

            // Escribir datos fila por fila
            for (int i = 0; i < tabla.getCantFilas(); i++) { // Recorre cada fila
                for (int j = 0; j < tabla.getCantColumna(); j++) { // Recorre cada columna
                    Columna<?> columna = tabla.getColumna(j);
                    Object celda = columna.getCelda(i);

                    // Convertir a "NA" si la celda es null y luego escribir
                    writer.write(celda != null ? celda.toString() : "NA");

                    // Agrega delimitador entre valores, pero no al final de la fila
                    if (j < tabla.getCantColumna() - 1) {
                        writer.write(delimitador);
                    }
                }
                writer.newLine(); // Nueva línea para la siguiente fila
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
