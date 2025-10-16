package dev.com.linnea.dominio.principal.insumo;

import java.util.Objects;

public class Insumo {

    private final InsumoId id;
    private String nome;
    private String unidadeDeMedida;
    private float custoUnitario;
    private String referencia; 

    public Insumo(InsumoId id, String referencia, String nome, String unidadeDeMedida, float custoUnitario) {
        
        if (id == null) {
            throw new IllegalArgumentException("ID do insumo não pode ser nulo.");
        }

        if (referencia == null || referencia.isBlank()) {
            throw new IllegalArgumentException("Referência do insumo é obrigatória.");
        }

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do insumo é obrigatório.");
        }
        
        this.id = id;
        this.referencia = referencia;
        this.nome = nome;
        this.unidadeDeMedida = unidadeDeMedida;
        this.custoUnitario = custoUnitario;
    }

    // Getters para todos os atributos
    public InsumoId getId() {
        return id;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getNome() {
        return nome;
    }

    public String getUnidadeDeMedida() {
        return unidadeDeMedida;
    }

    public float getCustoUnitario() {
        return custoUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Insumo insumo = (Insumo) o;
        return Objects.equals(id, insumo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}