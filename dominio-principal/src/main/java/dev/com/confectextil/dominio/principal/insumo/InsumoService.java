package dev.com.confectextil.dominio.principal.insumo;

import java.util.Objects;
import java.util.UUID;

import dev.com.confectextil.dominio.principal.Insumo;

public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = Objects.requireNonNull(insumoRepository);
    }

    public Insumo cadastrarInsumo(String referencia, String nome, String unidade) {
        if (insumoRepository.buscarPorReferencia(referencia).isPresent()) {
            throw new IllegalArgumentException("Já existe um insumo com a referência: " + referencia);
        }

        InsumoId insumoId = InsumoId.novo(UUID.randomUUID().toString());

        Insumo novoInsumo = new Insumo(
            insumoId,
            referencia,
            nome,
            unidade,
            0.0
        );

        insumoRepository.salvar(novoInsumo);

        return novoInsumo;
    }
}