package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

public class ProductoGravado extends Producto {
    private double porcentajeImpuesto;

    public ProductoGravado(String codigo, String nombre, double precioUnitario, int stock, double porcentajeImpuesto) {
        super(codigo, nombre, precioUnitario, stock);
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    @Override
    public double calcularImpuesto(int cantidad) {
        return getPrecioUnitario() * porcentajeImpuesto * cantidad;
    }

    @Override
    public String getTipo() {
        return "Gravado";
    }
}