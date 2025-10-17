package dev.com.confectextil.dominio.principal.modelo;

import dev.com.confectextil.dominio.principal.insumo.InsumoId;
import java.util.Objects;

public record InsumoPadrao(InsumoId insumoId, double quantidadeSugerida) {
    public InsumoPadrao {
        Objects.requireNonNull(insumoId, "ID do Insumo não pode ser nulo.");
        if (quantidadeSugerida <= 0) {
            throw new IllegalArgumentException("Quantidade sugerida deve ser maior que zero.");
        }
    }
}