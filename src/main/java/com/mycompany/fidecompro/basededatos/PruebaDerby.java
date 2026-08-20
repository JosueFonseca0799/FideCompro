package com.mycompany.fidecompro.basededatos;

import java.sql.Connection;

public class PruebaDerby {

    public static void main(String[] args) {

        try (Connection conexion = ConexionBD.obtenerConexion()) {

            System.out.println("Conexión mediante ConexionBD exitosa.");
            System.out.println("Base de datos: fidecomproDB");

        } catch (Exception e) {

            System.out.println("Error al conectar mediante ConexionBD.");
            e.printStackTrace();
        }
    }
}