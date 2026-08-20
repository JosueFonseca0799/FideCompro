package com.mycompany.fidecompro.basededatos;

import java.sql.Connection;
import java.sql.Statement;
/**
 *
 * @author j.fonseca
 */

public class CrearTablas {

    public static void main(String[] args) {

        try (Connection conexion = ConexionBD.obtenerConexion();
             Statement sentencia = conexion.createStatement()) {

            // Tabla USUARIOS
            sentencia.executeUpdate(
                "CREATE TABLE USUARIOS (" +
                "ID VARCHAR(20) PRIMARY KEY, " +
                "NOMBRE VARCHAR(100) NOT NULL, " +
                "USERNAME VARCHAR(50) NOT NULL UNIQUE, " +
                "PASSWORD VARCHAR(100) NOT NULL, " +
                "ROL VARCHAR(30) NOT NULL, " +
                "ACTIVO BOOLEAN NOT NULL" +
                ")"
            );

            // Tabla CLIENTES
            sentencia.executeUpdate(
                "CREATE TABLE CLIENTES (" +
                "ID VARCHAR(20) PRIMARY KEY, " +
                "NOMBRE VARCHAR(100) NOT NULL, " +
                "CEDULA VARCHAR(30) NOT NULL UNIQUE, " +
                "TELEFONO VARCHAR(30), " +
                "EMAIL VARCHAR(100), " +
                "DIRECCION VARCHAR(200)" +
                ")"
            );

            // Tabla PRODUCTOS
            sentencia.executeUpdate(
                "CREATE TABLE PRODUCTOS (" +
                "CODIGO VARCHAR(20) PRIMARY KEY, " +
                "NOMBRE VARCHAR(100) NOT NULL, " +
                "PRECIO_UNITARIO DECIMAL(12,2) NOT NULL, " +
                "STOCK INTEGER NOT NULL, " +
                "TIPO VARCHAR(20) NOT NULL, " +
                "PORCENTAJE_IMPUESTO DECIMAL(5,2) NOT NULL" +
                ")"
            );

            // Tabla FACTURAS
            sentencia.executeUpdate(
                "CREATE TABLE FACTURAS (" +
                "NUMERO_FACTURA VARCHAR(30) PRIMARY KEY, " +
                "FECHA TIMESTAMP NOT NULL, " +
                "CLIENTE_ID VARCHAR(20) NOT NULL, " +
                "USUARIO_ID VARCHAR(20) NOT NULL, " +
                "ESTADO VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (CLIENTE_ID) REFERENCES CLIENTES(ID), " +
                "FOREIGN KEY (USUARIO_ID) REFERENCES USUARIOS(ID)" +
                ")"
            );

            // Tabla LINEAS_FACTURA
            sentencia.executeUpdate(
                "CREATE TABLE LINEAS_FACTURA (" +
                "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "FACTURA_ID VARCHAR(30) NOT NULL, " +
                "PRODUCTO_CODIGO VARCHAR(20) NOT NULL, " +
                "CANTIDAD INTEGER NOT NULL, " +
                "PRECIO_UNITARIO DECIMAL(12,2) NOT NULL, " +
                "IMPUESTO DECIMAL(12,2) NOT NULL, " +
                "FOREIGN KEY (FACTURA_ID) REFERENCES FACTURAS(NUMERO_FACTURA), " +
                "FOREIGN KEY (PRODUCTO_CODIGO) REFERENCES PRODUCTOS(CODIGO)" +
                ")"
            );

            System.out.println("Tablas creadas correctamente.");

        } catch (Exception e) {
            System.out.println("Error al crear las tablas.");
            e.printStackTrace();
        }
    }
}