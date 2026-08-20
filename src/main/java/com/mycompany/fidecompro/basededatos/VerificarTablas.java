package com.mycompany.fidecompro.basededatos;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 *
 * @author j.fonseca
 */

public class VerificarTablas {

    public static void main(String[] args) {

        String[] tablas = {
            "USUARIOS",
            "CLIENTES",
            "PRODUCTOS",
            "FACTURAS",
            "LINEAS_FACTURA"
        };

        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement sentencia = conexion.createStatement()) {

            for (String tabla : tablas) {

                String sql = "SELECT * FROM " + tabla;

                try (ResultSet resultado = sentencia.executeQuery(sql)) {

                    ResultSetMetaData metadata = resultado.getMetaData();

                    System.out.println("Tabla: " + tabla);
                    System.out.println("Columnas: " + metadata.getColumnCount());

                    for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        System.out.println("  - " + metadata.getColumnName(i));
                    }

                    System.out.println();
                }
            }

            System.out.println("Verificacion de tablas completada.");

        } catch (Exception e) {
            System.out.println("Error al verificar las tablas.");
            e.printStackTrace();
        }
    }
}