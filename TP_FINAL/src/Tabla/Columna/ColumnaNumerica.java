package Tabla.Columna;

import java.util.ArrayList;
import java.util.Objects;

import Tabla.Excepciones.TipoDatoException;

public class ColumnaNumerica extends Columna<Number> {

    public ColumnaNumerica(String encabezado) {
        super(encabezado);

    }

    // Constructor de copia profunda
    public ColumnaNumerica(ColumnaNumerica otraColumna) {
        super(otraColumna.getEncabezado());
        for (Number valor : otraColumna.getColumna()) {
            this.columna.add(valor); // Copia cada valor
        }
    }

    @Override
    public void agregarDato(Number celda) {
        if (validarDato(celda)) {
            columna.add(celda);
        } else {
            throw new TipoDatoException("El dato no es un número válido.");
        }
    }

    @Override
    public boolean validarDato(Number celda) {
        // En este caso simplemente comprobamos que el dato no sea nulo
        return celda != null;
    }

    @Override
    public String getTipoDato() {
        return "Numerica";
    }

    @Override
    public Columna<Number> clone() {
        ColumnaNumerica nuevaColumna = new ColumnaNumerica(this.getEncabezado());
        nuevaColumna.columna = new ArrayList<>(this.columna); // Copia los datos
        return nuevaColumna;
    }

    @Override
    public Columna<Number> copiaEstructura() {
        return new ColumnaNumerica(this.getEncabezado());
    }

    @Override
    public void modificarDato(int indice, Object nuevoValor) {
        this.columna.set(indice, (Number) nuevoValor);
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }

        if (otro == null || this.getClass() != otro.getClass()) {
            return false;
        }

        Columna otro2 = (ColumnaNumerica) otro;
        return Objects.equals(this.getEncabezado(), otro2.getEncabezado());
    }
}
