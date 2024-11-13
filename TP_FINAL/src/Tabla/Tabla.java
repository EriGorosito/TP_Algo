package Tabla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import Tabla.Columna.Columna;
import Tabla.Columna.ColumnaBooleana;
import Tabla.Columna.ColumnaNumerica;
import Tabla.ColumnaCadena;
import Tabla.Excepciones.ColumnaNoEncontrada;
import Tabla.Excepciones.DiferenteCantidadColumnasException;
import Tabla.Excepciones.EncabezadosException;
import Tabla.Excepciones.EtiquetaColumnaException;
import Tabla.Excepciones.EtiquetaFilaException;
import Tabla.Excepciones.FilaVaciaException;
import Tabla.Excepciones.MuestrasRangoException;
import Tabla.Excepciones.TamanioFilaException;
import Tabla.Excepciones.TipoDatoException;
import Tabla.Excepciones.a;
import Tabla.Funciones.ArchivoCSV;
import Tabla.Funciones.Filtrado;
import Tabla.Funciones.Seleccionar;
import Tabla.OperadorLogico.OperadorLogico;

public class Tabla {
    private List<Columna<?>> tabla;
    private boolean tieneEncabezado = true;
    private LinkedHashMap<String, Integer> etiquetasFilas;

    public Tabla() {
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new LinkedHashMap<>();
    }

    public Tabla(String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        this.tieneEncabezado = tieneEncabezado;
        this.etiquetasFilas = new LinkedHashMap<>();
        this.tabla = new ArrayList<>();
        ArchivoCSV.cargarCSV(this, rutaArchivo, delimitador, tieneEncabezado);
        inicializarEtiquetas();
    }

    // Constructor con delimitador por default ","
    public Tabla(String rutaArchivo, boolean tieneEncabezado) {
        this.tieneEncabezado = tieneEncabezado;
        this.etiquetasFilas = new LinkedHashMap<>();
        this.tabla = new ArrayList<>();
        ArchivoCSV.cargarCSV(this, rutaArchivo, ",", tieneEncabezado);
        inicializarEtiquetas();
    }

    // CONSTRUCTOR para generar una estructura tabular a través de copia profunda
    public Tabla(Tabla otraTabla) {
        this.tieneEncabezado = otraTabla.tieneEncabezado;
        this.etiquetasFilas = new LinkedHashMap(otraTabla.etiquetasFilas);
        this.tabla = new ArrayList<>();

        for (Columna<?> columna : otraTabla.tabla) {
            Columna<?> nuevaColumna = columna.clone();
            this.agregarColumna(nuevaColumna);
        }
        inicializarEtiquetas();
    }

    // CONSTRUCTOR que toma una matriz bidimensional para crear la tabla.
    public <T> Tabla(T[][] datos, boolean tieneEncabezado) {
        this.tabla = new ArrayList<>();
        this.etiquetasFilas = new LinkedHashMap<>();

        int numColumnas = datos[0].length;
        crearColumnas(datos, numColumnas, tieneEncabezado);

        boolean primeraLinea = true;
        for (T[] fila : datos) {
            if (primeraLinea && tieneEncabezado) {
                primeraLinea = false;
                continue;
            }
            agregarFila(fila);
        }
        inicializarEtiquetas();
    }

    // CONSTRUCTOR para una secuencia lineal nativa de Java.
    public <T> Tabla(List<Object[]> filas, boolean tieneEncabezado) {
        this();
        if (filas == null || filas.isEmpty()) {
            throw new FilaVaciaException("La lista de filas no puede estar vacía.");
        }
        int numColumnas = filas.get(0).length;
        crearColumnas(filas.toArray(new Object[0][]), numColumnas, tieneEncabezado);
        boolean primeraLinea = true;
        for (Object[] fila : filas) {
            if (primeraLinea && tieneEncabezado) {
                primeraLinea = false;
                continue;
            }
            agregarFila(fila);
        }
        inicializarEtiquetas();
    }

