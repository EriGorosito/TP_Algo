package Tabla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Tabla.Columna.Columna;
import Tabla.Columna.ColumnaBooleana;
import Tabla.Columna.ColumnaCadena;
import Tabla.Columna.ColumnaNumerica;
import Tabla.excepciones.*;

public class Tabla {
    private List<Columna<?>> tabla;
    private String delimitador = ",";
    private boolean tieneEncabezado = true;
    private LinkedHashMap<String, Integer> etiquetasFilas;

    public Tabla() {
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new LinkedHashMap<>();
    }

    public Tabla(String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        this.delimitador = delimitador;
        this.tieneEncabezado = tieneEncabezado;
        this.etiquetasFilas = new LinkedHashMap<>();
        this.tabla = new ArrayList<>();
        ArchivoCSV archivo = new ArchivoCSV();
        archivo.cargarCSV(this, rutaArchivo, delimitador, tieneEncabezado);
        inicializarEtiquetas();
    }

    // Constructor con delimitador por default ","
    public Tabla(String rutaArchivo, boolean tieneEncabezado) {
        this.tieneEncabezado = tieneEncabezado;
        this.etiquetasFilas = new LinkedHashMap<>();
        this.tabla = new ArrayList<>();
        ArchivoCSV archivo = new ArchivoCSV();
        archivo.cargarCSV(this, rutaArchivo, delimitador, tieneEncabezado);
        inicializarEtiquetas();
    }

    // CONSTRUCTOR para generar una estructura tavular a través de copia profunda de
    // otra estructura del mismo tipo.
    public Tabla(Tabla otraTabla) {
        this.delimitador = otraTabla.delimitador;
        this.tieneEncabezado = otraTabla.tieneEncabezado;
        this.tabla = new ArrayList<>();
        
        for (Columna<?> columna : otraTabla.tabla) {
            Columna<?> nuevaColumna = columna.clone(); 
            this.agregarColumna(nuevaColumna);         
        }
    }

    // CONSTRUCTOR que toma una matriz bidimensional para crear la tabla.(estructura
    // de dos dimensiones nativa de Java)
    public <T> Tabla(T[][] datos) {
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new LinkedHashMap<>();

        // Inicializar columnas según el número de columnas en la matriz
        int numColumnas = datos[0].length;
        for (int col = 0; col < numColumnas; col++) {
            // Detectar el tipo de dato en la primera fila
            T primerValor = datos[0][col];

            if (primerValor instanceof Number) {
                tabla.add(new ColumnaNumerica("Columna" + col));
            } else if (primerValor instanceof Boolean) {
                tabla.add(new ColumnaBooleana("Columna" + col));
            } else {
                tabla.add(new ColumnaCadena("Columna" + col));
            }
        }

        // Agregar cada fila de la matriz a la tabla
        for (T[] fila : datos) {
            agregarFila(fila);
        }
        inicializarEtiquetas();

    }
    // CONSTRUCTOR para una secuencia lineal nativa de Java.

