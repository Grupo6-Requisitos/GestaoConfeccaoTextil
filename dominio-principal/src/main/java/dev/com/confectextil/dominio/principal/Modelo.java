package dev.com.confectextil.dominio.principal;

import java.util.List;
import java.util.Objects;

import dev.com.confectextil.dominio.principal.modelo.InsumoPadrao;
import dev.com.confectextil.dominio.principal.modelo.ModeloId;

public class Modelo {
    private final ModeloId id;
    private String referencia;
    private String nome;
    private String imagemUrl;
    private List<InsumoPadrao> insumosPadrao;

    public Modelo(ModeloId id, String referencia, String nome, String imagemUrl, List<InsumoPadrao> insumosPadrao) {
        this.id = Objects.requireNonNull(id, "ID do Modelo não pode ser nulo.");

        if (referencia == null || referencia.isBlank()) {
            throw new IllegalArgumentException("A referência do modelo é obrigatória");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do modelo é obrigatório");
        }

        this.referencia = referencia;
        this.nome = nome;
        this.imagemUrl = imagemUrl;
        this.insumosPadrao = Objects.requireNonNull(insumosPadrao, "A lista de insumos padrão não pode ser nula.");
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
}