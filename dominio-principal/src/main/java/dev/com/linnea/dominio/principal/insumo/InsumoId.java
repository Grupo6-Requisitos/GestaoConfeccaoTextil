package dev.com.linnea.dominio.principal.insumo;

import java.util.UUID;

public record InsumoId(UUID valor) {

    public static InsumoId novo() {
        return new InsumoId(UUID.randomUUID());
    }

    public static InsumoId comValor(String valor) {
        return new InsumoId(UUID.fromString(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}