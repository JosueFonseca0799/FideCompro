package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Cliente;
import com.mycompany.fidecompro.Factura;
import com.mycompany.fidecompro.Producto;
import com.mycompany.fidecompro.ProductoExento;
import com.mycompany.fidecompro.Usuario;

/**
 *
 * @author j.fonseca
 */

public class PruebaFacturaDAO {
    
    public static void main(String[] args) {

        try {

            Cliente cliente = new Cliente(
                "C1",
                "Cliente de Prueba",
                "123456789",
                "8888-8888",
                "prueba@fidecompro.com",
                "San José"
            );

            Usuario usuario = new Usuario(
                "U1",
                "Administrador",
                "admin",
                "1234",
                "ADMIN",
                true
            );

            Producto producto = new ProductoExento(
                "P002",
                "Arroz",
                1500.00,
                20
            );

            Factura factura = new Factura(
                "FAC-001",
                cliente,
                usuario
            );

            factura.agregarLinea(
                new com.mycompany.fidecompro.LineaFactura(
                    producto,
                    2
                )
            );

            FacturaDAO facturaDAO = new FacturaDAO();

            facturaDAO.insertar(factura);

            System.out.println("Factura insertada correctamente.");

            double impuesto = 0.0;

            facturaDAO.insertarLinea(
                factura.getNumeroFactura(),
                producto.getCodigo(),
                2,
                producto.getPrecioUnitario(),
                impuesto
            );

            System.out.println("Linea de factura insertada correctamente.");

            if (facturaDAO.existe("FAC-001")) {
                System.out.println("Factura encontrada correctamente.");
            } else {
                System.out.println("Factura no encontrada.");
            }

        } catch (Exception e) {

            System.out.println("Error al probar FacturaDAO.");
            e.printStackTrace();
        }
    }
}