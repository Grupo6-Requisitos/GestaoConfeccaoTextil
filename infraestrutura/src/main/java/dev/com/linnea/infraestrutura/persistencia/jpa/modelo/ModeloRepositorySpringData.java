package dev.com.linnea.infraestrutura.persistencia.jpa.modelo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dev.com.linnea.aplicacao.principal.modelo.ModeloResumo;

@Repository
public interface ModeloRepositorySpringData extends JpaRepository<ModeloJpa, String> {
    Optional<ModeloJpa> findByReferencia(String referencia);
    void deleteByReferencia(String referencia);
    List<ModeloResumo> findAllProjectedBy();
    ModeloResumo findProjectedByReferencia(String referencia);
}