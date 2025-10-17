package dev.com.confectextil.dominio.principal.modelo;

import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final InsumoRepository insumoRepository;

    public ModeloService(ModeloRepository modeloRepository, InsumoRepository insumoRepository) {
        this.modeloRepository = Objects.requireNonNull(modeloRepository);
        this.insumoRepository = Objects.requireNonNull(insumoRepository);
    }

    public record InsumoPadraoDTO(String insumoReferencia, double quantidadeSugerida) {}

    public Modelo cadastrarModelo(String referencia, String nome, String imagemUrl, List<InsumoPadraoDTO> insumos) {

        if (modeloRepository.buscarPorReferencia(referencia).isPresent()) {
            throw new IllegalArgumentException("A referência do modelo já existe");
        }

        List<InsumoPadrao> insumosPadrao = insumos.stream()
            .map(dto -> {
                dev.com.confectextil.dominio.principal.Insumo insumo = insumoRepository.buscarPorReferencia(dto.insumoReferencia())
                    .orElseThrow(() -> new IllegalArgumentException("O insumo com referência " + dto.insumoReferencia() + " não foi encontrado"));

                return new InsumoPadrao(insumo.getId(), dto.quantidadeSugerida());
            })
            .collect(Collectors.toList());

        ModeloId modeloId = ModeloId.novo(UUID.randomUUID().toString());

        Modelo novoModelo = new Modelo(
            modeloId,
            referencia,
            nome,
            imagemUrl,
            insumosPadrao
        );

        modeloRepository.salvar(novoModelo);

        return novoModelo;
    }

    public List<Modelo> listarTodos() {
        return modeloRepository.listarTodos();
    }
}