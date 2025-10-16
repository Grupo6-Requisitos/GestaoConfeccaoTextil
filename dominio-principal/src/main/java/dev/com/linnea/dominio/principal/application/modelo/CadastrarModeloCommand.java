package dev.com.linnea.dominio.principal.application.modelo;

import java.util.List;

public record CadastrarModeloCommand(
    String referencia,
    String nome,
    String imagemUrl,
    List<InsumoPadraoCommand> insumos
) {
    public record InsumoPadraoCommand(
        String insumoReferencia,
        double quantidadeSugerida
    ) {}
}