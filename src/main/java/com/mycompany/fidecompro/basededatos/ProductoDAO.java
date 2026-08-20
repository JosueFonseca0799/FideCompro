package com.mycompany.fidecompro.basededatos;

import com.mycompany.fidecompro.Producto;
import com.mycompany.fidecompro.ProductoExento;
import com.mycompany.fidecompro.ProductoGravado;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author j.fonseca
 */

public class ProductoDAO {
    
    public void insertar(Producto producto) throws SQLException {

        String sql = "INSERT INTO PRODUCTOS "
                   + "(CODIGO, NOMBRE, PRECIO_UNITARIO, STOCK, TIPO, PORCENTAJE_IMPUESTO) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, producto.getCodigo());
            sentencia.setString(2, producto.getNombre());
            sentencia.setBigDecimal(3, BigDecimal.valueOf(producto.getPrecioUnitario()));
            sentencia.setInt(4, producto.getStock());

            if (producto instanceof ProductoGravado) {
                sentencia.setString(5, "GRAVADO");
                sentencia.setBigDecimal(
                    6,
                    BigDecimal.valueOf(((ProductoGravado) producto).getPorcentajeImpuesto())
                );
            } else {
                sentencia.setString(5, "EXENTO");
                sentencia.setBigDecimal(6, BigDecimal.ZERO);
            }

            sentencia.executeUpdate();
        }
    }

    public Producto buscarPorCodigo(String codigo) throws SQLException {

        String sql = "SELECT CODIGO, NOMBRE, PRECIO_UNITARIO, STOCK, "
                   + "TIPO, PORCENTAJE_IMPUESTO "
                   + "FROM PRODUCTOS "
                   + "WHERE CODIGO = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {

                    String tipo = resultado.getString("TIPO");

                    String codigoProducto = resultado.getString("CODIGO");
                    String nombre = resultado.getString("NOMBRE");
                    double precio = resultado.getBigDecimal("PRECIO_UNITARIO").doubleValue();
                    int stock = resultado.getInt("STOCK");

                    if ("GRAVADO".equals(tipo)) {

                        double porcentaje = resultado
                                .getBigDecimal("PORCENTAJE_IMPUESTO")
                                .doubleValue();

                        return new ProductoGravado(
                            codigoProducto,
                            nombre,
                            precio,
                            stock,
                            porcentaje
                        );

                    } else {

                        return new ProductoExento(
                            codigoProducto,
                            nombre,
                            precio,
                            stock
                        );
                    }
                }
            }
        }

        return null;
    }
}