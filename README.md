# Algoritmos 1 Trabajo Práctico Integrador

# Trabajo Final 
En este código presentamos una librería que permite manipular y analizar datos en forma tabular para lenguaje Java. El mismo permite cuantificar en tiempo el costo de su ejecución. 

## Tipo de datos
El objeto Tabla permite columnas de tipo numerico, booleano y cadena.

## Generación 
Una estructura de forma tabular o Tabla se puede generar de las siguientes maneras: 

1. Desde la carga de un formato compatible en disco (CSV)
El mismo recibe como parametros un String con la ruta del archivo csv, un booleano que indica si el csv a leer tiene o no encabezados (Si no tiene se generan por default con un número indicando el orden) y un string con el delimitador que a utilizar, si no se indica este último por default se utiliza ','

```java
//Ejemplo de uso

Tabla nombre_tabla = new Tabla(ruta_archivo, true, ";"); //con delimitador

Tabla nombre_tabla = new Tabla(ruta_archivo, true); //sin delimitador
```
2. A través de copia profunda de otra estructura del mismo tipo
Parametros: Otra instancia de tipo tabla

```java
//Ejemplo de uso
Tabla nombre_tabla = new Tabla(otra_tabla);

```

3. Desde una estructura de dos dimensiones nativa de Java
Parametros: estructura bidimnesional T[][], boolean tieneEncabezado

Obs: en caso de que no tenga encabezado se generaran automaticamente.

```java

//Ejemplo de uso/
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};

Tabla tabla = new Tabla(datos, false);
System.out.println(tabla);
//     Columna1       Columna2       Columna3       
//0    10             Hola           true
//1    20             Mundo          false
//2    30             Java           true
```
4. Desde una secuencia lineal nativa de Java

Parametros: secuencia lineal List<Object[]>, boolean tieneEncabezado
Obs: en caso de que no tenga encabezado se generaran automaticamente.

Especificaciones: la lista que se recibe debe contener las filas que a su vez son listas de datos
```java
//Ejemplo de uso/

List<Object[]> filas = new ArrayList<>();
        filas.add(new Object[]{"encabezado1", "encabezado2", "encabezado3"});
        filas.add(new Object[]{10, "Hola", true});
        filas.add(new Object[]{20, "Mundo", false});
        filas.add(new Object[]{30, "Java", true});

Tabla tabla = new Tabla(filas, true);
System.out.println(tabla);
// encabezado1    encabezado2    encabezado3    
// 0    10             Hola           true
// 1    20             Mundo          false
// 2    30             Java           true
```

## Información
Se puede conocer la siguiente información sobre una instancia dada:
- Cantidad de filas:
  **getCantFilas()**
- Cantidad de columnas
  **getCantColumnas**
- Etiquetas de filas:
      Para conocer las etiquetas de todas las filas:
      **getEtiquetasFilas()**, devuelve un Mapa donde la clave es la etiqueta y el valor la posicion de la fila en la tabla
      Para saber la etiqueta de una determinada fila:
      **getEtiquetaFila(int indice)**, devuelve un String con la etiqueta de la fila en la posicion indicada
- Etiquetas de columnas
**getEncabezados()**, devuelve una lista con los encabezados de toda la tabla
**getEncabezado(int indice)**, devuelve el encaabezado de la columna que esta en la posición indicada
- Tipos de datos de columnas:
**getTipoDatos()**, devuelve un mapa donde el encabezado es la clave y el valor el tipo de dato de cada columna

```java
//Ejemplo de uso/
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};

Tabla tabla = new Tabla(datos, false);
System.out.println(tabla.getCantFilas());
//3
System.out.println(tabla.getCantColumna());
//3
System.out.println(tabla.getTipoDatos());
//{Columna1=Numerica, Columna2=String, Columna3=Booleana}
System.out.println(tabla.getEncabezados());
// [Columna1, Columna2, Columna3]
System.out.println(tabla.getEncabezado(1));
// Columna2
System.out.println(tabla.getEtiquetasFilas());
//{0=0, 1=1, 2=2}
System.out.println(tabla.getEtiquetaFila(1));
//1 (es un string)
```
También se puede utilizar el método **info()** que imprime toda esta información:
```java
// Ejemplo con la tabla creada antes
//Ejemplo de uso/
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};

Tabla tabla = new Tabla(datos, false);
tabla.info();
// Información de la tabla:
// Cantidad de filas 3
// Cantidad de columnas: 3
// Columna: Columna1 - Tipo de Dato: Numerica
// Columna: Columna2 - Tipo de Dato: String
// Columna: Columna3 - Tipo de Dato: Booleana 
```
## Setters
Para cambiar los encabezados de las columnas de una Tabla se puede utilizar: 
**cambiarEncabezados(List<String> encabezados)**

