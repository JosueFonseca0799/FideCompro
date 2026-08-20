package com.mycompany.fidecompro.basededatos;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author j.fonseca
 */

public class InicializarBD {

    public static void main(String[] args) {

        String sql = "INSERT INTO USUARIOS "
                   + "(ID, NOMBRE, USERNAME, PASSWORD, ROL, ACTIVO) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, "U1");
            sentencia.setString(2, "Administrador");
            sentencia.setString(3, "admin");
            sentencia.setString(4, "1234");
            sentencia.setString(5, "ADMIN");
            sentencia.setBoolean(6, true);

            sentencia.executeUpdate();

            System.out.println("Usuario administrador creado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al inicializar la base de datos.");
            e.printStackTrace();
        }
    }
}