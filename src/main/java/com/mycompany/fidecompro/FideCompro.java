package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

import javax.swing.SwingUtilities;

public class FideCompro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaTienda sistema = SistemaTienda.cargarDatos();
            VentanaLogin login = new VentanaLogin(sistema);
            login.setVisible(true);
        });
    }
}