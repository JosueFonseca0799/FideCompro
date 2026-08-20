package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author j.fonseca
 */

public class UsuarioDAO {
    
        public Usuario autenticar(String username, String password) throws SQLException {

        String sql = "SELECT ID, NOMBRE, USERNAME, PASSWORD, ROL, ACTIVO "
                   + "FROM USUARIOS "
                   + "WHERE USERNAME = ? AND PASSWORD = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, username);
            sentencia.setString(2, password);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    return new Usuario(
                        resultado.getString("ID"),
                        resultado.getString("NOMBRE"),
                        resultado.getString("USERNAME"),
                        resultado.getString("PASSWORD"),
                        resultado.getString("ROL"),
                        resultado.getBoolean("ACTIVO")
                    );
                }
            }
        }

        return null;
    }
}