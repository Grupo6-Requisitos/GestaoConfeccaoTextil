package dev.com.linnea.infraestrutura.persistencia.jpa.insumo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.com.confectextil.dominio.principal.Insumo;
import dev.com.confectextil.dominio.principal.insumo.InsumoId;
import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "insumo")
class InsumoJpa {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "referencia", nullable = false, unique = true)
    private String referencia;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "unidade", nullable = false)
    private String unidade;

    @Column(name = "quantidade_estoque", nullable = false)
    private double quantidadeEmEstoque;

    public InsumoJpa() {}

    static InsumoJpa fromDomain(Insumo insumo) {
        InsumoJpa jpa = new InsumoJpa();
        if (insumo.getId() != null) {
            jpa.id = insumo.getId().valor();
        }
        jpa.referencia = insumo.getReferencia();
        jpa.nome = insumo.getNome();
        jpa.unidade = insumo.getUnidade();
        jpa.quantidadeEmEstoque = insumo.getQuantidadeEmEstoque();
        return jpa;
    }

    Insumo toDomain() {
        InsumoId insumoId = (id == null || id.isBlank()) ? InsumoId.novo(java.util.UUID.randomUUID().toString()) : InsumoId.novo(id);
        return new Insumo(insumoId, referencia, nome, unidade, quantidadeEmEstoque);
    }
}

interface InsumoJpaRepository extends JpaRepository<InsumoJpa, String> {
    Optional<InsumoJpa> findByReferencia(String referencia);
}

@Repository
class InsumoRepositorioImpl implements InsumoRepository {

    @Autowired
    private InsumoJpaRepository repositorio;

    @Override
    public void salvar(Insumo insumo) {
        repositorio.save(InsumoJpa.fromDomain(insumo));
    }

    @Override
    public Optional<Insumo> buscarPorReferencia(String referencia) {
        return repositorio.findByReferencia(referencia)
                .map(InsumoJpa::toDomain);
    }

    @Override
    public Optional<Insumo> buscarPorId(InsumoId id) {
        if (id == null) {
            return Optional.empty();
        }
        return repositorio.findById(id.valor())
                .map(InsumoJpa::toDomain);
    }
}