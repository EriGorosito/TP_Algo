package Tabla.Funciones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;

import Tabla.Tabla;
import Tabla.Columna.Columna;
import Tabla.Excepciones.EtiquetaFilaException;
import Tabla.Excepciones.FilaVaciaException;
import Tabla.OperadorLogico.OperadorLogico;

public class Filtrado {
    public static Tabla filtrar(Tabla tabla, List<String> columnasFiltrar, List<Predicate<Object>> predicados,
            OperadorLogico operadoresLogicos) {
        List<String> etiquetasFiltradas = new ArrayList<>();
        Tabla tablaFiltrada = tabla.copiaVacia();

        // Iterar por cada fila de la tabla actual
        for (int i = 0; i < tabla.getCantFilas(); i++) {
            boolean resultado = (operadoresLogicos == OperadorLogico.AND);
            boolean filaCumple = false; // variable que indica si la fila cumple con las condiciones

            // Evaluar cada predicado en la fila
            for (int j = 0; j < columnasFiltrar.size(); j++) {
                String nombreColumna = columnasFiltrar.get(j);
                Predicate<Object> predicado = predicados.get(j);
                String etiquetaFila = tabla.getEtiquetaFila(i);

                if (etiquetaFila == null) {
                    throw new EtiquetaFilaException("No se encontró la etiqueta para la fila con índice: " + i);
                }

                Object valor = tabla.indexCelda(etiquetaFila, nombreColumna);

                if (valor == null) {
                    continue; // Si no hay valor, saltamos a la siguiente columna
                }
                boolean cumpleCondicion;
                if (valor instanceof Integer) {
                    cumpleCondicion = predicado.test((Integer) valor);
                } else if (valor instanceof Double) {
                    cumpleCondicion = predicado.test((Double) valor);
                } else {
                    cumpleCondicion = predicado.test(valor);
                }
                // Si el operador es AND, debemos asegurarnos que todas las condiciones se
                // cumplan
                if (operadoresLogicos == OperadorLogico.AND) {
                    if (!cumpleCondicion) {
                        resultado = false; // Si alguna condición no cumple, la fila no pasa
                        break; // No es necesario seguir evaluando
                    }
                }
                // Si el operador es OR, basta que una condición sea verdadera para que la fila
                // pase
                else if (operadoresLogicos == OperadorLogico.OR) {
                    if (cumpleCondicion) {
                        filaCumple = true; // Al menos una columna cumple, la fila es válida
                        break; // No es necesario seguir evaluando
                    }
                }
            }
            // Si la fila cumple con todas las condiciones (según AND u OR), agregarla a las
            // filas filtradas
            if ((operadoresLogicos == OperadorLogico.AND && resultado)
                    || (operadoresLogicos == OperadorLogico.OR && filaCumple)) {
                tablaFiltrada.agregarFila(tabla.obtenerFila(i).toArray());
                etiquetasFiltradas.add(tabla.getEtiquetaFila(i));

            }
        }
        // Verificar si se encontraron etiquetas filatradas
        if (etiquetasFiltradas.isEmpty()) {
            throw new FilaVaciaException("No se encontraron filas que cumplan con los criterios de filtrado.");
        }

        tablaFiltrada.cambiarEtiquetas(etiquetasFiltradas);
        return tablaFiltrada;
    }

}
