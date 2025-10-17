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
        this.referencia = Objects.requireNonNull(referencia, "Referência do Insumo é obrigatória.");
        this.nome = Objects.requireNonNull(nome, "Nome do Insumo é obrigatório.");
        this.unidade = Objects.requireNonNull(unidade, "Unidade do Insumo é obrigatória.");
        this.quantidadeEmEstoque = quantidadeEmEstoque;
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
}