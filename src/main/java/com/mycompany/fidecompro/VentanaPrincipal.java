package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private SistemaTienda sistema;
    private Usuario usuarioActual;
    private JTextArea areaTexto;

    public VentanaPrincipal(SistemaTienda sistema, Usuario usuarioActual) {
        this.sistema = sistema;
        this.usuarioActual = usuarioActual;

        setTitle("FideCompro - Menú Principal");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 10, 10));

        JButton btnRegistrarCliente = new JButton("Registrar cliente");
        JButton btnRegistrarProducto = new JButton("Registrar producto");
        JButton btnEntradaInventario = new JButton("Entrada inventario");
        JButton btnCrearFactura = new JButton("Crear factura");
        JButton btnVerResumen = new JButton("Ver resumen");
        JButton btnGuardar = new JButton("Guardar datos");

        panelBotones.add(btnRegistrarCliente);
        panelBotones.add(btnRegistrarProducto);
        panelBotones.add(btnEntradaInventario);
        panelBotones.add(btnCrearFactura);
        panelBotones.add(btnVerResumen);
        panelBotones.add(btnGuardar);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        add(panelBotones, BorderLayout.NORTH);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        btnRegistrarCliente.addActionListener(e -> registrarCliente());
        btnRegistrarProducto.addActionListener(e -> registrarProducto());
        btnEntradaInventario.addActionListener(e -> entradaInventario());
        btnCrearFactura.addActionListener(e -> crearFactura());
        btnVerResumen.addActionListener(e -> actualizarArea());
        btnGuardar.addActionListener(e -> {
            sistema.guardarDatos();
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente.");
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sistema.guardarDatos();
            }
        });

        actualizarArea();
    }

    private void registrarCliente() {
        try {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del cliente:");
            String cedula = JOptionPane.showInputDialog(this, "Cédula:");
            String telefono = JOptionPane.showInputDialog(this, "Teléfono:");
            String email = JOptionPane.showInputDialog(this, "Email:");
            String direccion = JOptionPane.showInputDialog(this, "Dirección:");

            if (nombre == null || cedula == null || nombre.isBlank() || cedula.isBlank()) {
                JOptionPane.showMessageDialog(this, "Nombre y cédula son obligatorios.");
                return;
            }

            sistema.registrarCliente(nombre, cedula, telefono, email, direccion);
            JOptionPane.showMessageDialog(this, "Cliente registrado con éxito.");
            actualizarArea();

        } catch (DatoDuplicadoExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarProducto() {
        try {
            String codigo = JOptionPane.showInputDialog(this, "Código del producto:");
            String nombre = JOptionPane.showInputDialog(this, "Nombre del producto:");
            String precioTexto = JOptionPane.showInputDialog(this, "Precio unitario:");
            String stockTexto = JOptionPane.showInputDialog(this, "Stock inicial:");

            if (codigo == null || nombre == null || precioTexto == null || stockTexto == null) {
                return;
            }

            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            String[] opciones = {"Gravado", "Exento"};
            int tipo = JOptionPane.showOptionDialog(
                    this,
                    "Seleccione el tipo de producto:",
                    "Tipo de producto",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            Producto producto;

            if (tipo == 0) {
                String impuestoTexto = JOptionPane.showInputDialog(this, "Porcentaje de impuesto (ejemplo 0.13):");
                double impuesto = Double.parseDouble(impuestoTexto);
                producto = new ProductoGravado(codigo, nombre, precio, stock, impuesto);
            } else {
                producto = new ProductoExento(codigo, nombre, precio, stock);
            }

            sistema.registrarProducto(producto);
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
            actualizarArea();

        } catch (DatoDuplicadoExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Debe ingresar valores numéricos válidos.");
        }
    }

    private void entradaInventario() {
        try {
            String codigo = JOptionPane.showInputDialog(this, "Código del producto:");
            String cantidadTexto = JOptionPane.showInputDialog(this, "Cantidad a ingresar:");

            if (codigo == null || cantidadTexto == null) {
                return;
            }

            int cantidad = Integer.parseInt(cantidadTexto);
            sistema.entradaInventario(codigo, cantidad);

            JOptionPane.showMessageDialog(this, "Entrada de inventario realizada.");
            actualizarArea();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser numérica.");
        }
    }

    private void crearFactura() {
        try {
            String cedula = JOptionPane.showInputDialog(this, "Cédula del cliente:");
            if (cedula == null || cedula.isBlank()) {
                return;
            }

            Factura factura = sistema.crearFactura(cedula, usuarioActual);

            if (factura == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el cliente.");
                return;
            }

            while (true) {
                String codigo = JOptionPane.showInputDialog(this, "Código del producto (Cancelar para terminar):");
                if (codigo == null || codigo.isBlank()) {
                    break;
                }

                String cantidadTexto = JOptionPane.showInputDialog(this, "Cantidad:");
                if (cantidadTexto == null || cantidadTexto.isBlank()) {
                    break;
                }

                int cantidad = Integer.parseInt(cantidadTexto);
                sistema.agregarLineaFactura(factura, codigo, cantidad);
            }

            sistema.emitirFactura(factura);

            JOptionPane.showMessageDialog(
                    this,
                    "Factura emitida correctamente.\nTotal: " + factura.calcularTotal()
            );

            actualizarArea();

        } catch (StockInsuficienteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
        }
    }

    private void actualizarArea() {
        StringBuilder sb = new StringBuilder();

        sb.append("Usuario actual: ").append(usuarioActual.getNombre()).append("\n\n");

        sb.append("=== CLIENTES ===\n");
        for (Cliente c : sistema.getClientes()) {
            sb.append(c).append("\n");
        }

        sb.append("\n=== PRODUCTOS ===\n");
        for (Producto p : sistema.getProductos()) {
            sb.append(p).append("\n");
        }

        sb.append("\n=== FACTURAS ===\n");
        List<Factura> facturas = sistema.getFacturas();
        for (Factura f : facturas) {
            sb.append(f).append("\n");
            for (LineaFactura lf : f.getLineas()) {
                sb.append("   - ").append(lf).append("\n");
            }
        }

        areaTexto.setText(sb.toString());
    }
}