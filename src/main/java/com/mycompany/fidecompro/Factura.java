package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Factura implements Serializable {
    private String numeroFactura;
    private LocalDateTime fecha;
    private Cliente cliente;
    private Usuario usuario;
    private String estado;
    private List<LineaFactura> lineas;

    public Factura(String numeroFactura, Cliente cliente, Usuario usuario) {
        this.numeroFactura = numeroFactura;
        this.fecha = LocalDateTime.now();
        this.cliente = cliente;
        this.usuario = usuario;
        this.estado = "BORRADOR";
        this.lineas = new ArrayList<>();
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getEstado() {
        return estado;
    }

    public List<LineaFactura> getLineas() {
        return lineas;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarLinea(LineaFactura linea) {
        lineas.add(linea);
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (LineaFactura linea : lineas) {
            subtotal += linea.calcularSubtotalLinea();
        }
        return subtotal;
    }

    public double calcularImpuestoTotal() {
        double impuesto = 0;
        for (LineaFactura linea : lineas) {
            impuesto += linea.calcularImpuestoLinea();
        }
        return impuesto;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularImpuestoTotal();
    }

    @Override
    public String toString() {
        return "Factura " + numeroFactura +
                " | Cliente: " + cliente.getNombre() +
                " | Estado: " + estado +
                " | Total: " + calcularTotal();
    }
}