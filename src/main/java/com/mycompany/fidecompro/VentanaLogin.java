package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */
import com.mycompany.fidecompro.cliente.ClienteServicio;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private SistemaTienda sistema;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public VentanaLogin(SistemaTienda sistema) {

        this.sistema = sistema;

        setTitle("FideCompro - Inicio de sesión");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();
        btnIngresar = new JButton("Ingresar");

        add(new JLabel("Usuario:"));
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        add(txtPassword);

        add(new JLabel(""));
        add(btnIngresar);

        btnIngresar.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {

        String usuario = txtUsuario.getText().trim();
        String password
                = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar usuario y contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            ClienteServicio servicio
                    = new ClienteServicio();

            String respuesta
                    = servicio.login(usuario, password);

            if (respuesta.startsWith("LOGIN_OK|")) {

                String[] datos
                        = respuesta.split("\\|");

                /*
                 * Formato:
                 *
                 * LOGIN_OK|ID|NOMBRE|ROL
                 */
                String id = datos[1];
                String nombre = datos[2];
                String rol = datos[3];

                Usuario u = new Usuario(
                        id,
                        nombre,
                        usuario,
                        password,
                        rol,
                        true
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Bienvenido " + nombre
                );

                VentanaPrincipal vp
                        = new VentanaPrincipal(
                                sistema,
                                u
                        );

                vp.setVisible(true);

                dispose();

            } else {

                String mensaje
                        = "Credenciales incorrectas.";

                if (respuesta.contains(
                        "CREDENCIALES_INVALIDAS")) {

                    mensaje
                            = "Usuario o contraseña incorrectos.";
                }

                JOptionPane.showMessageDialog(
                        this,
                        mensaje,
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con el servidor FideCompro.\n"
                    + "Verifique que el servidor esté iniciado.\n\n"
                    + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}