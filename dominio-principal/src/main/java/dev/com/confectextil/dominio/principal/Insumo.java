package dev.com.confectextil.dominio.principal;

import java.util.Objects;

import dev.com.confectextil.dominio.principal.insumo.InsumoId;

public class Insumo {
    private final InsumoId id;
    private String referencia;
    private String nome;
    private String unidade;
    private double quantidadeEmEstoque;

    public Insumo(InsumoId id, String referencia, String nome, String unidade, double quantidadeEmEstoque) {
        this.id = Objects.requireNonNull(id, "ID do Insumo não pode ser nulo.");
        setReferencia(referencia);
        setNome(nome);
        setUnidade(unidade);
        setQuantidadeEmEstoque(quantidadeEmEstoque);
    }

    public Insumo(String referencia, String nome, String unidade) {
        this(new InsumoId(), referencia, nome, unidade, 0);
    }

    public InsumoId getId() {
        return id;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getNome() {
        return nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public double getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void setReferencia(String referencia) {
        this.referencia = Objects.requireNonNull(referencia, "Referência do Insumo é obrigatória.");
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome do Insumo é obrigatório.");
    }

    public void setUnidade(String unidade) {
        this.unidade = Objects.requireNonNull(unidade, "Unidade do Insumo é obrigatória.");
    }

    public void setQuantidadeEmEstoque(double quantidadeEmEstoque) {
        this.quantidadeEmEstoque = quantidadeEmEstoque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Insumo insumo = (Insumo) o;
        return id.equals(insumo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}