    private <T> void crearColumnas(T[][] datos, int numColumnas, boolean tieneEncabezado) {
        for (int col = 0; col < numColumnas; col++) {
            Class<?> tipoColumna = detectarTipoColumna(datos, col, tieneEncabezado);
            String nombreColumna = tieneEncabezado ? datos[0][col].toString() : "Columna" + (col + 1);

            if (tipoColumna == Number.class) {
                tabla.add(new ColumnaNumerica(nombreColumna));
            } else if (tipoColumna == Boolean.class) {
                tabla.add(new ColumnaBooleana(nombreColumna));
            } else {
                tabla.add(new ColumnaCadena(nombreColumna));
            }
        }
    }

    // Método para detectar el tipo de columna
    private <T> Class<?> detectarTipoColumna(T[][] datos, int col, boolean tieneEncabezado) {
        boolean esNumerico = true;
        boolean esBooleano = true;
        boolean primeraLinea = true;

        for (T[] fila : datos) {
            if (primeraLinea && tieneEncabezado) {
                primeraLinea = false;
                continue;
            }
            T valor = fila[col];
            if (valor != null) {
                if (!(valor instanceof Number))
                    esNumerico = false;
                if (!(valor instanceof Boolean))
                    esBooleano = false;
            }
        }

        if (esNumerico) {
            return Number.class;
        }
        if (esBooleano) {
            return Boolean.class;
        }
        return String.class;
    }

    private void inicializarEtiquetas() {
        if (etiquetasFilas.size() != getCantFilas()) {
            etiquetasFilas.clear();
            for (int i = 0; i < getCantFilas(); i++) {
                String clave = String.valueOf(i);
                this.etiquetasFilas.put(clave, i);
            }
        }

    }

    public void cambiarEtiquetas(List<String> nuevasEtiquetas) {
        if (this.getCantFilas() != nuevasEtiquetas.size()) {
            throw new IllegalArgumentException(
                    "El tamaño de la lista de nuevas claves debe coincidir con el tamaño del mapa.");
        }
        // LinkedHashMap<String, Integer> nuevoMapa = new LinkedHashMap<>();
        etiquetasFilas.clear();
        for (int i = 0; i < getCantFilas(); i++) {
            String clave = nuevasEtiquetas.get(i);
            this.etiquetasFilas.put(clave, i);
        }

    }

    public void cambiarEncabezados(List<String> nuevosEncabezados) {
        if (nuevosEncabezados.size() != getCantColumna()) {
            throw new EncabezadosException("Se esperaba una lista de largo " + getCantColumna()
                    + " y se paso una de largo " + nuevosEncabezados.size());
        }
        for (int i = 0; i < getCantColumna(); i++) {
            this.getColumna(i).cambiarEncabezado(nuevosEncabezados.get(i));
        }
    }

    public void agregarColumna(Columna<?> columna) {
        tabla.add(columna);

    }

