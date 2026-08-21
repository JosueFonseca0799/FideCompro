package com.mycompany.fidecompro.servidor;

import com.mycompany.fidecompro.Cliente;
import com.mycompany.fidecompro.Factura;
import com.mycompany.fidecompro.LineaFactura;
import com.mycompany.fidecompro.Producto;
import com.mycompany.fidecompro.Usuario;
import com.mycompany.fidecompro.basededatos.ClienteDAO;
import com.mycompany.fidecompro.basededatos.FacturaDAO;
import com.mycompany.fidecompro.basededatos.ProductoDAO;
import com.mycompany.fidecompro.basededatos.UsuarioDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
                Scanner entrada = new Scanner(cliente.getInputStream()); PrintWriter salida = new PrintWriter(
                        cliente.getOutputStream(),
                        true
                )) {

            salida.println("CONEXION_OK");

            while (entrada.hasNextLine()) {

                String mensaje = entrada.nextLine();

                System.out.println(
                        "Mensaje recibido de "
                        + direccion
                        + ": "
                        + mensaje
                );

                if ("SALIR".equalsIgnoreCase(mensaje)) {

                    salida.println("CONEXION_CERRADA");
                    break;
                }

                if (mensaje.startsWith("LOGIN|")) {

                    procesarLogin(mensaje, salida);

                } else if (mensaje.startsWith("REGISTRAR_CLIENTE|")) {

                    procesarRegistrarCliente(mensaje, salida);

                } else if (mensaje.startsWith("BUSCAR_CLIENTE|")) {

                    procesarBuscarCliente(mensaje, salida);

                } else if (mensaje.startsWith("REGISTRAR_PRODUCTO|")) {

                    procesarRegistrarProducto(mensaje, salida);

                } else if (mensaje.startsWith("BUSCAR_PRODUCTO|")) {

                    procesarBuscarProducto(mensaje, salida);

                } else if (mensaje.startsWith("CREAR_FACTURA|")) {

                    procesarCrearFactura(mensaje, salida);

                } else if (mensaje.startsWith("AGREGAR_LINEA|")) {

                    procesarAgregarLinea(mensaje, salida);

                } else if (mensaje.startsWith("EMITIR_FACTURA|")) {

                    procesarEmitirFactura(mensaje, salida);

                } else {

                    salida.println(
                            "ERROR|OPERACION_NO_RECONOCIDA"
                    );
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

            if (datos.length != 3) {

                salida.println(
                        "LOGIN_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String username = datos[1];
            String password = datos[2];

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            Usuario usuario
                    = usuarioDAO.autenticar(
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

    private void procesarRegistrarCliente(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            if (datos.length != 7) {

                salida.println(
                        "CLIENTE_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            Cliente cliente = new Cliente(
                    datos[1],
                    datos[2],
                    datos[3],
                    datos[4],
                    datos[5],
                    datos[6]
            );

            ClienteDAO clienteDAO = new ClienteDAO();

            clienteDAO.insertar(cliente);

            salida.println(
                    "CLIENTE_REGISTRADO|"
                    + cliente.getId()
                    + "|"
                    + cliente.getNombre()
            );

            System.out.println(
                    "Cliente registrado: "
                    + cliente.getCedula()
            );

        } catch (Exception e) {

            salida.println(
                    "CLIENTE_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error registrando cliente: "
                    + e.getMessage()
            );
        }
    }

    private void procesarBuscarCliente(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            if (datos.length != 2) {

                salida.println(
                        "CLIENTE_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String cedula = datos[1];

            ClienteDAO clienteDAO = new ClienteDAO();

            Cliente cliente
                    = clienteDAO.buscarPorCedula(cedula);

            if (cliente != null) {

                salida.println(
                        "CLIENTE_ENCONTRADO|"
                        + cliente.getId()
                        + "|"
                        + cliente.getNombre()
                        + "|"
                        + cliente.getCedula()
                        + "|"
                        + cliente.getTelefono()
                        + "|"
                        + cliente.getEmail()
                        + "|"
                        + cliente.getDireccion()
                );

                System.out.println(
                        "Cliente encontrado: "
                        + cedula
                );

            } else {

                salida.println(
                        "CLIENTE_NO_ENCONTRADO"
                );

                System.out.println(
                        "Cliente no encontrado: "
                        + cedula
                );
            }

        } catch (Exception e) {

            salida.println(
                    "CLIENTE_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error buscando cliente: "
                    + e.getMessage()
            );
        }
    }

    private void procesarRegistrarProducto(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            if (datos.length != 7) {

                salida.println(
                        "PRODUCTO_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String codigo = datos[1];
            String nombre = datos[2];
            double precio = Double.parseDouble(datos[3]);
            int stock = Integer.parseInt(datos[4]);
            String tipo = datos[5];
            double impuesto = Double.parseDouble(datos[6]);

            Producto producto;

            if ("GRAVADO".equalsIgnoreCase(tipo)) {

                producto
                        = new com.mycompany.fidecompro.ProductoGravado(
                                codigo,
                                nombre,
                                precio,
                                stock,
                                impuesto
                        );

            } else if ("EXENTO".equalsIgnoreCase(tipo)) {

                producto
                        = new com.mycompany.fidecompro.ProductoExento(
                                codigo,
                                nombre,
                                precio,
                                stock
                        );

            } else {

                salida.println(
                        "PRODUCTO_ERROR|TIPO_INVALIDO"
                );

                return;
            }

            ProductoDAO productoDAO = new ProductoDAO();

            productoDAO.insertar(producto);

            salida.println(
                    "PRODUCTO_REGISTRADO|"
                    + producto.getCodigo()
                    + "|"
                    + producto.getNombre()
            );

            System.out.println(
                    "Producto registrado: "
                    + producto.getCodigo()
            );

        } catch (Exception e) {

            salida.println(
                    "PRODUCTO_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error registrando producto: "
                    + e.getMessage()
            );
        }
    }

    private void procesarBuscarProducto(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            if (datos.length != 2) {

                salida.println(
                        "PRODUCTO_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String codigo = datos[1];

            ProductoDAO productoDAO = new ProductoDAO();

            Producto producto
                    = productoDAO.buscarPorCodigo(codigo);

            if (producto != null) {

                String tipo;

                if (producto instanceof com.mycompany.fidecompro.ProductoGravado) {

                    tipo = "GRAVADO";

                } else {

                    tipo = "EXENTO";
                }

                salida.println(
                        "PRODUCTO_ENCONTRADO|"
                        + producto.getCodigo()
                        + "|"
                        + producto.getNombre()
                        + "|"
                        + producto.getPrecioUnitario()
                        + "|"
                        + producto.getStock()
                        + "|"
                        + tipo
                );

                System.out.println(
                        "Producto encontrado: "
                        + codigo
                );

            } else {

                salida.println(
                        "PRODUCTO_NO_ENCONTRADO"
                );

                System.out.println(
                        "Producto no encontrado: "
                        + codigo
                );
            }

        } catch (Exception e) {

            salida.println(
                    "PRODUCTO_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error buscando producto: "
                    + e.getMessage()
            );
        }
    }

    private void procesarCrearFactura(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            /*
             * Formato:
             *
             * CREAR_FACTURA|NUMERO_FACTURA|CLIENTE_ID|USUARIO_ID
             */
            if (datos.length != 4) {

                salida.println(
                        "FACTURA_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String numeroFactura = datos[1];
            String clienteId = datos[2];
            String usuarioId = datos[3];

            ClienteDAO clienteDAO = new ClienteDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            Cliente cliente
                    = clienteDAO.buscarPorId(clienteId);

            Usuario usuario
                    = usuarioDAO.buscarPorId(usuarioId);

            if (cliente == null) {

                salida.println(
                        "FACTURA_ERROR|CLIENTE_NO_ENCONTRADO"
                );

                return;
            }

            if (usuario == null) {

                salida.println(
                        "FACTURA_ERROR|USUARIO_NO_ENCONTRADO"
                );

                return;
            }

            FacturaDAO facturaDAO = new FacturaDAO();

            if (facturaDAO.existe(numeroFactura)) {

                salida.println(
                        "FACTURA_ERROR|YA_EXISTE"
                );

                return;
            }

            Factura factura
                    = new Factura(
                            numeroFactura,
                            cliente,
                            usuario
                    );

            facturaDAO.insertar(factura);

            salida.println(
                    "FACTURA_CREADA|"
                    + factura.getNumeroFactura()
                    + "|"
                    + factura.getEstado()
            );

            System.out.println(
                    "Factura creada: "
                    + numeroFactura
            );

        } catch (Exception e) {

            salida.println(
                    "FACTURA_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error creando factura: "
                    + e.getMessage()
            );
        }
    }

    private void procesarAgregarLinea(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            /*
             * Formato:
             *
             * AGREGAR_LINEA|NUMERO_FACTURA|CODIGO_PRODUCTO|CANTIDAD
             */
            if (datos.length != 4) {

                salida.println(
                        "FACTURA_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String numeroFactura = datos[1];
            String codigoProducto = datos[2];
            int cantidad = Integer.parseInt(datos[3]);

            if (cantidad <= 0) {

                salida.println(
                        "FACTURA_ERROR|CANTIDAD_INVALIDA"
                );

                return;
            }

            FacturaDAO facturaDAO = new FacturaDAO();
            ProductoDAO productoDAO = new ProductoDAO();

            if (!facturaDAO.existe(numeroFactura)) {

                salida.println(
                        "FACTURA_ERROR|FACTURA_NO_EXISTE"
                );

                return;
            }

            Producto producto
                    = productoDAO.buscarPorCodigo(codigoProducto);

            if (producto == null) {

                salida.println(
                        "FACTURA_ERROR|PRODUCTO_NO_ENCONTRADO"
                );

                return;
            }

            if (producto.getStock() < cantidad) {

                salida.println(
                        "FACTURA_ERROR|STOCK_INSUFICIENTE"
                );

                return;
            }

            double precio
                    = producto.getPrecioUnitario();

            double impuesto
                    = producto.calcularImpuesto(1);

            facturaDAO.insertarLinea(
                    numeroFactura,
                    codigoProducto,
                    cantidad,
                    precio,
                    impuesto
            );

            salida.println(
                    "LINEA_AGREGADA|"
                    + codigoProducto
                    + "|"
                    + cantidad
            );

            System.out.println(
                    "Línea agregada a factura "
                    + numeroFactura
            );

        } catch (Exception e) {

            salida.println(
                    "FACTURA_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error agregando línea: "
                    + e.getMessage()
            );
        }
    }

    private void procesarEmitirFactura(
            String mensaje,
            PrintWriter salida) {

        try {

            String[] datos = mensaje.split("\\|", -1);

            /*
             * Formato:
             *
             * EMITIR_FACTURA|NUMERO_FACTURA|CODIGO_PRODUCTO|CANTIDAD
             *
             * Para esta primera versión la emisión se realiza
             * sobre la línea que acabamos de agregar.
             */
            if (datos.length != 4) {

                salida.println(
                        "FACTURA_ERROR|FORMATO_INVALIDO"
                );

                return;
            }

            String numeroFactura = datos[1];
            String codigoProducto = datos[2];
            int cantidad = Integer.parseInt(datos[3]);

            ProductoDAO productoDAO = new ProductoDAO();
            FacturaDAO facturaDAO = new FacturaDAO();

            Producto producto
                    = productoDAO.buscarPorCodigo(codigoProducto);

            if (producto == null) {

                salida.println(
                        "FACTURA_ERROR|PRODUCTO_NO_ENCONTRADO"
                );

                return;
            }

            if (producto.getStock() < cantidad) {

                salida.println(
                        "FACTURA_ERROR|STOCK_INSUFICIENTE"
                );

                return;
            }

            productoDAO.actualizarStock(
                    codigoProducto,
                    cantidad
            );

            facturaDAO.actualizarEstado(
                    numeroFactura,
                    "EMITIDA"
            );

            salida.println(
                    "FACTURA_EMITIDA|"
                    + numeroFactura
                    + "|STOCK_RESTANTE|"
                    + (producto.getStock() - cantidad)
            );

            System.out.println(
                    "Factura emitida: "
                    + numeroFactura
                    + " | Stock descontado: "
                    + cantidad
            );

        } catch (Exception e) {

            salida.println(
                    "FACTURA_ERROR|"
                    + e.getMessage()
            );

            System.out.println(
                    "Error emitiendo factura: "
                    + e.getMessage()
            );
        }
    }
}