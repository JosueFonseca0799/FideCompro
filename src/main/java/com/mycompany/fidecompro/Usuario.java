package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */


public class Usuario extends Persona {
    private String username;
    private String password;
    private String rol;
    private boolean activo;

    public Usuario(String id, String nombre, String username, String password, String rol, boolean activo) {
        super(id, nombre);
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean validarPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return getNombre() + " (" + username + ")";
    }
}