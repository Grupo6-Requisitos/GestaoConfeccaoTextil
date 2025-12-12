package dev.com.linnea.infraestrutura.persistencia.jpa.parceiro;

import dev.com.linnea.aplicacao.principal.parceiro.ParceiroResumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParceiroRepositorySpringData extends JpaRepository<ParceiroJpa, String> {
    @Query("SELECT p FROM ParceiroJpa p")
    List<ParceiroResumo> findAllProjectedBy();
}