Obs: La lista debe tener el mismo largo que la cantidad de columnas que tiene la tabla

Para cambiar las etiquetas de las filas:
**cambiarEtiquetasFilas(List<String> etiquetasFilas)**

Obs: La lista debe tener el mismo largo que la cantidad de filas
```java
Object[][] datos = {
    {"encabezado1", "encabezado2", "encabezado3"},
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};

Tabla tabla = new Tabla(datos, true);
List<String> encabezados = new ArrayList<>(Arrays.asList("A", "B", "C")); 
tabla.cambiarEncabezados(encabezados);
tabla.cambiarEtiquetas(new ArrayList<>(Arrays.asList("D", "E", "F")));
System.out.println(tabla);
// A              B              C
// D    10             Hola           true
// E    20             Mundo          false
// F    30             Java           true
```
## Acceso indexado
Se provee el acceso indexado a nivel fila y columna. Esto significa:
- Se puede conocer las celdas de una fila con el nombre de su etiqueta:
**indexFila(String etiquetaFila)**
  Devuelve una lista de tipo Object
  
- Se puede conocer las celdas de una columna:
**indexColumna(String encabezado)**
  Devuelve un ArrayList según el tipo de columna
  
- Se puede conocer el valor de una celda:
**indexCelda(String etiquetaFila, String etiquetaColumna)**
```java
   //Ejemplo de uso
 Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};
Tabla tabla = new Tabla(datos, false);
System.out.println("Fila 1: " + tabla.indexFila("1"));
//Fila 1: [20, Mundo, false]
System.out.println("Columna 'Columna 1': " +  tabla.indexColumna("Columna1"));
//Columna 'Columna 1': [10, 20, 30]
System.out.println("Celda en (1, Columna1): " + tabla.indexCelda("1", "Columna1"));
//Celda en (1, Columna1): 20
```

## Formatos de carga/descarga
El objeto tabla soporta la lectura y escritura entre estructuras en memoria y el formato CSV en disco especificando si tiene encabezados y el delimitador.
Para descargar una Tabla como archivo csv se debe agregar en la ruta el nombre que tendrá el archivo también.

**descargarACSV(String ruta_archivo, Boolean tieneEncabezado, String delimitador)**


```java
//Ejemplo de uso
Object[][] datos = {
            {10, "Hola", true},
            {20, "Mundo", false},
            {30, "Java", true}
        };

Tabla tabla = new Tabla(datos);
String descarga_rutaArchivo = "tabla_salida4.csv";
tabla.descargarACSV(descarga_rutaArchivo, true, ",");
```

La carga de un archivo CSV se puede relizar generando una Tabla desde un archivo csv.

## Visualización
Presentar de forma simple y sencilla en formato texto la información en forma de tabla. Se pueden definir tamaños máximos configurables para cortar la salida en caso de estructuras
grandes. Por ejemplo, mostrar un máximo de columnas o filas, y mostrar un máximo de caracteres por cada celda.


## Modificación

Una instancia de tipo Tabla puede modificarse de las siguientes forma:

a) Accediendo directo a una celda y asignando un nuevo valor
**modificarCelda(String encabezado, String etiquetaFila,  T nuevoValor)**

b) Insertando una columna nueva a partir de otra columna (con misma cantidad de elementos que filas)


c) Insertando una columna nueva a partir de una secuencia lineal nativa de Java (con misma cantidad de elementos que filas)

**`agregarColumna(List<Object> nuevaColumna)`**  
**`agregarColumna( String encabezado, List<Object> nuevaColumna)`** 

d) Eliminando una columna

**eliminarColumna(int indice)**
**eliminarColumna(String encabezado)**

e) Eliminando una fila
**eliminarFila(String etiqueta)**
```java
//Ejemplo de uso
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};

Tabla tabla = new Tabla(datos, false);

tabla.modificarCelda("Columna2","1", "cambio");
tabla.agregarColumna(new ArrayList<>(Arrays.asList("Agregar", "Nueva", "Columna")));
List<Object> nuevaColumna = new ArrayList<>(Arrays.asList(6, 4, 5));
tabla.agregarColumna("Encabezado", nuevaColumna);
tabla.eliminarColumna("Columna3");
tabla.eliminarFilaPorEtiqueta("0");
System.out.println(tabla);
// Columna1       Columna2       Columna4       Encabezado     
// 1    20             cambio         Nueva          4
// 2    30             Java           Columna        5

```

