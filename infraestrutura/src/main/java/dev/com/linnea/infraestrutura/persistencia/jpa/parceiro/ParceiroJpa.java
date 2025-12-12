package dev.com.linnea.infraestrutura.persistencia.jpa.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PARCEIRO")
public class ParceiroJpa {
    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String id;

    @Column(name = "NOME", nullable = false, length = 80)
    private String nome;

    @Column(name = "TELEFONE", nullable = false, length = 11)
    private String telefone;

    public ParceiroJpa() {
    }
    public ParceiroJpa(String id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }
    public static ParceiroJpa fromDomain(Parceiro parceiro) {
        if (parceiro == null) {
            return null;
        }
        return new ParceiroJpa(
                parceiro.getId().getId(),
                parceiro.getNome(),
                parceiro.getTelefone()
        );
    }

    public Parceiro toDomain() {
        return new Parceiro(
                new ParceiroId(this.id),
                this.nome,
                this.telefone
        );
    }

    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getTelefone() {
        return telefone;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "ParceiroJpa{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}