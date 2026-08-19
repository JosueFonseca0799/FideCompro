package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

import java.io.Serializable;

public class LineaFactura implements Serializable {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double impuesto;

    public LineaFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioUnitario();
        this.impuesto = producto.calcularImpuesto(1);
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double calcularSubtotalLinea() {
        return precioUnitario * cantidad;
    }

    public double calcularImpuestoLinea() {
        return impuesto * cantidad;
    }

    public double calcularTotalLinea() {
        return calcularSubtotalLinea() + calcularImpuestoLinea();
    }

    @Override
    public String toString() {
        return producto.getNombre() + " x" + cantidad +
                " | Subtotal: " + calcularSubtotalLinea() +
                " | Impuesto: " + calcularImpuestoLinea() +
                " | Total: " + calcularTotalLinea();
    }
}
