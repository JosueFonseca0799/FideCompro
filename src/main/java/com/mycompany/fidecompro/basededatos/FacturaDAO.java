package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author j.fonseca
 */

public class FacturaDAO {
    
    public void insertar(Factura factura) throws SQLException {

        String sql = "INSERT INTO FACTURAS "
                   + "(NUMERO_FACTURA, FECHA, CLIENTE_ID, USUARIO_ID, ESTADO) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, factura.getNumeroFactura());

            sentencia.setTimestamp(
                2,
                java.sql.Timestamp.valueOf(factura.getFecha())
            );

            sentencia.setString(
                3,
                factura.getCliente().getId()
            );

            sentencia.setString(
                4,
                factura.getUsuario().getId()
            );

            sentencia.setString(
                5,
                factura.getEstado()
            );

            sentencia.executeUpdate();
        }
    }

    public void insertarLinea(
            String numeroFactura,
            String codigoProducto,
            int cantidad,
            double precioUnitario,
            double impuesto) throws SQLException {

        String sql = "INSERT INTO LINEAS_FACTURA "
                   + "(FACTURA_ID, PRODUCTO_CODIGO, CANTIDAD, "
                   + "PRECIO_UNITARIO, IMPUESTO) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, numeroFactura);
            sentencia.setString(2, codigoProducto);
            sentencia.setInt(3, cantidad);

            sentencia.setBigDecimal(
                4,
                java.math.BigDecimal.valueOf(precioUnitario)
            );

            sentencia.setBigDecimal(
                5,
                java.math.BigDecimal.valueOf(impuesto)
            );

            sentencia.executeUpdate();
        }
    }

    public boolean existe(String numeroFactura) throws SQLException {

        String sql = "SELECT NUMERO_FACTURA "
                   + "FROM FACTURAS "
                   + "WHERE NUMERO_FACTURA = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, numeroFactura);

            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        }
    }

    public void actualizarEstado(
            String numeroFactura,
            String estado) throws SQLException {

        String sql = "UPDATE FACTURAS "
                   + "SET ESTADO = ? "
                   + "WHERE NUMERO_FACTURA = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, estado);
            sentencia.setString(2, numeroFactura);

            sentencia.executeUpdate();
        }
    }
}