    public void agregarColumna(List<Object> secuencia) {
        int numero = getCantColumna() + 1;

        if (secuencia == null || secuencia.isEmpty()) {
            throw new IllegalArgumentException("La secuencia no puede ser nula o vacía");
        }

        Columna<?> nuevaColumna;

        // Verifica si todos los elementos son numéricos (o nulos)
        if (secuencia.stream().allMatch(elemento -> elemento == null || elemento instanceof Number)) {
            nuevaColumna = new ColumnaNumerica("Columna" + numero);
            for (Object elemento : secuencia) {
                if (elemento == null) {
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
                if (elemento == null) {
                    ((ColumnaBooleana) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaBooleana) nuevaColumna).agregarDato((Boolean) elemento); // Permite valores nulos
            }
        }
        // Si no son numéricos ni booleanos, se asume que son cadenas (o nulos)
        else {
            nuevaColumna = new ColumnaCadena("Columna" + numero);
            for (Object elemento : secuencia) {
                if (elemento == null) {
                    ((ColumnaCadena) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaCadena) nuevaColumna).agregarDato(elemento == null ? null : elemento.toString()); // Permite
                                                                                                           // valores
                                                                                                           // nulos
            }

            // Asignar etiquetas para cada fila

        }
        // Agrega la nueva columna a la tabla
        this.agregarColumna(nuevaColumna);
    }

    public void agregarColumna(String encabezado, List<Object> secuencia) {
        if (secuencia == null || secuencia.isEmpty()) {
            throw new IllegalArgumentException("La secuencia no puede ser nula o vacía");
        }

        Columna<?> nuevaColumna;

        // Verifica si todos los elementos son numéricos (o nulos)
        if (secuencia.stream().allMatch(elemento -> elemento == null || elemento instanceof Number)) {
            nuevaColumna = new ColumnaNumerica(encabezado);
            for (Object elemento : secuencia) {
                if (elemento == null) {
                    ((ColumnaNumerica) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaNumerica) nuevaColumna).agregarDato((Number) elemento); // Permite valores nulos
            }
        }
        // Verifica si todos los elementos son booleanos (o nulos)
        else if (secuencia.stream().allMatch(elemento -> elemento == null || elemento instanceof Boolean)) {
            nuevaColumna = new ColumnaBooleana(encabezado);
            for (Object elemento : secuencia) {
                if (elemento == null) {
                    ((ColumnaBooleana) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaBooleana) nuevaColumna).agregarDato((Boolean) elemento); // Permite valores nulos
            }
        }
        // Si no son numéricos ni booleanos, se asume que son cadenas
        else {
            nuevaColumna = new ColumnaCadena(encabezado);
            for (Object elemento : secuencia) {
                if (elemento == null) {
                    ((ColumnaCadena) nuevaColumna).agregarNA();
                    continue;
                }
                ((ColumnaCadena) nuevaColumna).agregarDato(elemento == null ? null : elemento.toString()); // Permite
                                                                                                           // valores
                                                                                                           // nulos
            }
        }
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
            } else if (columna instanceof ColumnaNumerica) {
                columna.agregarDato((Number) valores[i]);
            } else if (columna instanceof ColumnaBooleana) {
                columna.agregarDato((Boolean) valores[i]);
            } else {
                columna.agregarDato(valores[i].toString());
            }
        }
    }

    public List<Object> obtenerFila(int indice) {
        List<Object> fila = new ArrayList<>();
        for (Columna<?> columna : tabla) {
            fila.add(columna.getCelda(indice));
        }
        return fila;
    }

    public Columna<?> getColumna(int posicion) {
        return tabla.get(posicion);
    }

    public Columna<?> getColumna(String encabezado) {
        for (Columna<?> columna : this.tabla) {
            if (encabezado.equals(columna.getEncabezado())) {
                return columna;
            }
        }
        throw new ColumnaNoEncontrada("No se encontro una columna con ese encabezado");
    }

    public int getCantFilas() {
        return tabla.isEmpty() ? 0 : tabla.get(0).getColumna().size();
    }

    public int getCantColumna() {
        return tabla.size();
    }

    public List<String> getEncabezados() {
        List<String> encabezados = new ArrayList<>();
        if (this.tieneEncabezado == true) {
            for (Columna columna : tabla) {
                String encabezado = columna.getEncabezado();
                if (encabezado != null) {
                    encabezados.add(encabezado.trim());
                } else {
                    encabezados.add("NA");
                }
            }
        }
        return encabezados;
    }

    public String getEncabezado(int indice) {
        return getColumna(indice).getEncabezado();
    }

    public Map<String, String> getTipoDatos() {
        Map<String, String> tipodeDatos = new LinkedHashMap<>();
        for (Columna columna : tabla) {
            String encabezado = columna.getEncabezado();
            String tipoDato = columna.getTipoDato();
            tipodeDatos.put(encabezado, tipoDato);
        }
        return tipodeDatos;
    }

    public Map<String, Integer> getEtiquetasFilas() {
        inicializarEtiquetas();
        return new LinkedHashMap<>(etiquetasFilas);
    }

    public String getEtiquetaFila(int indice) {
        inicializarEtiquetas();
        for (Map.Entry<String, Integer> entry : etiquetasFilas.entrySet()) {
            if (entry.getValue() == indice) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void info() {
        System.out.println("Información de la tabla:");
        System.out.println("Cantidad de filas " + this.getCantFilas());
        System.out.println("Cantidad de columnas: " + this.getCantColumna());
        Map<String, String> tipoDeDatos = this.getTipoDatos();
        for (Map.Entry<String, String> entry : tipoDeDatos.entrySet()) {
            System.out.println("Columna: " + entry.getKey() + " - Tipo de Dato: " + entry.getValue());
        }
    }

    public void descargarACSV(String rutaArchivo, boolean tieneEncabezado, String delimitador) {
        ArchivoCSV.descargarACSV(this, rutaArchivo, tieneEncabezado, delimitador);
    }

    public List<Object> indexFila(String etiquetaFila) {
        inicializarEtiquetas();
        List<Object> fila = new ArrayList<>();
        Integer indicefila = etiquetasFilas.get(etiquetaFila);
        if (indicefila == null) {
            throw new EtiquetaFilaException("Etiqueta de fila no encontrada: " + etiquetaFila);
        }
        fila = this.obtenerFila(indicefila);
        return fila;
    }

    public List<Object> indexColumna(String etiquetaColumna) {
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);
        if (indiceColumna == -1) {
            System.out.println("Etiquetas de columnas disponibles: " + this.getEncabezados());
            throw new EtiquetaColumnaException("Etiqueta de columna no encontrada: " + etiquetaColumna);
        }
        Columna<?> columna = tabla.get(indiceColumna);
        return new ArrayList<>(columna.getColumna());
    }

    public Object indexCelda(String etiquetaFila, String etiquetaColumna) {
        inicializarEtiquetas(); // Asegura que las etiquetas estén inicializadas
        Integer indiceFila = etiquetasFilas.get(etiquetaFila);
        int indiceColumna = this.getEncabezados().indexOf(etiquetaColumna);

        if (indiceFila == null) {
            throw new EtiquetaFilaException("Etiqueta de fila no encontrada: " + etiquetaFila);
        }
        if (indiceColumna == -1) {
            throw new EtiquetaColumnaException("Etiqueta de columna no encontrada: " + etiquetaColumna);
        }

        return tabla.get(indiceColumna).getCelda(indiceFila);
    }

    public Tabla ordenarFilas(List<String> etiquetasColumnas, boolean ascendente) {
        List<List<Object>> filas = new ArrayList<>();
        List<List<Object>> filasConEtiquetas = new ArrayList<>();

        // Recopilar las etiquetas y datos de filas en una lista
        for (int i = 0; i < getCantFilas(); i++) {
            List<Object> filaConEtiqueta = new ArrayList<>();
            filaConEtiqueta.add(getEtiquetaFila(i));
            List<Object> fila = new ArrayList<>();
            for (Columna<?> columna : tabla) {
                fila.add(columna.getCelda(i));
            }
            filaConEtiqueta.add(fila); // Agregar la fila como lista
            filasConEtiquetas.add(filaConEtiqueta);
        }

        // Crear un comparador para las filas
        Collections.sort(filasConEtiquetas, new Comparator<List<Object>>() {
            @Override
            public int compare(List<Object> filaConEtiqueta1, List<Object> filaConEtiqueta2) {
                List<Object> fila1 = (List<Object>) filaConEtiqueta1.get(1);
                List<Object> fila2 = (List<Object>) filaConEtiqueta2.get(1);
                for (String etiquetaColumna : etiquetasColumnas) {
                    int indiceColumna = getEncabezados().indexOf(etiquetaColumna);
                    if (indiceColumna == -1) {
                        throw new EtiquetaColumnaException("Etiqueta de columna no encontrada: " + etiquetaColumna);
                    }

                    // Obtener los valores de las celdas en las filas
                    Object valor1 = fila1.get(indiceColumna);
                    Object valor2 = fila2.get(indiceColumna);

                    // Si ambos son null, son iguales
                    if (valor1 == null && valor2 == null) {
                        continue;
                    }

                    // Si solo uno es null, consideramos el null como menor
                    if (valor1 == null) {
                        return ascendente ? -1 : 1; // El valor1 es null, se coloca después si es ascendente
                    }
                    if (valor2 == null) {
                        return ascendente ? 1 : -1; // El valor2 es null, se coloca después si es ascendente
                    }
                    Comparable comparable1 = (Comparable) valor1;
                    Comparable comparable2 = (Comparable) valor2;

                    int comparacion = comparable1.compareTo(comparable2);
                    if (comparacion != 0) {
                        return ascendente ? comparacion : -comparacion;
                    }

                }
                return 0; // Son iguales en todos los criterios
            }
        });

        Tabla tablaOrdenada = this.copiaVacia();
        List<String> etiquetasOrdenadas = new ArrayList<>();
        for (List<Object> filaConEtiqueta : filasConEtiquetas) {
            etiquetasOrdenadas.add((String) filaConEtiqueta.get(0)); // Añadir etiqueta original
            tablaOrdenada.agregarFila(((List<Object>) filaConEtiqueta.get(1)).toArray());
        }

        tablaOrdenada.cambiarEtiquetas(etiquetasOrdenadas);
        return tablaOrdenada;
    }

    public Tabla concatenarTablas(Tabla tabla1) {
        // Validación: Comparar cantidad de columnas
        if (tabla1.getCantColumna() != this.getCantColumna()) {
            throw new DiferenteCantidadColumnasException("Las tablas no tienen la misma cantidad de columnas.");
        }
        // Validación: Verificar que las columnas coincidan en tipo, orden y etiquetas
        for (int i = 0; i < tabla1.getCantColumna(); i++) {
            Columna<?> columna1 = tabla1.getColumna(i);
            Columna<?> columna2 = this.getColumna(i);

            if (!columna2.equals(columna1)) {
                throw new a("Las columnas no coinciden en tipo de datos, orden o etiquetas.");
            }
        }
        Tabla tablaConcatenada = copiaProfunda();
        // Agregar filas de tabla2
        for (int i = 0; i < tabla1.getCantFilas(); i++) {
            tablaConcatenada.agregarFila(tabla1.obtenerFila(i).toArray());
        }
        tablaConcatenada.inicializarEtiquetas();
        return tablaConcatenada;
    }

    public Tabla head(int cantidad) {
        return Seleccionar.head(this, cantidad);
    }

    public Tabla tail(int cantidad) {
        return Seleccionar.tail(this, cantidad);
    }

    public Tabla seleccionar(List<String> etiquetasFilas, List<String> encabezadosCol) {
        Tabla tablaSeleccionada = Seleccionar.seleccionar(this, etiquetasFilas, encabezadosCol);
        return tablaSeleccionada;
    }

    public Tabla filtrar(List<String> columnasFiltrar, List<Predicate<Object>> predicados,
            OperadorLogico operadoresLogicos) {
        return Filtrado.filtrar(this, columnasFiltrar, predicados, operadoresLogicos);
    }

    public Tabla muestreo(int cantidad) {
        if (cantidad <= 0 || cantidad > getCantFilas()) {
            throw new MuestrasRangoException("La cantidad de muestras debe estar entre 1 y " + getCantFilas());
        }

        Tabla muestra = this.copiaVacia();
        // Generador de números aleatorios
        Random random = new Random();
        List<Integer> indicesSeleccionados = new ArrayList<>();
        List<String> etiquetas = new ArrayList<>();

        // Generar índices aleatorios sin repetición
        while (indicesSeleccionados.size() < cantidad) {
            int indice = random.nextInt(getCantFilas());
            if (!indicesSeleccionados.contains(indice)) {
                indicesSeleccionados.add(indice);
            }
        }
        // Agregar las filas seleccionadas a la nueva tabla
        for (int indice : indicesSeleccionados) {
            List<?> fila = obtenerFila(indice);
            etiquetas.add(this.getEtiquetaFila(indice));
            muestra.agregarFila(fila.toArray());

        }
        muestra.cambiarEtiquetas(etiquetas);
        return muestra;
    }

    public Tabla copiaProfunda() {
        Tabla nuevaTabla = new Tabla();
        nuevaTabla.tieneEncabezado = this.tieneEncabezado;
        nuevaTabla.etiquetasFilas = new LinkedHashMap<>(this.etiquetasFilas);

        for (Columna columna : this.tabla) {
            Columna<?> nuevacol = columna.clone();
            nuevaTabla.agregarColumna(nuevacol);
        }
        return nuevaTabla;
    }

    public Tabla copiaVacia() {
        Tabla nuevaTabla = new Tabla();
        nuevaTabla.tieneEncabezado = this.tieneEncabezado;
        nuevaTabla.etiquetasFilas = new LinkedHashMap<>();
        for (Columna columna : tabla) {
            nuevaTabla.agregarColumna(columna.copiaEstructura());
        }
        return nuevaTabla;
    }

    public void eliminarFilaPorEtiqueta(String etiquetaFila) {
        inicializarEtiquetas();
        Integer indiceFila = etiquetasFilas.get(etiquetaFila);
        if (indiceFila == null) {
            throw new EtiquetaFilaException("La etiqueta de fila no existe: " + etiquetaFila);
        }

        for (Columna<?> columna : this.tabla) {
            columna.getColumna().remove((int) indiceFila); // Eliminar el elemento en el índice específico
        }

        actualizarEtiquetas(etiquetaFila, indiceFila);

    }

    private void actualizarEtiquetas(String etiquetaFila, Integer indice) {
        etiquetasFilas.remove(etiquetaFila);
        for (Map.Entry<String, Integer> entry : etiquetasFilas.entrySet()) {
            int currentIndice = entry.getValue();
            if (currentIndice > indice) {
                etiquetasFilas.put(entry.getKey(), currentIndice - 1);
            }
        }
    }

    public void eliminarColumna(String encabezado) {
        int indice = this.getEncabezados().indexOf(encabezado.trim());
        if (indice == -1) {
            throw new ColumnaNoEncontrada("No se encontro una columna con ese encabezado");
        }
        tabla.remove(indice);
    }

    public void eliminarColumna(int indice) {
        if (indice > getCantColumna()) {
            throw new DiferenteCantidadColumnasException("El indice de la columna está fuera de rango");
        }
        tabla.remove(indice);
    }

    public void modificarCelda(String encabezado, String etiquetaFila, String nuevoValor) {
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

        if (celda instanceof String) {
            this.tabla.get(indiceColumna).modificarDato(indiceFila, nuevoValor);
        } else {
            throw new TipoDatoException("El tipo de dato del nuevo valor no coincide con el tipo de la celda.");
        }
    }

    public void modificarCelda(String encabezado, String etiquetaFila, Number nuevoValor) {
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

        if (celda instanceof Number) {
            this.tabla.get(indiceColumna).modificarDato(indiceFila, nuevoValor);
        } else {
            throw new TipoDatoException("El tipo de dato del nuevo valor no coincide con el tipo de la celda.");
        }
    }

    public void modificarCelda(String encabezado, String etiquetaFila, Boolean nuevoValor) {
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

        if (celda instanceof Boolean) {
            this.tabla.get(indiceColumna).modificarDato(indiceFila, nuevoValor);
        } else {
            throw new TipoDatoException("El tipo de dato del nuevo valor no coincide con el tipo de la celda.");
        }
    }

    public void imputarNA(String nuevoValor) {
        for (Columna columna : this.tabla) {
            int i = 0;
            for (Object celda : columna.getColumna()) {
                if (celda == null) {
                    if (columna.getTipoDato() == "String") {
                        columna.modificarDato(i, nuevoValor);
                    }
                }
                i++;
            }
        }
    }

    public void imputarNA(Number nuevoValor) {
        for (Columna columna : this.tabla) {
            int i = 0;
            for (Object celda : columna.getColumna()) {
                if (celda == null) {
                    if (columna.getTipoDato() == "Numerica") {
                        columna.modificarDato(i, nuevoValor);
                    }
                }
                i++;
            }
        }
    }

    public void imputarNA(boolean nuevoValor) {

        for (Columna columna : this.tabla) {
            int i = 0;
            for (Object celda : columna.getColumna()) {
                if (celda == null) {
                    if (columna.getTipoDato() == "Booleana") {
                        columna.modificarDato(i, nuevoValor);
                    }
                }
                i++;
            }
        }
    }

    public void imputarNA(Map<String, Object> valores) {
        for (Map.Entry<String, Object> entry : valores.entrySet()) {
            String encabezado = entry.getKey();
            Object nuevoValor = entry.getValue();
            List encabezados = this.getEncabezados();
            if (encabezados.isEmpty()) {
                throw new EncabezadosException("La tabla no tiene encabezados");
            } else if (encabezados.contains(encabezado)) {
                Columna columna = getColumna(encabezado);
                int i = 0;
                for (Object celda : columna.getColumna()) {
                    if (celda == null) {
                        if ((columna.getTipoDato() == "Numerica" && nuevoValor instanceof Number) ||
                                (columna.getTipoDato() == "String" && nuevoValor instanceof String) ||
                                (columna.getTipoDato() == "Booleana" && nuevoValor instanceof Boolean)) {
                            columna.modificarDato(i, nuevoValor);
                        } else {
                            throw new TipoDatoException("Tipo de dato incompatible");
                        }
                    }
                    i++;
                }
            } else {
                System.out.println("El encabezado " + encabezado + " no se encuentra en la tabla");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        inicializarEtiquetas();
        // Agregar los encabezados de las columnas
        sb.append(String.format("%-5s", "")); // Espacio vacío para el índice
        for (Columna<?> columna : tabla) {
            sb.append(String.format("%-15s", columna.getEncabezado()));
        }
        sb.append("\n");

        // Obtener el número máximo de filas en cualquier columna
        int maxFilas = tabla.stream().mapToInt(Columna::largo).max().orElse(0);

        List<String> claves = new ArrayList<>(etiquetasFilas.keySet());
        // Mostrar solo las primeras 5 y las últimas 5 filas si hay más de 10 filas
        if (maxFilas > 10) {
            for (int i = 0; i < 5; i++) {
                sb.append(String.format("%-5s", claves.get(i))); // Imprimir el índice de fila
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
                sb.append(String.format("%-5s", claves.get(i))); // Imprimir el índice de fila
                for (Columna<?> columna : tabla) {
                    Object celda = (i < columna.largo()) ? columna.getCelda(i) : "NA";
                    sb.append(String.format("%-15s", celda == null ? "NA" : celda.toString()));
                }
                sb.append("\n");
            }
        } else {
            // Agregar todas las filas si hay 10 o menos
            for (int i = 0; i < maxFilas; i++) {
                sb.append(String.format("%-5s", claves.get(i))); // Imprimir el índice de fila
                for (Columna<?> columna : tabla) {
                    Object celda = (i < columna.largo()) ? columna.getCelda(i) : "NA";
                    sb.append(String.format("%-15s", celda == null ? "NA" : celda.toString()));
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}