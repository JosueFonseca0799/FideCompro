package com.mycompany.fidecompro;

import com.mycompany.fidecompro.cliente.ClienteServicio;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
/**
 *
 * @author josue
 */

public class VentanaPrincipal extends JFrame {

    private SistemaTienda sistema;
    private Usuario usuarioActual;
    private ClienteServicio servicio;

    private JTextArea areaTexto;

    public VentanaPrincipal(
            SistemaTienda sistema,
            Usuario usuarioActual) {

        this.sistema = sistema;
        this.usuarioActual = usuarioActual;
        this.servicio = new ClienteServicio();

        setTitle("FideCompro - Menú Principal");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelBotones =
            new JPanel(new GridLayout(2, 3, 10, 10));

        JButton btnRegistrarCliente =
            new JButton("Registrar cliente");

        JButton btnBuscarCliente =
            new JButton("Buscar cliente");

        JButton btnRegistrarProducto =
            new JButton("Registrar producto");

        JButton btnBuscarProducto =
            new JButton("Buscar producto");

        JButton btnCrearFactura =
            new JButton("Crear factura");

        JButton btnSalir =
            new JButton("Salir");

        panelBotones.add(btnRegistrarCliente);
        panelBotones.add(btnBuscarCliente);
        panelBotones.add(btnRegistrarProducto);
        panelBotones.add(btnBuscarProducto);
        panelBotones.add(btnCrearFactura);
        panelBotones.add(btnSalir);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        add(panelBotones, BorderLayout.NORTH);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        btnRegistrarCliente.addActionListener(
            e -> registrarCliente()
        );

        btnBuscarCliente.addActionListener(
            e -> buscarCliente()
        );

        btnRegistrarProducto.addActionListener(
            e -> registrarProducto()
        );

        btnBuscarProducto.addActionListener(
            e -> buscarProducto()
        );

        btnCrearFactura.addActionListener(
            e -> crearFactura()
        );

        btnSalir.addActionListener(
            e -> salir()
        );

        areaTexto.setText(
            "FideCompro conectado.\n\n"
            + "Usuario: "
            + usuarioActual.getNombre()
            + "\n"
            + "Rol: "
            + usuarioActual.getRol()
            + "\n\n"
            + "Seleccione una operación."
        );
    }

    private void registrarCliente() {

        String nombre =
            JOptionPane.showInputDialog(
                this,
                "Nombre del cliente:"
            );

        if (nombre == null || nombre.isBlank()) {
            return;
        }

        String cedula =
            JOptionPane.showInputDialog(
                this,
                "Cédula:"
            );

        if (cedula == null || cedula.isBlank()) {
            return;
        }

        String telefono =
            JOptionPane.showInputDialog(
                this,
                "Teléfono:"
            );

        String email =
            JOptionPane.showInputDialog(
                this,
                "Email:"
            );

        String direccion =
            JOptionPane.showInputDialog(
                this,
                "Dirección:"
            );

        String id = "C" + System.currentTimeMillis();

        try {

            String respuesta =
                servicio.registrarCliente(
                    id,
                    nombre,
                    cedula,
                    telefono == null ? "" : telefono,
                    email == null ? "" : email,
                    direccion == null ? "" : direccion
                );

            areaTexto.setText(
                "=== REGISTRO DE CLIENTE ===\n\n"
                + respuesta
            );

            if (respuesta.startsWith(
                    "CLIENTE_REGISTRADO|")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente."
                );
            } else {

                JOptionPane.showMessageDialog(
                    this,
                    respuesta,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IOException e) {

            mostrarErrorConexion(e);
        }
    }

    private void buscarCliente() {

        String cedula =
            JOptionPane.showInputDialog(
                this,
                "Ingrese la cédula:"
            );

        if (cedula == null || cedula.isBlank()) {
            return;
        }

        try {

            String respuesta =
                servicio.buscarCliente(cedula);

            areaTexto.setText(
                "=== BÚSQUEDA DE CLIENTE ===\n\n"
                + respuesta
            );

            if (respuesta.startsWith(
                    "CLIENTE_ENCONTRADO|")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cliente encontrado correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Cliente no encontrado."
                );
            }

        } catch (IOException e) {

            mostrarErrorConexion(e);
        }
    }

