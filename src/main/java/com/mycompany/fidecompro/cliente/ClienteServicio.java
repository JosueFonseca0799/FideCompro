package com.mycompany.fidecompro.cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author j.fonseca
 */

public class ClienteServicio {
    
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    private String enviar(String mensaje) throws IOException {

        try (
            Socket socket = new Socket(HOST, PUERTO);
            Scanner entrada = new Scanner(socket.getInputStream());
            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(),
                true
            )
        ) {

            entrada.nextLine(); // CONEXION_OK

            salida.println(mensaje);

            String respuesta = entrada.nextLine();

            salida.println("SALIR");

            if (entrada.hasNextLine()) {
                entrada.nextLine();
            }

            return respuesta;
        }
    }

    public String login(String username, String password)
            throws IOException {

        return enviar(
            "LOGIN|" + username + "|" + password
        );
    }

    public String registrarCliente(
            String id,
            String nombre,
            String cedula,
            String telefono,
            String email,
            String direccion) throws IOException {

        return enviar(
            "REGISTRAR_CLIENTE|"
            + id + "|"
            + nombre + "|"
            + cedula + "|"
            + telefono + "|"
            + email + "|"
            + direccion
        );
    }

    public String buscarCliente(String cedula)
            throws IOException {

        return enviar(
            "BUSCAR_CLIENTE|" + cedula
        );
    }

    public String registrarProducto(
            String codigo,
            String nombre,
            double precio,
            int stock,
            String tipo,
            double impuesto) throws IOException {

        return enviar(
            "REGISTRAR_PRODUCTO|"
            + codigo + "|"
            + nombre + "|"
            + precio + "|"
            + stock + "|"
            + tipo + "|"
            + impuesto
        );
    }

    public String buscarProducto(String codigo)
            throws IOException {

        return enviar(
            "BUSCAR_PRODUCTO|" + codigo
        );
    }

    public String crearFactura(
            String numeroFactura,
            String clienteId,
            String usuarioId) throws IOException {

        return enviar(
            "CREAR_FACTURA|"
            + numeroFactura + "|"
            + clienteId + "|"
            + usuarioId
        );
    }

    public String agregarLinea(
            String numeroFactura,
            String codigoProducto,
            int cantidad) throws IOException {

        return enviar(
            "AGREGAR_LINEA|"
            + numeroFactura + "|"
            + codigoProducto + "|"
            + cantidad
        );
    }

    public String emitirFactura(
            String numeroFactura,
            String codigoProducto,
            int cantidad) throws IOException {

        return enviar(
            "EMITIR_FACTURA|"
            + numeroFactura + "|"
            + codigoProducto + "|"
            + cantidad
        );
    }
}