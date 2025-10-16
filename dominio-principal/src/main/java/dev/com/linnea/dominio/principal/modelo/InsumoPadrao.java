package dev.com.linnea.dominio.principal.modelo;

import dev.com.linnea.dominio.principal.insumo.InsumoId;

public record InsumoPadrao(InsumoId insumoId, double quantidadeSugerida) {

    public InsumoPadrao {
        // Validações (invariantes) do Value Object
        if (insumoId == null) {
            throw new IllegalArgumentException("ID do insumo não pode ser nulo.");
        }
        if (quantidadeSugerida <= 0) {
            throw new IllegalArgumentException("Quantidade sugerida deve ser maior que zero.");
        }
    }
}