    public Tabla(List<Object[]> filas) {
        this(); // Llama al constructor vacío para inicializar la tabla
 
 
        if (filas == null || filas.isEmpty()) {
            throw new FilaVaciaException("La lista de filas no puede estar vacía.");
        }
 
 
        int numColumnas = filas.get(0).length;
 
 
        // Determina el tipo de cada columna recorriendo todas las filas
        for (int col = 0; col < numColumnas; col++) {
            boolean esNumerico = true;
            boolean esBooleano = true;
 
 
            for (Object[] fila : filas) {
                Object valor = fila[col];
                if (valor != null) {
                    if (!(valor instanceof Number)) {
                        esNumerico = false;
                    }
                    if (!(valor instanceof Boolean)) {
                        esBooleano = false;
                    }
                }
            }
 
 
            // Crear la columna según el tipo dominante
            if (esNumerico) {
                tabla.add(new ColumnaNumerica("Columna " + (col + 1)));
            } else if (esBooleano) {
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
 

    private void inicializarEtiquetas() {
        for (int i = 0; i < getNumeroFilas(); i++) {
            String clave = String.valueOf(i);
            this.etiquetasFilas.put(clave, i);
        }
    }

    public Columna<?> getColumna(int posicion) {
        return tabla.get(posicion);
    }
    public Columna<?> getColumna(String encabezado) {
        for(Columna<?> columna : this.tabla){
            if(encabezado == columna.getEncabezado()){
                return columna;
            }
        }
        throw new ColumnaNoEncontrada ("No se encontro una columna con ese encabezado");
    }

    // para permitir la adición de columnas individuales.
    public void agregarColumna(Columna<?> columna) {
        tabla.add(columna);
    }

    public void agregarColumna(List<Object> secuencia) {
        int numero = getCantColumna() + 1;
 
 
        if (secuencia == null || secuencia.isEmpty()) {
            throw new IllegalArgumentException("La secuencia no puede ser nula o vacía");
        }
 
 
        // Determina el tipo de columna en función de los elementos de la secuencia
        Columna<?> nuevaColumna;
       
        // Verifica si todos los elementos son numéricos (o nulos)
        if (secuencia.stream().allMatch(elemento -> elemento == null || elemento instanceof Number)) {
            nuevaColumna = new ColumnaNumerica("Columna" + numero);
            for (Object elemento : secuencia) {
                if(elemento == null){
                    ((ColumnaNumerica) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaNumerica) nuevaColumna).agregarDato((Number) elemento); // Permite valores nulos
            }
        }
        // Verifica si todos los elementos son booleanos (o nulos)
        else if (secuencia.stream().allMatch(elemento -> elemento == null || elemento instanceof Boolean)) {
            nuevaColumna = new ColumnaBooleana("Columna" + numero);
            for (Object elemento : secuencia) {
                if(elemento == null){
                    ((ColumnaBooleana) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaBooleana) nuevaColumna).agregarDato((Boolean) elemento);  // Permite valores nulos
            }
        }
        // Si no son numéricos ni booleanos, se asume que son cadenas (o nulos)
        else {
            nuevaColumna = new ColumnaCadena("Columna" + numero);
            for (Object elemento : secuencia) {
                if(elemento == null){
                    ((ColumnaCadena) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaCadena) nuevaColumna).agregarDato(elemento == null ? null : elemento.toString());  // Permite valores nulos
            }
        }
 
 
        // Agrega la nueva columna a la tabla
        this.agregarColumna(nuevaColumna);
    }
 

    public void agregarFila(Object... valores) {


        if (valores.length != tabla.size()) {
            System.out.println(this.tabla.size());
            System.out.println(valores.length);
            throw new TamanioFilaException("Número de valores no coincide con el número de columnas.");
        }
 
 
        for (int i = 0; i < valores.length; i++) {
            Columna columna = tabla.get(i);
            if (valores[i] == null) {
                columna.agregarNA(); // Si el valor es nulo, agregamos "NA"
            } else if (columna instanceof ColumnaNumerica){
                columna.agregarDato((Number)valores[i]);
            }else if(columna instanceof ColumnaBooleana){
                columna.agregarDato((Boolean) valores[i]);
            }else{
                columna.agregarDato(valores[i].toString());
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

    public void descargarACSV(String rutaArchivo) {
        ArchivoCSV archivo = new ArchivoCSV();
        archivo.descargarACSV(this, rutaArchivo, tieneEncabezado, delimitador);
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

    public void info() {
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
        List<Object> fila = new ArrayList<>();

        Integer indicefila = etiquetasFilas.get(etiquetaFila);

        fila = this.obtenerFila(indicefila);
        return fila;

    }

    public List<Object> indexColumna(String etiquetaColumna) {
        inicializarEtiquetas(); // Asegura que las etiquetas estén inicializadas
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);
        if (indiceColumna == -1) {
            System.out.println("Etiquetas de columnas disponibles: " + this.getEncabezados());
            throw new EtiquetaColumnaException("Etiqueta de columna no encontrada: " + etiquetaColumna);
        }

        Columna<?> columna = tabla.get(indiceColumna);
        return new ArrayList<>(columna.getColumna()); // Retorna una copia de la columna
    }

    public Object indexCelda(String etiquetaFila, String etiquetaColumna) {
        inicializarEtiquetas(); // Asegura que las etiquetas estén inicializadas
        Integer indiceFila = etiquetasFilas.get(etiquetaFila);
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);

        if (indiceFila == -1) {
            throw new EtiquetaFilaException("Etiqueta de fila no encontrada: " + etiquetaFila);
        }
        if (indiceColumna == -1) {
            throw new EtiquetaColumnaException("Etiqueta de columna no encontrada: " + etiquetaColumna);
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
                        throw new EtiquetaColumnaException("Etiqueta de columna no encontrada: " + etiquetaColumna);
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
            tablaOrdenada.agregarColumna(columna.copia());
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
            throw new DiferenteCantidadColumnasException("Las tablas no tienen la misma cantidad de columnas.");
        }

        // Validación: Verificar que las columnas coincidan en tipo, orden y etiquetas
        for (int i = 0; i < tabla1.tabla.size(); i++) {
            Columna<?> columna1 = tabla1.tabla.get(i);
            Columna<?> columna2 = tabla2.tabla.get(i);

            // Verificar tipo de datos, nombre y orden
            if (!columna1.getClass().equals(columna2.getClass()) ||
                    !columna1.getEncabezado().equals(columna2.getEncabezado())) {
                throw new a("Las columnas no coinciden en tipo de datos, orden o etiquetas.");
            }
        }

        // Crear una nueva tabla para almacenar la concatenación de ambas
        Tabla tablaConcatenada = new Tabla();

        // Copiar columnas de tabla1 a la tabla concatenada (solo una vez, ya que ambas
        // tablas tienen las mismas columnas)
        for (Columna<?> columna : tabla1.tabla) {
            tablaConcatenada.tabla.add(columna.copia()); // Copia profunda de la columna
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
            throw new FilasRangoException("Cantidad de filas fuera de rango");
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

        if (cantidad > getNumeroFilas()) {
            throw new FilasRangoException("Cantidad de filas fuera de rango");
        }

        for (Columna<?> columna : tabla) {
            nuevaTabla.agregarColumna(columna.copia());
        }

        for (int i = getNumeroFilas() - cantidad; i < getNumeroFilas(); i++) {
            List<Object> fila = new ArrayList<>();
            for (Columna<?> columna : tabla) {
                fila.add(columna.getCelda(i));
            }
            nuevaTabla.agregarFila(fila.toArray());
        }

        return nuevaTabla;
    }

    public Tabla seleccionar(List<String> etiquetasFilas, List<String> encabezadosCol) {
        Tabla nuevaTabla = new Tabla();

        List<Integer> indicesColumnas = new ArrayList<>();
        if (encabezadosCol == null || encabezadosCol.isEmpty()) {
            for (int i = 0; i < this.tabla.size(); i++) {
                indicesColumnas.add(i);
                nuevaTabla.agregarColumna(tabla.get(i).copia());
            }
        } else {

            for (String encabezado : encabezadosCol) {
                int indice = this.getEncabezados().indexOf(encabezado.trim());
                if (indice != -1) {
                    indicesColumnas.add(indice);
                    nuevaTabla.agregarColumna(tabla.get(indice).copia()); // Agregar la columna seleccionada
                } else {
                    throw new EncabezadoNoEncontradoException("Encabezado no encontrado: " + encabezado);
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
                }
                nuevaTabla.agregarFila(nuevaFila.toArray());

            }
        }

        return nuevaTabla;
    }

    public Tabla filtrar(String filtro) {
        Tabla tablaFiltrada = new Tabla();
        tablaFiltrada.setColumnas(this.tabla);

        for (int i = 0; i < getNumeroFilas(); i++) {
            if (evaluarFiltro(filtro, i)) {
                tablaFiltrada.agregarFila(obtenerFila(i).toArray());
            }
        }

        return tablaFiltrada;
    }

    private boolean evaluarFiltro(String filtro, int filaIndex) {
        String[] condiciones = filtro.split("and|or|not");
        List<String> operadores = obtenerOperadores(filtro);

        boolean resultado = evaluarCondicion(condiciones[0].trim(), filaIndex);
        for (int i = 1; i < condiciones.length; i++) {
            boolean condicionEvaluada = evaluarCondicion(condiciones[i], filaIndex);
            String operador = operadores.get(i - 1);

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
            System.out.println(col.getEncabezado());
            System.out.println(columna);

            if (col.getEncabezado().equals(columna)) {
                return col.getCelda(filaIndex);
            }
        }
        throw new ColumnaNoEncontrada("Columna no encontrada: " + columna);
    }

    private List<String> obtenerOperadores(String filtro) {
        List<String> operadores = new ArrayList<>();
        String[] partes = filtro.split(" ");

        for (String parte : partes) {
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
            throw new MuestrasRangoException("La cantidad de muestras debe estar entre 1 y " + getNumeroFilas());
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

    public Tabla copiaIndependiente() {
        Tabla nuevaTabla = new Tabla();
        nuevaTabla.delimitador = this.delimitador;
        nuevaTabla.tieneEncabezado = this.tieneEncabezado;

        for (Columna columna : tabla) {
            Columna<?> nuevacol = columna.clone();
            nuevaTabla.agregarColumna(nuevacol);
        }

        return nuevaTabla;
    }

    public void eliminarFilaPorEtiqueta(String etiquetaFila) {
        // Verificar que la etiqueta de la fila exista
        Integer indiceFila = etiquetasFilas.get(etiquetaFila);
        if (indiceFila == null) {
            throw new EtiquetaFilaException("La etiqueta de fila no existe: " + etiquetaFila);
        }

        // Eliminar la fila correspondiente de cada columna en la copia
        for (Columna<?> columna : this.tabla) {
            columna.getColumna().remove((int) indiceFila); // Eliminar el elemento en el índice específico
        }

        // Actualizar las etiquetas de fila en la nueva tabla
        this.etiquetasFilas.clear();
        for (int i = 0; i < this.getNumeroFilas(); i++) {
            this.etiquetasFilas.put(String.valueOf(i), i);
        }
    }
    
    public void eliminarColumna(String encabezado){
        int indice = this.getEncabezados().indexOf(encabezado.trim());
        if (indice == -1){
            throw new ColumnaNoEncontrada ("No se encontro una columna con ese encabezado");
        }
        tabla.remove(indice);
    }

    public void modificarCelda(String encabezado, String etiquetaFila, Object nuevoValor){
        int indiceColumna = this.getEncabezados().indexOf(encabezado);
        Integer indiceFila = etiquetasFilas.get(etiquetaFila);

        if (indiceColumna == -1) {
            throw new ColumnaNoEncontrada("La columna con el encabezado especificado no existe.");
        }
        if (indiceFila == -1) {
            throw new EtiquetaFilaException("Etiqueta de fila no encontrada: ");
        }
        Columna columna = this.getColumna(indiceColumna);
        Object celda = columna.getCelda(indiceFila);

        if ((celda instanceof Number && nuevoValor instanceof Number) || 
        (celda instanceof String && nuevoValor instanceof String) || 
        (celda instanceof Boolean && nuevoValor instanceof Boolean)) {

            this.tabla.get(indiceColumna).modificarDato(indiceFila, nuevoValor);

        }
        else {
            throw new TipoDatoException("El tipo de dato del nuevo valor no coincide con el tipo de la celda.");
        }
    }
}