package dev.com.linnea.infraestrutura.persistencia.jpa.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroId;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroRepositorio;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ParceiroRepositoryImpl implements ParceiroRepositorio {
    private final ParceiroRepositorySpringData springDataRepository;

    public ParceiroRepositoryImpl(ParceiroRepositorySpringData springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    @Transactional
    public void salvar(Parceiro parceiro) {
        ParceiroJpa parceiroJpa = ParceiroJpa.fromDomain(parceiro);
        springDataRepository.save(parceiroJpa);
    }

    @Override
    @Transactional
    public Parceiro editar(Parceiro parceiro) {
        ParceiroJpa parceiroJpa = ParceiroJpa.fromDomain(parceiro);
        ParceiroJpa updatedJpa = springDataRepository.save(parceiroJpa);
        return updatedJpa.toDomain();
    }

    @Override
    public Optional<Parceiro> buscarPorId(ParceiroId id) {
        return springDataRepository.findById(id.getId())
                .map(ParceiroJpa::toDomain);
    }

    @Override
    public List<Parceiro> listarTodos() {
        return springDataRepository.findAll()
                .stream()
                .map(ParceiroJpa::toDomain)
                .collect(Collectors.toList());
    }
}