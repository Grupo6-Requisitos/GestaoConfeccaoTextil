package dev.com.linnea.dominio.principal.application.modelo;

import dev.com.linnea.dominio.principal.insumo.Insumo;
import dev.com.linnea.dominio.principal.insumo.InsumoRepository;
import dev.com.linnea.dominio.principal.modelo.InsumoPadrao;
import dev.com.linnea.dominio.principal.modelo.Modelo;
import dev.com.linnea.dominio.principal.modelo.ModeloId;
import dev.com.linnea.dominio.principal.modelo.ModeloRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CadastrarModeloServico {

    private final ModeloRepository modeloRepository;
    private final InsumoRepository insumoRepository;

    public CadastrarModeloServico(ModeloRepository modeloRepository, InsumoRepository insumoRepository) {
        this.modeloRepository = modeloRepository;
        this.insumoRepository = insumoRepository;
    }

    public ModeloDTO executar(CadastrarModeloCommand comando) {
        this.modeloRepository.buscarPorReferencia(comando.referencia()).ifPresent(modeloExistente -> {
            throw new IllegalStateException("A referência do modelo já existe");
        });

        List<InsumoPadrao> insumosPadrao = new ArrayList<>();

        if (comando.insumos() != null && !comando.insumos().isEmpty()) {
            for (var insumoCmd : comando.insumos()) {
                Insumo insumo = this.insumoRepository
                    .buscarPorReferencia(insumoCmd.insumoReferencia())
                    .orElseThrow(() -> new IllegalArgumentException("O insumo com referência " + insumoCmd.insumoReferencia() + " não foi encontrado"));
                
                insumosPadrao.add(new InsumoPadrao(insumo.getId(), insumoCmd.quantidadeSugerida()));
            }
        }
        
        Modelo novoModelo = new Modelo(
            ModeloId.novo(),
            comando.referencia(),
            comando.nome(),
            comando.imagemUrl(),
            insumosPadrao
        );

        this.modeloRepository.salvar(novoModelo);

        return new ModeloDTOMapper().apply(novoModelo);
    }

    private static class ModeloDTOMapper {
        public ModeloDTO apply(Modelo modelo) {
            List<ModeloDTO.InsumoPadraoDTO> insumosDTO = modelo.getInsumosPadrao().stream()
                .map(insumoPadrao -> new ModeloDTO.InsumoPadraoDTO(
                    insumoPadrao.insumoId().toString(),
                    insumoPadrao.quantidadeSugerida()
                ))
                .collect(Collectors.toList());

            return new ModeloDTO(
                modelo.getId().toString(),
                modelo.getReferencia(),
                modelo.getNome(),
                modelo.getImagemUrl(),
                insumosDTO
            );
        }
    }
}