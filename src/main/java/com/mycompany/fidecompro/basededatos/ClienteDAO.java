package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author j.fonseca
 */

public class ClienteDAO {

    public void insertar(Cliente cliente) throws SQLException {

        String sql = "INSERT INTO CLIENTES "
                   + "(ID, NOMBRE, CEDULA, TELEFONO, EMAIL, DIRECCION) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, cliente.getId());
            sentencia.setString(2, cliente.getNombre());
            sentencia.setString(3, cliente.getCedula());
            sentencia.setString(4, cliente.getTelefono());
            sentencia.setString(5, cliente.getEmail());
            sentencia.setString(6, cliente.getDireccion());

            sentencia.executeUpdate();
        }
    }

    public Cliente buscarPorCedula(String cedula) throws SQLException {

        String sql = "SELECT ID, NOMBRE, CEDULA, TELEFONO, EMAIL, DIRECCION "
                   + "FROM CLIENTES "
                   + "WHERE CEDULA = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, cedula);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    return new Cliente(
                        resultado.getString("ID"),
                        resultado.getString("NOMBRE"),
                        resultado.getString("CEDULA"),
                        resultado.getString("TELEFONO"),
                        resultado.getString("EMAIL"),
                        resultado.getString("DIRECCION")
                    );
                }
            }
        }

        return null;
    }
}