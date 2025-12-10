package dev.com.linnea.infraestrutura.persistencia.jpa.modelo;

import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ModeloRepositoryImpl implements ModeloRepository {

    private final ModeloRepositorySpringData springData;
    private final ModelMapper mapper;

    public ModeloRepositoryImpl(ModeloRepositorySpringData springData, ModelMapper mapper) {
        this.springData = springData;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Modelo modelo) {
        ModeloJpa jpa = ModeloJpa.fromDomain(modelo);
        // garante que mantém o mesmo ID se já existir na base
        springData.findByReferencia(modelo.getReferencia())
                .ifPresent(existing -> jpa.setId(existing.getId()));
        springData.save(jpa);
    }

    @Override
    public Optional<Modelo> buscarPorReferencia(String referencia) {
        return springData.findByReferencia(referencia).map(j -> j.toDomain(mapper));
    }

    @Override
    public List<Modelo> listarTodos() {
        return springData.findAll().stream().map(j -> j.toDomain(mapper)).collect(Collectors.toList());
    }

    @Override
    public void removerPorReferencia(String referencia) {
        springData.deleteByReferencia(referencia);
    }

    @Override
    public void atualizar(String referenciaOriginal, Modelo modeloAtualizado) {
        springData.findByReferencia(referenciaOriginal).ifPresent(existing -> {
            ModeloJpa jpa = ModeloJpa.fromDomain(modeloAtualizado);
            // preserva ID atual
            jpa.setId(existing.getId());
            springData.save(jpa);
        });
    }
}
