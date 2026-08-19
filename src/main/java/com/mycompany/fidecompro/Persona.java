package com.mycompany.fidecompro;
import java.io.Serializable;
/**
 *
 * @author josue
 */

public abstract class Persona implements Serializable {
    private String id;
    private String nombre;

    public Persona(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}