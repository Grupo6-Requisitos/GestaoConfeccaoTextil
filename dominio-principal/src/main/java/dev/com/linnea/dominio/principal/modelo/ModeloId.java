package dev.com.linnea.dominio.principal.modelo;

import java.util.UUID;

public record ModeloId(UUID valor) {

    public static ModeloId novo() {
        return new ModeloId(UUID.randomUUID());
    }

    public static ModeloId comValor(String valor) {
        return new ModeloId(UUID.fromString(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}