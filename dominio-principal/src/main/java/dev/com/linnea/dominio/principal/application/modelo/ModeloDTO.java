package dev.com.linnea.dominio.principal.application.modelo;

import java.util.List;

public record ModeloDTO(
    String id,
    String referencia,
    String nome,
    String imagemUrl,
    List<InsumoPadraoDTO> insumosPadrao
) {
    public record InsumoPadraoDTO(
        String insumoId,
        double quantidadeSugerida
    ) {}
}