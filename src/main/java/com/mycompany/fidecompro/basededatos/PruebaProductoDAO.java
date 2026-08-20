package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Producto;
import com.mycompany.fidecompro.ProductoExento;
import com.mycompany.fidecompro.ProductoGravado;

/**
 *
 * @author j.fonseca
 */

public class PruebaProductoDAO {
    
    public static void main(String[] args) {

        ProductoDAO productoDAO = new ProductoDAO();

        try {

            Producto gravado = new ProductoGravado(
                "P001",
                "Cafe",
                2500.00,
                10,
                13.0
            );

            Producto exento = new ProductoExento(
                "P002",
                "Arroz",
                1500.00,
                20
            );

            productoDAO.insertar(gravado);
            productoDAO.insertar(exento);

            System.out.println("Productos insertados correctamente.");

            Producto producto1 = productoDAO.buscarPorCodigo("P001");
            Producto producto2 = productoDAO.buscarPorCodigo("P002");

            if (producto1 != null) {
                System.out.println("Producto P001 encontrado.");
                System.out.println("Nombre: " + producto1.getNombre());
                System.out.println("Tipo: " + producto1.getClass().getSimpleName());
                System.out.println("Stock: " + producto1.getStock());
            }

            if (producto2 != null) {
                System.out.println("Producto P002 encontrado.");
                System.out.println("Nombre: " + producto2.getNombre());
                System.out.println("Tipo: " + producto2.getClass().getSimpleName());
                System.out.println("Stock: " + producto2.getStock());
            }

        } catch (Exception e) {

            System.out.println("Error al probar ProductoDAO.");
            e.printStackTrace();
        }
    }
}