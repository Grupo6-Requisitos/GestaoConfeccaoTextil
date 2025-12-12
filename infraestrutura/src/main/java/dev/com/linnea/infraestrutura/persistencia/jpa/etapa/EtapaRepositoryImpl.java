package dev.com.linnea.infraestrutura.persistencia.jpa.etapa;

import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaId;
import dev.com.confectextil.dominio.principal.etapa.EtapaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EtapaRepositoryImpl implements EtapaRepository {

    private final EtapaRepositorySpringData springData;

    public EtapaRepositoryImpl(EtapaRepositorySpringData springData) {
        this.springData = springData;
    }

    @Override
    public void salvar(Etapa etapa) {
        EtapaJpa jpa = EtapaJpa.fromDomain(etapa);
        // Preserva ID se já existir
        springData.findById(etapa.getId().getValor())
                  .ifPresent(existing -> jpa.setId(existing.getId()));
        springData.save(jpa);
    }

    @Override
    public Etapa editar(Etapa etapa) {
        EtapaJpa jpa = EtapaJpa.fromDomain(etapa);
        springData.save(jpa);
        return jpa.toDomain();
    }

    @Override
    public Optional<Etapa> buscarPorId(EtapaId etapaId) {
        return springData.findById(etapaId.getValor())
                         .map(EtapaJpa::toDomain);
    }

    @Override
    public void excluir(EtapaId id) {
        springData.deleteById(id.getValor());
    }
    @Override
    public List<Etapa> listarTodos() {
        return springData.findAll().stream()
                         .map(EtapaJpa::toDomain)
                         .collect(Collectors.toList());
    }

}
