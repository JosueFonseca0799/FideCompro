package com.mycompany.fidecompro.basededatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author j.fonseca
 */
public class ConexionBD {

    private static final String URL = "jdbc:derby:fidecomproDB;create=true";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}