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

        System.out.println("Conectando al servidor FideCompro...");

        try (
            Socket socket = new Socket(HOST, PUERTO);
            Scanner entrada = new Scanner(socket.getInputStream());
            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(),
                true
            )
        ) {

            System.out.println("Conexión establecida.");

            // Respuesta inicial del servidor
            String respuesta = entrada.nextLine();

            System.out.println(
                "Servidor respondió: " + respuesta
            );

            // LOGIN
            salida.println("LOGIN|admin|1234");

            respuesta = entrada.nextLine();

            System.out.println(
                "Respuesta LOGIN: " + respuesta
            );

            // Cerrar conexión
            salida.println("SALIR");

            respuesta = entrada.nextLine();

            System.out.println(
                "Servidor respondió: " + respuesta
            );

        } catch (IOException e) {

            System.out.println(
                "No se pudo conectar con el servidor."
            );

            e.printStackTrace();
        }
    }
}