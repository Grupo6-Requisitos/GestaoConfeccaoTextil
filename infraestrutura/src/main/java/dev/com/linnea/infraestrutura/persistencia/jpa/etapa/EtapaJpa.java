package dev.com.linnea.infraestrutura.persistencia.jpa.etapa;

import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaId;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "etapa")
public class EtapaJpa {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int ordem;

    @Column(name = "tipo") // <--- COLUNA MAPEADA NO BANCO
    private String tipo;

    public EtapaJpa() {}

    // ---------- Getters e Setters ----------
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // ---------- Conversões ----------

    public static EtapaJpa fromDomain(Etapa etapa) {
        Objects.requireNonNull(etapa, "Etapa de domínio não pode ser nula");
        EtapaJpa jpa = new EtapaJpa();
        if (etapa.getId() != null) {
            jpa.setId(etapa.getId().getValor());
        }
        jpa.setNome(etapa.getNome());
        jpa.setOrdem(etapa.getOrdem());
        jpa.setTipo(etapa.getTipo()); // <--- SALVA O TIPO NO BANCO
        return jpa;
    }

    public Etapa toDomain() {
        EtapaId etapaId = (this.id == null || this.id.isBlank()) ? null : new EtapaId(this.id);
        if (etapaId != null) {
            // RECUPERA COM O TIPO
            return new Etapa(etapaId, this.nome, this.ordem, this.tipo);
        } else {
            return new Etapa(EtapaId.novo(""), this.nome, this.ordem, this.tipo);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EtapaJpa)) return false;
        EtapaJpa that = (EtapaJpa) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EtapaJpa [id=" + id + ", nome=" + nome + ", ordem=" + ordem + ", tipo=" + tipo + "]";
    }
}