package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

public class ProductoExento extends Producto {

    public ProductoExento(String codigo, String nombre, double precioUnitario, int stock) {
        super(codigo, nombre, precioUnitario, stock);
    }

    @Override
    public double calcularImpuesto(int cantidad) {
        return 0;
    }

    @Override
    public String getTipo() {
        return "Exento";
    }
}