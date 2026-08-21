package com.mycompany.fidecompro.cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author j.fonseca
 */

public class ClienteSocket {
    
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   PRUEBA DE FACTURACION - FIDECOMPRO");
        System.out.println("========================================");

        try (
                Socket socket = new Socket(HOST, PUERTO); Scanner entrada = new Scanner(socket.getInputStream()); PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(),
                        true
                )) {

            // 1. CONEXIÓN
            System.out.println(
                    "Servidor: " + entrada.nextLine()
            );

            // 2. LOGIN
            salida.println("LOGIN|admin|1234");

            System.out.println(
                    "LOGIN: " + entrada.nextLine()
            );

            // 3. CONSULTAR PRODUCTO
            salida.println("BUSCAR_PRODUCTO|P003");

            System.out.println(
                    "PRODUCTO ANTES: " + entrada.nextLine()
            );

            // 4. CREAR FACTURA
            // Usamos FAC-003 para evitar conflictos
            // con pruebas anteriores.
            salida.println(
                    "CREAR_FACTURA|FAC-003|C2|U1"
            );

            System.out.println(
                    "FACTURA: " + entrada.nextLine()
            );

            // 5. AGREGAR LINEA
            // Producto P003 - cantidad 2
            salida.println(
                    "AGREGAR_LINEA|FAC-003|P003|2"
            );

            System.out.println(
                    "LINEA: " + entrada.nextLine()
            );

            // 6. EMITIR FACTURA
            salida.println(
                    "EMITIR_FACTURA|FAC-003|P003|2"
            );

            System.out.println(
                    "EMISION: " + entrada.nextLine()
            );

            // 7. CONSULTAR PRODUCTO DESPUES
            salida.println("BUSCAR_PRODUCTO|P003");

            System.out.println(
                    "PRODUCTO DESPUES: " + entrada.nextLine()
            );

            // 8. CERRAR CONEXION
            salida.println("SALIR");

            System.out.println(
                    "Servidor: " + entrada.nextLine()
            );

            System.out.println("========================================");
            System.out.println("       PRUEBA FINALIZADA");
            System.out.println("========================================");

        } catch (IOException e) {

            System.out.println(
                    "ERROR DE CONEXION: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}
