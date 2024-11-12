package Tabla;

import java.util.ArrayList;
import java.util.List;
import Tabla.Columna.Columna;
import Tabla.Excepciones.EncabezadoNoEncontradoException;
import Tabla.Excepciones.FilasRangoException;

public class Seleccionar {
    public static Tabla seleccionar(Tabla tabla, List<String> etiquetasFilas, List<String> encabezadosCol){
         Tabla nuevaTabla = new Tabla();

        List<Integer> indicesColumnas = new ArrayList<>();
        if (encabezadosCol == null || encabezadosCol.isEmpty()) {
            for (int i = 0; i < tabla.getCantColumna(); i++) {
                indicesColumnas.add(i);
                nuevaTabla.agregarColumna(tabla.getColumna(i).copia());
            }
        } else {

            for (String encabezado : encabezadosCol) {
                int indice = tabla.getEncabezados().indexOf(encabezado.trim());
                if (indice != -1) {
                    indicesColumnas.add(indice);
                    nuevaTabla.agregarColumna(tabla.getColumna(indice).copia()); // Agregar la columna seleccionada
                } else {
                    throw new EncabezadoNoEncontradoException("Encabezado no encontrado: " + encabezado);
                }
            }
        }

        if (etiquetasFilas == null || etiquetasFilas.isEmpty()) {
            for (int i = 0; i < tabla.getCantFilas(); i++) {
                List<Object> nuevaFila = new ArrayList<>();
                for (int indiceColumna : indicesColumnas) {
                    nuevaFila.add(tabla.getColumna(indiceColumna).getCelda(i));
                }
                nuevaTabla.agregarFila(nuevaFila.toArray());
            }
        } else {
            // Seleccionar solo las filas indicadas en etiquetasFilas
            for (String etiquetaFila : etiquetasFilas) {
                List<Object> nuevaFila = new ArrayList<>();
                int indiceFila = etiquetasFilas.indexOf(etiquetaFila);
                for (int indiceColumna : indicesColumnas) {
                    nuevaFila.add(tabla.getColumna(indiceColumna).getCelda(indiceFila));
                }
                nuevaTabla.agregarFila(nuevaFila.toArray());

            }
        }
        nuevaTabla.cambiarEtiquetas(etiquetasFilas);
        return nuevaTabla;
    }

    public static Tabla head(Tabla tabla, int cantidad) {
        Tabla nuevaTabla = new Tabla();
        List<String> etiquetas = new ArrayList<>();

        if (cantidad > tabla.getCantFilas()) {
            throw new FilasRangoException("Cantidad de filas fuera de rango");
        }

        for(int i = 0; i < tabla.getCantColumna(); i++){
            nuevaTabla.agregarColumna(tabla.getColumna(i).copia());
        }

        for (int i = 0; i < cantidad; i++) {
            List<Object> fila = new ArrayList<>();

            for(int j = 0; j < tabla.getCantColumna(); j++){
                fila.add(tabla.getColumna(j).getCelda(i));
            }
            nuevaTabla.agregarFila(fila.toArray());
            etiquetas.add(tabla.getEtiquetaFila(i));
        }
        nuevaTabla.cambiarEtiquetas(etiquetas);
        return nuevaTabla;
    }

        public static Tabla tail(Tabla tabla, int cantidad) {
            Tabla nuevaTabla = new Tabla();
            List<String> etiquetas = new ArrayList<>();
    
            if (cantidad > tabla.getCantFilas()) {
                throw new FilasRangoException("Cantidad de filas fuera de rango");
            }
            for(int i = 0; i < tabla.getCantColumna(); i++){
                nuevaTabla.agregarColumna(tabla.getColumna(i).copia());
            }
            for (int i = tabla.getCantFilas() - cantidad; i < tabla.getCantFilas(); i++) {
                List<Object> fila = new ArrayList<>();
                for(int j = 0; j < tabla.getCantColumna(); j++){
                    fila.add(tabla.getColumna(j).getCelda(i));
                }
                nuevaTabla.agregarFila(fila.toArray());
                etiquetas.add(tabla.getEtiquetaFila(i));
            }
            nuevaTabla.cambiarEtiquetas(etiquetas);
            return nuevaTabla;
        }
    
}
