package com.mycompany.fidecompro.servidor;

import com.mycompany.fidecompro.Usuario;
import com.mycompany.fidecompro.basededatos.UsuarioDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author j.fonseca
 */

public class ClienteHandler extends Thread {

    private final Socket cliente;

    public ClienteHandler(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {

        String direccion = cliente.getInetAddress().getHostAddress();

        System.out.println(
            "Atendiendo cliente en hilo: "
            + Thread.currentThread().getName()
            + " - "
            + direccion
        );

        try (
            Scanner entrada = new Scanner(cliente.getInputStream());
            PrintWriter salida = new PrintWriter(
                cliente.getOutputStream(),
                true
            )
        ) {

            // Confirmar que la conexión fue establecida
            salida.println("CONEXION_OK");

            while (entrada.hasNextLine()) {

                String mensaje = entrada.nextLine();

                System.out.println(
                    "Mensaje recibido de "
                    + direccion
                    + ": "
                    + mensaje
                );

                // Cerrar conexión
                if ("SALIR".equalsIgnoreCase(mensaje)) {

                    salida.println("CONEXION_CERRADA");
                    break;
                }

                // Procesar LOGIN
                if (mensaje.startsWith("LOGIN|")) {

                    procesarLogin(mensaje, salida);

                } else {

                    salida.println("ERROR|OPERACION_NO_RECONOCIDA");
                }
            }

        } catch (IOException e) {

            System.out.println(
                "Error atendiendo cliente "
                + direccion
                + ": "
                + e.getMessage()
            );

        } finally {

            try {
                cliente.close();
            } catch (IOException e) {
                System.out.println(
                    "No se pudo cerrar el cliente."
                );
            }

            System.out.println(
                "Cliente desconectado: "
                + direccion
            );
        }
    }

    private void procesarLogin(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|");

            // Formato esperado:
            // LOGIN|usuario|password

            if (datos.length != 3) {

                salida.println(
                    "LOGIN_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String username = datos[1];
            String password = datos[2];

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            Usuario usuario =
                    usuarioDAO.autenticar(
                        username,
                        password
                    );

            if (usuario != null) {

                salida.println(
                    "LOGIN_OK|"
                    + usuario.getId()
                    + "|"
                    + usuario.getNombre()
                    + "|"
                    + usuario.getRol()
                );

                System.out.println(
                    "Login exitoso para usuario: "
                    + username
                );

            } else {

                salida.println(
                    "LOGIN_ERROR|CREDENCIALES_INVALIDAS"
                );

                System.out.println(
                    "Login rechazado para usuario: "
                    + username
                );
            }

        } catch (Exception e) {

            salida.println(
                "LOGIN_ERROR|ERROR_SERVIDOR"
            );

            System.out.println(
                "Error procesando login: "
                + e.getMessage()
            );
        }
    }
}