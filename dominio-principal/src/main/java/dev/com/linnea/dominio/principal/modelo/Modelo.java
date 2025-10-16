package dev.com.linnea.dominio.principal.modelo;

import java.util.List;
import java.util.Objects;

public class Modelo {

    private final ModeloId id;
    private String referencia;
    private String nome;
    private String imagemUrl;
    private List<InsumoPadrao> insumosPadrao;

    public Modelo(ModeloId id, String referencia, String nome, String imagemUrl, List<InsumoPadrao> insumosPadrao) {
        if (id == null) {
            throw new IllegalArgumentException("ID do modelo não pode ser nulo.");
        }

        if (referencia == null || referencia.isBlank()) {
            throw new IllegalArgumentException("A referência do modelo é obrigatória.");
        }
        
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do modelo é obrigatório.");
        }

        this.id = id;
        this.referencia = referencia;
        this.nome = nome;
        this.imagemUrl = imagemUrl;
        this.insumosPadrao = insumosPadrao != null ? insumosPadrao : List.of();
    }

    public ModeloId getId() {
        return id;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getNome() {
        return nome;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public List<InsumoPadrao> getInsumosPadrao() {
        return List.copyOf(insumosPadrao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modelo modelo = (Modelo) o;
        return Objects.equals(id, modelo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}