package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

import java.io.Serializable;

public abstract class Producto implements Serializable {
    private String codigo;
    private String nombre;
    private double precioUnitario;
    private int stock;

    public Producto(String codigo, String nombre, double precioUnitario, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void aumentarStock(int cantidad) {
        stock += cantidad;
    }

    public void disminuirStock(int cantidad) throws StockInsuficienteException {
        if (cantidad > stock) {
            throw new StockInsuficienteException("Stock insuficiente para el producto: " + nombre);
        }
        stock -= cantidad;
    }

    public abstract double calcularImpuesto(int cantidad);
    public abstract String getTipo();

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | Tipo: " + getTipo() + " | Precio: " + precioUnitario + " | Stock: " + stock;
    }
}