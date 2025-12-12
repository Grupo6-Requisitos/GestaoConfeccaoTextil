package dev.com.linnea.infraestrutura.persistencia.jpa.fabrico;

import dev.com.confectextil.dominio.principal.fabrico.FabricoId;
import dev.com.confectextil.dominio.principal.fabrico.FabricoRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.com.confectextil.dominio.principal.Fabrico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FAB_FABRICO")
class FabricoJpa {

    @Id
    @Column(name = "FAB_ID", nullable = false, length = 64)
    String id;

    @Column(name = "FAB_NF", nullable = false)
    String nomeFantasia;

    @Column(name = "FAB_CNPJ", nullable = false)
    String cnpj;

    FabricoJpa() {}

    static FabricoJpa fromDomain(Fabrico fabrico) {
        FabricoJpa jpa = new FabricoJpa();

        // se o FabricoId for nulo, deixamos id nulo (banco pode aceitar ou você trata no serviço)
        jpa.id = (fabrico.getId() == null) ? null : fabrico.getId().valor();
        jpa.nomeFantasia = fabrico.getNomeFantasia();
        jpa.cnpj = fabrico.getCnpj();

        return jpa;
    }

    Fabrico toDomain() {
        FabricoId fabricoId = (this.id == null || this.id.isBlank())
                ? null
                : FabricoId.comValor(this.id);

        if (fabricoId != null) {
            return new Fabrico(fabricoId, this.nomeFantasia, this.cnpj);
        } else {
            return new Fabrico(this.nomeFantasia, this.cnpj);
        }
    }

    @Override
    public String toString() {
        return nomeFantasia;
    }
}

interface FabricoJpaRepository extends JpaRepository<FabricoJpa, String> {
    Optional<FabricoJpa> findByCnpj(String cnpj);
}

@Repository
class FabricoRepositorioImpl implements FabricoRepository {

    @Autowired
    FabricoJpaRepository repositorio;

    @Override
    public void salvar(Fabrico fabrico) {
        FabricoJpa jpa = FabricoJpa.fromDomain(fabrico);
        repositorio.save(jpa);
    }

    @Override
    public Fabrico editar(Fabrico fabrico) {
        FabricoJpa jpa = FabricoJpa.fromDomain(fabrico);
        FabricoJpa salvo = repositorio.save(jpa);
        return salvo.toDomain();
    }

    @Override
    public Optional<Fabrico> buscarPorId(FabricoId fabricoId) {
        return repositorio.findById(fabricoId.valor())
                          .map(FabricoJpa::toDomain);
    }

    @Override
    public Optional<Fabrico> buscarPorCnpj(String cnpj) {
        return repositorio.findByCnpj(cnpj)
                          .map(FabricoJpa::toDomain);
    }
}