package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Usuario;

/**
 *
 * @author j.fonseca
 */

public class PruebaUsuarioDAO {

    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {

            Usuario usuario = usuarioDAO.autenticar("admin", "1234");

            if (usuario != null) {

                System.out.println("Autenticación exitosa.");
                System.out.println("ID: " + usuario.getId());
                System.out.println("Nombre: " + usuario.getNombre());
                System.out.println("Usuario: " + usuario.getUsername());
                System.out.println("Rol: " + usuario.getRol());
                System.out.println("Activo: " + usuario.isActivo());

            } else {

                System.out.println("Usuario o contraseña incorrectos.");
            }

        } catch (Exception e) {

            System.out.println("Error al probar UsuarioDAO.");
            e.printStackTrace();
        }
    }
}