    private void registrarProducto() {

        String codigo =
            JOptionPane.showInputDialog(
                this,
                "Código del producto:"
            );

        if (codigo == null || codigo.isBlank()) {
            return;
        }

        String nombre =
            JOptionPane.showInputDialog(
                this,
                "Nombre del producto:"
            );

        if (nombre == null || nombre.isBlank()) {
            return;
        }

        String precioTexto =
            JOptionPane.showInputDialog(
                this,
                "Precio unitario:"
            );

        String stockTexto =
            JOptionPane.showInputDialog(
                this,
                "Stock inicial:"
            );

        try {

            double precio =
                Double.parseDouble(precioTexto);

            int stock =
                Integer.parseInt(stockTexto);

            String[] opciones = {
                "Gravado",
                "Exento"
            };

            int tipo =
                JOptionPane.showOptionDialog(
                    this,
                    "Seleccione el tipo:",
                    "Tipo de producto",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
                );

            if (tipo < 0) {
                return;
            }

            String tipoProducto;
            double impuesto;

            if (tipo == 0) {

                tipoProducto = "GRAVADO";

                String impuestoTexto =
                    JOptionPane.showInputDialog(
                        this,
                        "Porcentaje de impuesto.\n"
                        + "Ejemplo: 13"
                    );

                impuesto =
                    Double.parseDouble(impuestoTexto);

            } else {

                tipoProducto = "EXENTO";
                impuesto = 0;
            }

            String respuesta =
                servicio.registrarProducto(
                    codigo,
                    nombre,
                    precio,
                    stock,
                    tipoProducto,
                    impuesto
                );

            areaTexto.setText(
                "=== REGISTRO DE PRODUCTO ===\n\n"
                + respuesta
            );

            if (respuesta.startsWith(
                    "PRODUCTO_REGISTRADO|")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Producto registrado correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    respuesta,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Debe ingresar valores numéricos válidos.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IOException e) {

            mostrarErrorConexion(e);
        }
    }

    private void buscarProducto() {

        String codigo =
            JOptionPane.showInputDialog(
                this,
                "Código del producto:"
            );

        if (codigo == null || codigo.isBlank()) {
            return;
        }

        try {

            String respuesta =
                servicio.buscarProducto(codigo);

            areaTexto.setText(
                "=== BÚSQUEDA DE PRODUCTO ===\n\n"
                + respuesta
            );

            if (respuesta.startsWith(
                    "PRODUCTO_ENCONTRADO|")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Producto encontrado correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Producto no encontrado."
                );
            }

        } catch (IOException e) {

            mostrarErrorConexion(e);
        }
    }

    private void crearFactura() {

        String cedula =
            JOptionPane.showInputDialog(
                this,
                "Cédula del cliente:"
            );

        if (cedula == null || cedula.isBlank()) {
            return;
        }

        try {

            // Primero buscamos el cliente.
            String respuestaCliente =
                servicio.buscarCliente(cedula);

            if (!respuestaCliente.startsWith(
                    "CLIENTE_ENCONTRADO|")) {

                JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el cliente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String[] clienteDatos =
                respuestaCliente.split("\\|");

            String clienteId =
                clienteDatos[1];

            String numeroFactura =
                "FAC-" + System.currentTimeMillis();

            // Crear factura.
            String respuestaFactura =
                servicio.crearFactura(
                    numeroFactura,
                    clienteId,
                    usuarioActual.getId()
                );

            if (!respuestaFactura.startsWith(
                    "FACTURA_CREADA|")) {

                JOptionPane.showMessageDialog(
                    this,
                    respuestaFactura,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            areaTexto.setText(
                "=== FACTURA ===\n\n"
                + respuestaFactura
                + "\n"
            );

            boolean continuar = true;

            while (continuar) {

                String codigo =
                    JOptionPane.showInputDialog(
                        this,
                        "Código del producto:"
                    );

                if (codigo == null ||
                    codigo.isBlank()) {

                    break;
                }

                String cantidadTexto =
                    JOptionPane.showInputDialog(
                        this,
                        "Cantidad:"
                    );

                if (cantidadTexto == null ||
                    cantidadTexto.isBlank()) {

                    break;
                }

                int cantidad =
                    Integer.parseInt(cantidadTexto);

                String respuestaLinea =
                    servicio.agregarLinea(
                        numeroFactura,
                        codigo,
                        cantidad
                    );

                areaTexto.append(
                    "\n"
                    + respuestaLinea
                    + "\n"
                );

                if (!respuestaLinea.startsWith(
                        "LINEA_AGREGADA|")) {

                    JOptionPane.showMessageDialog(
                        this,
                        respuestaLinea,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                int otra =
                    JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea agregar otro producto?",
                        "Factura",
                        JOptionPane.YES_NO_OPTION
                    );

                continuar =
                    otra == JOptionPane.YES_OPTION;
            }

            String codigoEmitir =
                JOptionPane.showInputDialog(
                    this,
                    "Código del producto vendido:"
                );

            if (codigoEmitir == null ||
                codigoEmitir.isBlank()) {

                return;
            }

            String cantidadEmitirTexto =
                JOptionPane.showInputDialog(
                    this,
                    "Cantidad vendida:"
                );

            if (cantidadEmitirTexto == null ||
                cantidadEmitirTexto.isBlank()) {

                return;
            }

            int cantidadEmitir =
                Integer.parseInt(
                    cantidadEmitirTexto
                );

            String respuestaEmision =
                servicio.emitirFactura(
                    numeroFactura,
                    codigoEmitir,
                    cantidadEmitir
                );

            areaTexto.append(
                "\n"
                + respuestaEmision
            );

            if (respuestaEmision.startsWith(
                    "FACTURA_EMITIDA|")) {

                JOptionPane.showMessageDialog(
                    this,
                    "Factura emitida correctamente."
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    respuestaEmision,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Cantidad inválida.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IOException e) {

            mostrarErrorConexion(e);
        }
    }

    private void salir() {

        int opcion =
            JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir de FideCompro?",
                "Salir",
                JOptionPane.YES_NO_OPTION
            );

        if (opcion == JOptionPane.YES_OPTION) {

            dispose();
        }
    }

    private void mostrarErrorConexion(
            Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "No se pudo conectar con el servidor.\n\n"
            + e.getMessage(),
            "Error de conexión",
            JOptionPane.ERROR_MESSAGE
        );
    }
}