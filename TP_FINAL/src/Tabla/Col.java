package Tabla;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Tabla.excepciones.TipoInvalido;

public class Col<T> {
    private List<T> columna;
    private String tipoDato;
    private Object encabezado;
    private static int contador;

    public Columna(){
        this.columna = new ArrayList<>();
    }

    public Columna (T[] columna){
        this.columna = new ArrayList<>(Arrays.asList(columna));
        this.tipoDato = definirtipo(this.columna.get(1));
        this.encabezado = this.columna.get(0);
    }

    public Columna (T[] columna, boolean tieneEncabezado){
        this.columna = new ArrayList<>(Arrays.asList(columna));
        this.tipoDato = definirtipo(this.columna.get(1));
        this.encabezado = definirEncabezado(tieneEncabezado);
        contador++;
    }

    private Object definirEncabezado(boolean tieneEncabezado) {
        Object encabezado;
        if (tieneEncabezado){
            encabezado = columna.get(0);
        }else{
            encabezado = contador;
        }
        return encabezado;
    }

    private String definirtipo(T celda) {
        String tipoDato;
        if (celda instanceof Boolean){
            tipoDato = "booleano";
        }else if (celda instanceof String){
            tipoDato = "string";
        }else if ((celda) instanceof Integer){
            tipoDato = "Entero";
        }else{
            tipoDato = "NA";
        }
        return tipoDato;
    }

    public Columna (List<T> columna){
        this.columna = columna;
    }
    
    public void agregarCelda(T celda, int posicion) throws TipoInvalido{
        if (definirtipo(celda).equals(tipoDato)){
            columna.add(posicion, celda);
        } else{
            throw new TipoInvalido("El tipo de dato de la celda no coincide con el de la columna");
        }
    }

    public void agregarCelda(T celda) throws TipoInvalido{
        //if (definirtipo(celda).equals(tipoDato)){
            columna.add(celda);
        //} else{
          //  throw new TipoInvalido("El tipo de dato de la celda no coincide con el de la columna");
        //}
    }

    public T reemplazarCelda(T celda, int posicion) throws TipoInvalido{
        T celdaVieja = columna.get(posicion);
        if (definirtipo(celda).equals(tipoDato)){
            columna.add(posicion, celda);
        } else{
            throw new TipoInvalido("El tipo de dato de la celda no coincide con el de la columna");
        }
        return celdaVieja;
    }

    public T devolverCelda(int posicion) throws Exception{
        if(posicion <= columna.size()){
            return columna.get(posicion);
        }
        throw new IndexOutOfBoundsException("La posicion está fuera de rango");
    }

}
