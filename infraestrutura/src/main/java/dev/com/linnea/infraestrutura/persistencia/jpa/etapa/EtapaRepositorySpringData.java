package dev.com.linnea.infraestrutura.persistencia.jpa.etapa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaRepositorySpringData extends JpaRepository<EtapaJpa, String> {
}
