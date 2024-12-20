package Tabla.Columna;

import java.util.ArrayList;
import java.util.List;

public abstract class Columna<T> {
    protected String encabezado;
    protected List<T> columna;

    public Columna(String encabezado) {
        this.encabezado = encabezado;
        this.columna = new ArrayList<>();
    }

    public String getEncabezado() {
        return encabezado;
    }

    public List<T> getColumna() {
        return columna;
    }

    public T getCelda(int i) {
        return columna.get(i);
    }

    public int largo() {
        return columna.size();
    }

    public abstract void agregarDato(T celda);

    public void agregarNA() {
        columna.add(null);
    }

    public void cambiarEncabezado(String nuevoEncabezado) {
        this.encabezado = nuevoEncabezado;
    }

    public abstract boolean validarDato(T celda);

    public abstract void modificarDato(int indice, Object nuevoValor);

    public abstract boolean equals(Object otro);

    public abstract String getTipoDato();

    // Copia profunda
    public abstract Columna<T> clone();

    // Copia estructura
    public abstract Columna<T> copiaEstructura();

    @Override
    public String toString() {
        String salida = "";
        for (Object c : columna) {
            if (c == null) {
                salida += " NA ";
            } else {
                salida += " " + c;
            }
        }
        return salida;
    }

}
