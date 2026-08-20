package com.mycompany.fidecompro.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author j.fonseca
 */

public class ServidorFideCompro {
    
    private static final int PUERTO = 5000;

    public static void main(String[] args) {

        System.out.println("Iniciando servidor FideCompro...");

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {

            System.out.println("Servidor iniciado correctamente.");
            System.out.println("Esperando conexiones en el puerto " + PUERTO + "...");

            while (true) {

                Socket cliente = servidor.accept();

                System.out.println(
                    "Cliente conectado: "
                    + cliente.getInetAddress().getHostAddress()
                );

                ClienteHandler hilo = new ClienteHandler(cliente);

                hilo.start();
            }

        } catch (IOException e) {

            System.out.println("Error en el servidor.");
            e.printStackTrace();
        }
    }
}