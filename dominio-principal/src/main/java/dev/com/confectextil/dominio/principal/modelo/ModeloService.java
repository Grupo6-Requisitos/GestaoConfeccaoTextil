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

    public void removerModeloPorReferencia(String referencia) {
        if (referencia == null || referencia.isBlank()) {
            throw new IllegalArgumentException("Referência alvo não informada");
        }

        Modelo modelo = modeloRepository.buscarPorReferencia(referencia)
            .orElseThrow(() -> new IllegalStateException("O modelo com referência " + referencia + " não foi encontrado"));

        modeloRepository.removerPorReferencia(modelo.getReferencia());
    }

    public Modelo atualizarModelo(String referenciaAlvo, String novaReferencia, String novoNome) {
        if (referenciaAlvo == null || referenciaAlvo.isBlank()) {
            throw new IllegalArgumentException("Referência alvo não informada");
        }

        Modelo existente = modeloRepository.buscarPorReferencia(referenciaAlvo)
            .orElseThrow(() -> new IllegalStateException("O modelo com referência " + referenciaAlvo + " não foi encontrado"));

        String referenciaFinal = existente.getReferencia();
        if (novaReferencia != null) {
            if (novaReferencia.isBlank()) {
                throw new IllegalArgumentException("A nova referência é obrigatória");
            }
            if (!novaReferencia.equals(referenciaFinal) && modeloRepository.buscarPorReferencia(novaReferencia).isPresent()) {
                throw new IllegalStateException("Já existe um modelo com a referência " + novaReferencia);
            }
            referenciaFinal = novaReferencia;
        }

        String nomeFinal = existente.getNome();
        if (novoNome != null) {
            if (novoNome.isBlank()) {
                throw new IllegalArgumentException("O nome do modelo é obrigatório");
            }
            nomeFinal = novoNome;
        }

        Modelo atualizado = new Modelo(
            existente.getId(),
            referenciaFinal,
            nomeFinal,
            existente.getImagemUrl(),
            existente.getInsumosPadrao()
        );

        modeloRepository.atualizar(referenciaAlvo, atualizado);

        return atualizado;
    }

    public List<Modelo> listarTodos() {
        return modeloRepository.listarTodos();
    }
}