Una instancia de tipo Tabla puede modificarse de las siguientes forma:
### Modificar celda
Accediendo directo a una celda y asignando un nuevo valor
**modificarCelda(String etiquetaFila, String encabezado, T nuevoValor)**
```java
//Ejemplo de uso
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};

Tabla tabla = new Tabla(datos, false);
tabla.modificarCelda("Columna2","1", "cambio");
System.out.println(tabla);
// Columna1       Columna2       Columna3       
// 0    10             Hola           true
// 1    20             cambio         false
// 2    30             Java           true
```


### Insertar Columna
Insertando una columna nueva a partir de otra columna (con misma cantidad de elementos que filas)


Insertando una columna nueva a partir de una secuencia lineal nativa de Java (con misma cantidad de elementos que filas)

**`agregarColumna(List<Object> nuevaColumna)`**

**`agregarColumna(List<Object> nuevaColumna, String encabezado)`**

```java

//Ejemplo de uso con la tabla anterior
tabla.agregarColumna(new ArrayList<>(Arrays.asList("Agregar", "Nueva", "Columna")));
List<Object> nuevaColumna = new ArrayList<>(Arrays.asList(6, 4, 5));
tabla.agregarColumna("Encabezado", nuevaColumna);
System.out.println(tabla);
// Columna1       Columna2       Columna3       Columna4       Encabezado     
// 0    10             Hola           true           Agregar        6
// 1    20             Mundo          false          Nueva          4
// 2    30             Java           true           Columna        5

```
### Eliminar Columna

**eliminarColumna(int indice)**
**eliminarColumna(String encabezado)**
```java
//Ejemplo de uso utilizando la tabla creada en modificar celda

tabla.eliminarColumna("Columna3");
System.out.println(tabla);
// Columna1       Columna2       
// 0    10             Hola
// 1    20             Mundo
// 2    30             Java

```
### Eliminar Fila

**eliminarFila(String etiqueta)**
```java
//Ejemplo de uso

tabla.eliminarFilaPorEtiqueta("1");
System.out.println(tabla);
// Columna1       Columna2       Columna3       
// 0    10             Hola           true
// 2    30             Java           true

```

## Selección
Se pueden seleccionar determinadas filas y columnas a partir de sus etiquetas y encabezados. Esto genera una nueva instancia de tipo Tabla.
Además se pueden seleccionar únicamente encabezados y etiquetas completando con un null el parametro no deseado.

**seleccionar (List<String> etiquetaFilas, List<String> encabezados)**

Casos especiales:

**head(int x):** Devuelve las primeras x filas

**tail(int x)**: Devuelve las últimas x filas

```java
//Ejemplo de uso
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};
Tabla tabla = new Tabla(datos, false);

Tabla tabla_seleccion = tabla.seleccionar(Arrays.asList("1", "2"), Arrays.asList("Columna1"));
System.out.println(tabla_seleccion);
// Columna1       
// 1    10
// 2    20

//Visualizar 
System.out.println(tabla.seleccionar(Arrays.asList("1", "2"), null));
// Columna1       Columna2       Columna3       
// 1    10             Hola           true
// 2    20             Mundo          false
Tabla head = tabla.head(1);
System.out.println(head);
// Columna1       Columna2       Columna3
// 0    10             Hola           true
System.out.println(tabla.tail(2));
// Columna1       Columna2       Columna3
// 1    20             Mundo          false
// 2    30             Java           true
```


## Filtrado
Permitir la selección parcial de la estructura tabular a través de un filtro aplicado a valores de las celdas (query). Este filtro se puede componer de uno o más comparadores sobre cierta columna (<, >, =) que se combinan con operadores lógicos (and, or), filtrando así
aquellas filas donde las celdas cumplen aquella condición.

**`filtrar(List<String> columnasFiltrar, List<Predicate<Object>> predicados, OperadorLogico operadoresLogicos)`**

El operador lógico se debe poner en mayuculas y pueden ser unicamente OperadorLogico.AND o OperadorLogico.OR
```java
    Object[][] datos = {
        {10, "Hola", true},
        {20, "Mundo", false},
        {30, "Java", true}
};

Tabla tabla = new Tabla(datos, false);
List<String> columnas = List.of("Columna1", "Columna2");
List<Predicate<Object>> predicados = List.of(
                valor -> (Integer) valor  < 30,   
                valor -> valor.equals("Hola".trim())
                );

Tabla tablaFiltrada = tabla.filtrar(columnas, predicados, OperadorLogico.AND);
System.out.println(tablaFiltrada);
//     Columna1       Columna2       Columna3
//0    10             Hola           true
```

## Copia profunda
Se puede realizar la copia profunda de los elementos de una estructura para generar una nueva con mismos valores, pero independiente de la estructura original en memoria.
Esto se puede realizar creando una instancia a partir de otra como se mostro antes o utilizando la siguiente función.

