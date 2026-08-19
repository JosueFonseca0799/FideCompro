package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

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
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        try {
            Usuario u = sistema.login(usuario, password);
            JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre());

            VentanaPrincipal vp = new VentanaPrincipal(sistema, u);
            vp.setVisible(true);

            dispose();

        } catch (AutenticacionExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}