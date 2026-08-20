package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Cliente;

/**
 *
 * @author j.fonseca
 */

public class PruebaClienteDAO {
    
    public static void main(String[] args) {

        ClienteDAO clienteDAO = new ClienteDAO();

        try {

            Cliente cliente = new Cliente(
                "C1",
                "Cliente de Prueba",
                "123456789",
                "8888-8888",
                "prueba@fidecompro.com",
                "San José"
            );

            clienteDAO.insertar(cliente);

            System.out.println("Cliente insertado correctamente.");

            Cliente encontrado = clienteDAO.buscarPorCedula("123456789");

            if (encontrado != null) {

                System.out.println("Cliente encontrado.");
                System.out.println("ID: " + encontrado.getId());
                System.out.println("Nombre: " + encontrado.getNombre());
                System.out.println("Cedula: " + encontrado.getCedula());

            } else {

                System.out.println("Cliente no encontrado.");
            }

        } catch (Exception e) {

            System.out.println("Error al probar ClienteDAO.");
            e.printStackTrace();
        }
    }
}