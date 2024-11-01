package Tabla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tabla {
    private List<Columna<?>> tabla;
    private String delimitador;
    private boolean tieneEncabezado;
    private List<String> etiquetasFilas;

    public Tabla() {
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.delimitador = ",";
        this.tieneEncabezado = true;
    }

    public Tabla(String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        this.delimitador = delimitador;
        this.tieneEncabezado = tieneEncabezado;
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.tabla = new ArrayList<>();
        cargarCSV(rutaArchivo);
    }

    // CONSTRUCTOR para generar una estructura tavular a través de copia profunda de
    // otra estructura del mismo tipo.
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

    // CONSTRUCTOR que toma una matriz bidimensional para crear la tabla.(estructura
    // de dos dimensiones nativa de Java)
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
    // CONSTRUCTOR para una secuencia lineal nativa de Java.

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

    public void cargarCSV(String rutaArchivo) {
        List<String> encabezados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraFila = true;
            boolean columnasdefinidas = false;

            while ((linea = reader.readLine()) != null) {
                String[] valores = linea.split(delimitador);

                if (primeraFila && tieneEncabezado) {
                    // Si hay encabezado, lo leemos
                    encabezados.addAll(Arrays.asList(valores));
                    primeraFila = false;
                    continue;
                }
                if (!columnasdefinidas) {
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
                    // System.out.println(columna);
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

        } catch (IOException e) {
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

    // para permitir la adición de columnas individuales.
    public void agregarColumna(Columna<?> columna) {
        tabla.add(columna);
    }

    public void agregarFila(Object... valores) {

        if (valores.length != tabla.size()) {
            System.out.println(this.tabla.size());
            System.out.println(valores.length);
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

    private List<Object> obtenerFila(int indice) {
        List<Object> fila = new ArrayList<>();
        for (Columna<?> columna : tabla) {
            fila.add(columna.getCelda(indice));
        }
        return fila;
    }

    // Método para escribir datos a un archivo CSV
    public void descargarACSV(String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (tieneEncabezado) {
                // Escribir encabezado
                for (int i = 0; i < tabla.size(); i++) {
                    writer.write(tabla.get(i).getEncabezado());
                    if (i < tabla.size() - 1) {
                        writer.write(delimitador);
                    }
                }
                writer.newLine();
            }

            // Escribir datos fila por fila
            for (int i = 0; i < getNumeroFilas(); i++) { // Recorre cada fila
                for (int j = 0; j < tabla.size(); j++) { // Recorre cada columna
                    Columna<?> columna = tabla.get(j);
                    Object celda = columna.getCelda(i);

                    // Convertir a "NA" si la celda es null y luego escribir
                    writer.write(celda != null ? celda.toString() : "NA");

                    // Agrega delimitador entre valores, pero no al final de la fila
                    if (j < tabla.size() - 1) {
                        writer.write(delimitador);
                    }
                }
                writer.newLine(); // Nueva línea para la siguiente fila
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumeroFilas() {
        return tabla.isEmpty() ? 0 : tabla.get(0).getColumna().size();
    }

    public int getCantColumna() {
        return tabla.size();
    }

    public List<String> getEncabezados() {
        List<String> encabezados = new ArrayList<>();
        for (Columna columna : tabla) {
            String encabezado = columna.getEncabezado(); // Obtener el encabezado

            // Verificar si el encabezado es nulo
            if (encabezado != null) {
                encabezados.add(encabezado.trim()); // Agregar encabezado sin espacios en blanco
            } else {
                encabezados.add("NA"); // Agregar un valor por defecto si es nulo
            }
            // encabezados.add(columna.getEncabezado().trim());
        }
        return encabezados;
    }

    public Map<String, String> getTipoDatos() {
        Map<String, String> tipodeDatos = new HashMap<>();
        for (Columna columna : tabla) {
            String encabezado = columna.getEncabezado();
            String tipoDato = columna.getTipoDato();
            tipodeDatos.put(encabezado, tipoDato);
        }
        return tipodeDatos;
    }

    public void infoTabla() {
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
        inicializarEtiquetas(); // Asegura que las etiquetas estén inicializadas
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);
        if (indiceColumna == -1) {
            System.out.println("Etiquetas de columnas disponibles: " + this.getEncabezados());
            throw new IllegalArgumentException("Etiqueta de columna no encontrada: " + etiquetaColumna);
        }

        Columna<?> columna = tabla.get(indiceColumna);
        return new ArrayList<>(columna.getColumna()); // Retorna una copia de la columna
    }

    public Object indexCelda(String etiquetaFila, String etiquetaColumna) {
        inicializarEtiquetas(); // Asegura que las etiquetas estén inicializadas
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

    public Tabla ordenarFilas(List<String> etiquetasColumnas, boolean ascendente) {
        List<List<Object>> filas = new ArrayList<>();

        // Recopilar los datos en una lista de filas
        for (int i = 0; i < getNumeroFilas(); i++) {
            List<Object> fila = new ArrayList<>();
            for (Columna<?> columna : tabla) {
                fila.add(columna.getCelda(i));
            }
            filas.add(fila);
        }

        // Crear un comparador para las filas
        Collections.sort(filas, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> fila1, List<Object> fila2) {
                for (String etiquetaColumna : etiquetasColumnas) {
                    int indiceColumna = getEncabezados().indexOf(etiquetaColumna);
                    if (indiceColumna == -1) {
                        throw new IllegalArgumentException("Etiqueta de columna no encontrada: " + etiquetaColumna);
                    }

                    Comparable valor1 = (Comparable) fila1.get(indiceColumna);
                    Comparable valor2 = (Comparable) fila2.get(indiceColumna);

                    int comparacion = valor1.compareTo(valor2);
                    if (comparacion != 0) {
                        return ascendente ? comparacion : -comparacion;
                    }
                }
                return 0; // Son iguales en todos los criterios
            }
        });

        // Crear una nueva instancia de Tabla
        Tabla tablaOrdenada = new Tabla();

        // Agregar las columnas a la nueva tabla
        for (Columna<?> columna : tabla) {
            tablaOrdenada.agregarColumna(columna.clone()); // Asegúrate de que Columna tenga un método clone()
        }

        // Agregar las filas ordenadas a la nueva tabla
        for (List<Object> fila : filas) {
            tablaOrdenada.agregarFila(fila.toArray());
        }

        return tablaOrdenada;
    }

    public static Tabla concatenarTablas(Tabla tabla1, Tabla tabla2) {
        // Validación: Comparar cantidad de columnas
        if (tabla1.tabla.size() != tabla2.tabla.size()) {
            throw new IllegalArgumentException("Las tablas no tienen la misma cantidad de columnas.");
        }

        // Validación: Verificar que las columnas coincidan en tipo, orden y etiquetas
        for (int i = 0; i < tabla1.tabla.size(); i++) {
            Columna<?> columna1 = tabla1.tabla.get(i);
            Columna<?> columna2 = tabla2.tabla.get(i);

            // Verificar tipo de datos, nombre y orden
            if (!columna1.getClass().equals(columna2.getClass()) ||
                    !columna1.getEncabezado().equals(columna2.getEncabezado())) {
                throw new IllegalArgumentException("Las columnas no coinciden en tipo de datos, orden o etiquetas.");
            }
        }

        // Crear una nueva tabla para almacenar la concatenación de ambas
        Tabla tablaConcatenada = new Tabla();

        // Copiar columnas de tabla1 a la tabla concatenada (solo una vez, ya que ambas
        // tablas tienen las mismas columnas)
        for (Columna<?> columna : tabla1.tabla) {
            tablaConcatenada.tabla.add(columna.clone()); // Copia profunda de la columna
        }

        // Agregar filas de tabla1
        for (int i = 0; i < tabla1.getNumeroFilas(); i++) {
            tablaConcatenada.agregarFila(tabla1.obtenerFila(i).toArray());
        }

        // Agregar filas de tabla2
        for (int i = 0; i < tabla2.getNumeroFilas(); i++) {
            tablaConcatenada.agregarFila(tabla2.obtenerFila(i).toArray());
        }

        return tablaConcatenada;
    }

    public Tabla head(int cantidad) {
        Tabla nuevaTabla = new Tabla();

        if (cantidad > getNumeroFilas()) {
            throw new IndexOutOfBoundsException("Cantidad de filas fuera de rango");
        }

        for (Columna<?> columna : tabla) {
            nuevaTabla.agregarColumna(columna.copia());
        }

        for (int i = 0; i < cantidad; i++) {
            List<Object> fila = new ArrayList<>();
            for (Columna<?> columna : tabla) {
                fila.add(columna.getCelda(i));
            }
            nuevaTabla.agregarFila(fila.toArray());
        }

        return nuevaTabla;
    }

    public Tabla tail(int cantidad) {
        Tabla nuevaTabla = new Tabla();
        if (cantidad > tabla.size()) {
            throw new IndexOutOfBoundsException("Cantidad de columnas fuera de rango");
        }

        for (int i = 0; i < cantidad; i++) {
            nuevaTabla.agregarColumna(tabla.get(i).clone());
        }

        return nuevaTabla;
    }

    public Tabla seleccionar(List<String> etiquetasFilas, List<String> encabezadosCol) {
        Tabla nuevaTabla = new Tabla();

        List<Integer> indicesColumnas = new ArrayList<>();
        if (encabezadosCol == null || encabezadosCol.isEmpty()) {
            for (int i = 0; i < this.tabla.size(); i++) {
                indicesColumnas.add(i);
                System.out.println(tabla.get(i).clone());
                nuevaTabla.agregarColumna(tabla.get(i).copia());
            }
        } else {

            for (String encabezado : encabezadosCol) {
                int indice = this.getEncabezados().indexOf(encabezado.trim());
                if (indice != -1) {
                    indicesColumnas.add(indice);
                    nuevaTabla.agregarColumna(tabla.get(indice).copia()); // Agregar la columna seleccionada
                } else {
                    throw new IllegalArgumentException("Encabezado no encontrado: " + encabezado);
                }
            }
        }

        if (etiquetasFilas == null || etiquetasFilas.isEmpty()) {
            for (int i = 0; i < this.getNumeroFilas(); i++) {
                List<Object> nuevaFila = new ArrayList<>();
                for (int indiceColumna : indicesColumnas) {
                    nuevaFila.add(this.tabla.get(indiceColumna).getCelda(i));
                }
                nuevaTabla.agregarFila(nuevaFila.toArray());
            }
        } else {
            // Seleccionar solo las filas indicadas en etiquetasFilas
            for (String etiquetaFila : etiquetasFilas) {
                List<Object> nuevaFila = new ArrayList<>();
                int indiceFila = etiquetasFilas.indexOf(etiquetaFila);
                for (int indiceColumna : indicesColumnas) {
                    nuevaFila.add(tabla.get(indiceColumna).getCelda(indiceFila));
                    System.out.println(nuevaFila.size());
                }
                nuevaTabla.agregarFila(nuevaFila.toArray());

            }
        }

        return nuevaTabla;
    }


    public Tabla filtrar(String filtro){
        Tabla tablaFiltrada = new Tabla();
        tablaFiltrada.setColumnas(this.tabla);
   

        for(int i = 0; i < getNumeroFilas(); i++){
            if (evaluarFiltro(filtro, i)) {
                tablaFiltrada.agregarFila(obtenerFila(i).toArray());
            }
        }

        return tablaFiltrada;
    }

    private boolean evaluarFiltro(String filtro, int filaIndex){
        String[] condiciones = filtro.split("and|or|not");
        List<String> operadores = obtenerOperadores(filtro);
        
        boolean resultado = evaluarCondicion(condiciones[0].trim(), filaIndex);
        for(int i = 1; i < condiciones.length; i++){
            boolean condicionEvaluada = evaluarCondicion(condiciones[i], filaIndex);
            String operador = operadores.get(i-1);

            if (operador.equals("and")) {
                resultado = resultado && condicionEvaluada;
            } else if (operador.equals("or")) {
                resultado = resultado || condicionEvaluada;
            } else if (operador.equals("not")) {
                resultado = !resultado;
            }
        }
        return resultado;
    }

    private boolean evaluarCondicion(String filtro, int filaIndex) {
        String[] partes = filtro.split(" ");
        String columna = partes[0];
        String operador = partes[1];
        String condicion = partes[2];
        
        Object valorCelda = obtenerValorColumna(filaIndex, columna);

        switch (operador) {
            case "=":
                return valorCelda.toString().equals(condicion);
            case "<":
                return Double.parseDouble(valorCelda.toString()) < Double.parseDouble(condicion);
            case ">":
                return Double.parseDouble(valorCelda.toString()) > Double.parseDouble(condicion);
            default:
                return false;
        }
    }

    private Object obtenerValorColumna(int filaIndex, String columna) {
        for (Columna<?> col : tabla) {
            if (col.getEncabezado().equals(columna)) {
                return col.getCelda(filaIndex);
            }
        }
        throw new IllegalArgumentException("Columna no encontrada: " + columna);
    }

    private List<String> obtenerOperadores(String filtro) {
        List<String> operadores = new ArrayList<>();
        String[] partes = filtro.split(" ");

        for(String parte : partes){
            if (parte.equals("and") || parte.equals("or") || parte.equals("not")) {
                operadores.add(parte);
            }
        }
        return operadores;
    }

    public void setColumnas(List<Columna<?>> columnas) {
        this.tabla = new ArrayList<>(columnas);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Agregar los encabezados de las columnas
        sb.append(String.format("%-5s", "")); // Espacio vacío para el índice
        for (Columna<?> columna : tabla) {
            sb.append(String.format("%-15s", columna.getEncabezado()));
        }
        sb.append("\n");

        // Obtener el número máximo de filas en cualquier columna
        int maxFilas = tabla.stream().mapToInt(Columna::largo).max().orElse(0);

        // Mostrar solo las primeras 5 y las últimas 5 filas si hay más de 10 filas
        if (maxFilas > 10) {
            for (int i = 0; i < 5; i++) {
                sb.append(String.format("%-5d", i)); // Imprimir el índice de fila
                for (Columna<?> columna : tabla) {
                    Object celda = (i < columna.largo()) ? columna.getCelda(i) : "NA";
                    sb.append(String.format("%-15s", celda == null ? "NA" : celda.toString()));
                }
                sb.append("\n");
            }

            // Imprimir "..." debajo de los encabezados
            sb.append(String.format("%-5s", " ")); // Espacio vacío para el índice
            for (Columna<?> columna : tabla) {
                sb.append(String.format("%-15s", "...")); // Puntos debajo de cada columna
            }
            sb.append("\n");

            for (int i = maxFilas - 5; i < maxFilas; i++) {
                sb.append(String.format("%-5d", i)); // Imprimir el índice de fila
                for (Columna<?> columna : tabla) {
                    Object celda = (i < columna.largo()) ? columna.getCelda(i) : "NA";
                    sb.append(String.format("%-15s", celda == null ? "NA" : celda.toString()));
                }
                sb.append("\n");
            }
        } else {
            // Agregar todas las filas si hay 10 o menos
            for (int i = 0; i < maxFilas; i++) {
                sb.append(String.format("%-5d", i)); // Imprimir el índice de fila
                for (Columna<?> columna : tabla) {
                    Object celda = (i < columna.largo()) ? columna.getCelda(i) : "NA";
                    sb.append(String.format("%-15s", celda == null ? "NA" : celda.toString()));
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public void imprimirTabla() {
        // for(Columna c: tabla){
        // System.out.println(c );

        // }

        // ESTE METODO ES PARA QUE IMPRIMA LOS DATOS DE LA COLUMNA UNA ABAJO DE LA OTRA
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

    public Tabla muestreo(int cantidad) {
        if (cantidad <= 0 || cantidad > getNumeroFilas()) {
            throw new IllegalArgumentException("La cantidad de muestras debe estar entre 1 y " + getNumeroFilas());
        }

        Tabla muestra = new Tabla(); // Nueva tabla para almacenar la muestra

        // Clonamos las columnas de la tabla original
        for (Columna<?> columna : tabla) {
            muestra.agregarColumna(columna.copia());
        }

        // Generador de números aleatorios
        Random random = new Random();
        List<Integer> indicesSeleccionados = new ArrayList<>();

        // Generar índices aleatorios sin repetición
        while (indicesSeleccionados.size() < cantidad) {
            int indice = random.nextInt(getNumeroFilas());
            if (!indicesSeleccionados.contains(indice)) {
                indicesSeleccionados.add(indice);
            }
        }

        // Agregar las filas seleccionadas a la nueva tabla
        for (int indice : indicesSeleccionados) {
            List<Object> fila = obtenerFila(indice);
            muestra.agregarFila(fila.toArray());
        }

        return muestra;
    }

}
