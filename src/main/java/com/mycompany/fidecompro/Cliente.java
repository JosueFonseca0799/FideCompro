package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */


public class Cliente extends Persona {
    private String cedula;
    private String telefono;
    private String email;
    private String direccion;

    public Cliente(String id, String nombre, String cedula, String telefono, String email, String direccion) {
        super(id, nombre);
        this.cedula = cedula;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return getNombre() + " - Cédula: " + cedula;
    }
}