**copiaProfunda()**
```java
    Object[][] datos = {
            {10, "Hola", true},
            {20, "Mundo", false},
            {30, "Java", true}
    };
    
    Tabla tabla = new Tabla(datos, false);
    Tabla copiaTabla = tabla.copiaProfunda();
    copiaTabla.cambiarEtiquetas(new ArrayList<>(Arrays.asList("D", "E", "F")));
    copiaTabla.eliminarColumna("Columna1");
    System.out.println(copiaTabla);
    System.out.println(tabla);
    // Columna2       Columna3       
    // D    Hola           true           
    // E    Mundo          false          
    // F    Java           true           
    
    //      Columna1       Columna2       Columna3       
    // 0    10             Hola           true           
    // 1    20             Mundo          false          
    // 2    30             Java           true   
    

```

## Concatenación
Se puede generar una nueva Tabla a partir de la concatenación de dos estructuras existentes. Esta operación es válida si las columnas de ambas estructuras coinciden, tanto en cantidad de columnas como también orden, tipo de datos y etiquetas asociadas. Las etiquetas de las filas son generadas automáticamente.
**concatenarTablas(Tabla otraTabla)**

Está operación utiliza la copia de las estructuras originales.
```java
    Object[][] datos = {
        {10, "Hola", true},
        {20, "Mundo", false},
        {30, "Java", true}
    };
    Tabla tabla = new Tabla(datos,false);
    
    List<Object[]> otrasfilas = new ArrayList<>();
    otrasfilas.add(new Object[]{13, "grupo", true});
    otrasfilas.add(new Object[]{24, "numero", false});
    otrasfilas.add(new Object[]{70, "10", true});
    
    Tabla tabla2 = new Tabla(otrasfilas,false);
    Tabla tablaConcatenada = tabla.concatenarTablas(tabla2);
    System.out.println(tablaConcatenada);
    // Columna1       Columna2       Columna3       
    // 0    10             Hola           true
    // 1    20             Mundo          false
    // 2    30             Java           true
    // 3    13             grupo          true
    // 4    24             numero         false
    // 5    70             10             true
```

## Ordenamiento
Se pueden ordenar las filas de la estructura según un criterio (ascendente o descendente) sobre una o más columnas. Si se opta por ordenar por más de una columna, se toma el orden ingresado de las columnas como precedencia para ordenar las filas.
**ordenarFilas(List<String> etiquetasColumnas, boolean ascendente)**

```java
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};
Tabla tabla = new Tabla(datos, false);
List<String> l = new ArrayList<>();
l.add("Columna1");
System.out.println(tabla.ordenarFilas(l, false));
// Columna1       Columna2       Columna3       
// 0    30             Java           true
// 1    20             Mundo          false
// 2    10             Hola           true
```
## Imputación

Se puede  modificar (rellenar) las celdas con valores faltantes (celdas con NA) con cierto valor literal indicado.

Para esto se pueden tomar dos caminos. 
Por un lado se puede utilizar el método **imputarNA(valor)** que cambia las columnas del tipo de valor ingresado o por otro lado **imputarNA(Map<String, Object>)** donde la clave es el encabezado de la columna y el valor es con el que modificarán los NA.

```java
//Ejemplo de uso 
    Object[][] datos = {
        {10, "Hola", null},
        {null, "Mundo", false},
        {30, null, true}
    };
    Tabla tabla = new Tabla(datos, false);
    tabla.imputarNA(5);
    tabla.imputarNA("modificacion");
    tabla.imputarNA(false);
    System.out.println(tabla);
```
```java
        //Ejemplo de uso con encabezados
        Object[][] datos = {
            {10, "Hola", null},
            {null, "Mundo", false},
            {30, null, true}
        };
        Tabla tabla = new Tabla(datos, false);
        Map<String, Object> valores = new HashMap<>();
        valores.put("Columna1", 5);
        valores.put("Columna2", "cambiar");
        valores.put("Columna3", (Boolean) true);
        tabla.imputarNA(valores);
        System.out.println(tabla);
        //      Columna1       Columna2       Columna3       
        // 0    10             Hola           true           
        // 1    5              Mundo          false          
        // 2    30             cambiar        true 
        
```
## Muestreo
Tabla ofrece una selección aleatoria de filas según un porcentaje del total de la estructura.

**muestreo(int cantidad)**

```java
Object[][] datos = {
    {10, "Hola", true},
    {20, "Mundo", false},
    {30, "Java", true}
};
Tabla tabla = new Tabla(datos,false);
System.out.println( tabla.muestreo(2));

```