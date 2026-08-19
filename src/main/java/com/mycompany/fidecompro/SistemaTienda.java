package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaTienda implements Serializable {

    private Map<String, Usuario> usuarios;
    private Map<String, Cliente> clientes;
    private Map<String, Producto> productos;
    private List<Factura> facturas;
    private int consecutivoFactura;

    public SistemaTienda() {
        usuarios = new HashMap<>();
        clientes = new HashMap<>();
        productos = new HashMap<>();
        facturas = new ArrayList<>();
        consecutivoFactura = 1;

        cargarUsuarioBase();
    }

    private void cargarUsuarioBase() {
        if (!usuarios.containsKey("admin")) {
            Usuario admin = new Usuario("U1", "Administrador", "admin", "1234", "ADMIN", true);
            usuarios.put(admin.getUsername(), admin);
        }
    }

    public Usuario login(String username, String password) throws AutenticacionExcepcion {
        Usuario usuario = usuarios.get(username);

        if (usuario == null) {
            throw new AutenticacionExcepcion("El usuario no existe.");
        }

        if (!usuario.isActivo()) {
            throw new AutenticacionExcepcion("El usuario está inactivo.");
        }

        if (!usuario.validarPassword(password)) {
            throw new AutenticacionExcepcion("Contraseña incorrecta.");
        }

        return usuario;
    }

    public void registrarCliente(String nombre, String cedula, String telefono, String email, String direccion)
            throws DatoDuplicadoExcepcion {

        if (clientes.containsKey(cedula)) {
            throw new DatoDuplicadoExcepcion("Ya existe un cliente con esa cédula.");
        }

        Cliente cliente = new Cliente(
                "C" + (clientes.size() + 1),
                nombre,
                cedula,
                telefono,
                email,
                direccion
        );

        clientes.put(cedula, cliente);
    }

    public List<Cliente> buscarCliente(String criterio) {
        List<Cliente> resultados = new ArrayList<>();

        for (Cliente c : clientes.values()) {
            if (c.getCedula().contains(criterio) ||
                c.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                resultados.add(c);
            }
        }

        return resultados;
    }

    public Cliente buscarClientePorCedula(String cedula) {
        return clientes.get(cedula);
    }

    public void registrarProducto(Producto producto) throws DatoDuplicadoExcepcion {
        if (productos.containsKey(producto.getCodigo())) {
            throw new DatoDuplicadoExcepcion("Ya existe un producto con ese código.");
        }

        productos.put(producto.getCodigo(), producto);
    }

    public Producto buscarProducto(String codigo) {
        return productos.get(codigo);
    }

    public void entradaInventario(String codigo, int cantidad) {
        Producto producto = productos.get(codigo);
        if (producto != null) {
            producto.aumentarStock(cantidad);
        }
    }

    public void salidaInventario(String codigo, int cantidad) throws StockInsuficienteException {
        Producto producto = productos.get(codigo);
        if (producto != null) {
            producto.disminuirStock(cantidad);
        }
    }

    public Factura crearFactura(String cedulaCliente, Usuario usuario) {
        Cliente cliente = buscarClientePorCedula(cedulaCliente);
        if (cliente == null) {
            return null;
        }

        String numero = "FAC-" + consecutivoFactura++;
        Factura factura = new Factura(numero, cliente, usuario);
        facturas.add(factura);
        return factura;
    }

    public void agregarLineaFactura(Factura factura, String codigoProducto, int cantidad)
            throws StockInsuficienteException {

        Producto producto = buscarProducto(codigoProducto);

        if (producto == null) {
            throw new StockInsuficienteException("El producto no existe.");
        }

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay suficiente stock disponible.");
        }

        LineaFactura linea = new LineaFactura(producto, cantidad);
        factura.agregarLinea(linea);
    }

    public void emitirFactura(Factura factura) throws StockInsuficienteException {
        if (factura == null) {
            throw new StockInsuficienteException("La factura es nula.");
        }

        if (factura.getCliente() == null) {
            throw new StockInsuficienteException("La factura no tiene cliente.");
        }

        if (factura.getLineas().isEmpty()) {
            throw new StockInsuficienteException("La factura no tiene líneas.");
        }

        for (LineaFactura linea : factura.getLineas()) {
            if (linea.getProducto().getStock() < linea.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para " + linea.getProducto().getNombre()
                );
            }
        }

        for (LineaFactura linea : factura.getLineas()) {
            linea.getProducto().disminuirStock(linea.getCantidad());
        }

        factura.setEstado("EMITIDA");
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos.values());
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes.values());
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void guardarDatos() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("fidecompro.dat"))) {
            out.writeObject(this);
        } catch (Exception e) {
            System.out.println("No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    public static SistemaTienda cargarDatos() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("fidecompro.dat"))) {
            SistemaTienda sistema = (SistemaTienda) in.readObject();

            if (sistema.usuarios == null) sistema.usuarios = new HashMap<>();
            if (sistema.clientes == null) sistema.clientes = new HashMap<>();
            if (sistema.productos == null) sistema.productos = new HashMap<>();
            if (sistema.facturas == null) sistema.facturas = new ArrayList<>();
            if (sistema.consecutivoFactura <= 0) sistema.consecutivoFactura = sistema.facturas.size() + 1;

            sistema.cargarUsuarioBase();
            return sistema;

        } catch (Exception e) {
            return new SistemaTienda();
        }
    }
}