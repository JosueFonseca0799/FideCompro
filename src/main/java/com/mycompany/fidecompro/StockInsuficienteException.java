package com.mycompany.fidecompro;

/**
 *
 * @author josue
 */

public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}