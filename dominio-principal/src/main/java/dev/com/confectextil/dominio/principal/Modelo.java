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

        setReferencia(referencia);
        setNome(nome);
        setImagemUrl(imagemUrl);
        setInsumosPadrao(insumosPadrao);
    }

    public Modelo(String referencia, String nome, String imagemUrl) {
        this.id = new ModeloId();

        setReferencia(referencia);
        setNome(nome);
        setImagemUrl(imagemUrl);
        setInsumosPadrao(insumosPadrao);
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

    public void setReferencia(String referencia) {
        if (referencia == null || referencia.isBlank()) {
            throw new IllegalArgumentException("A referência do modelo é obrigatória");
        }
        this.referencia = referencia;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do modelo é obrigatório");
        }
        this.nome = nome;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setInsumosPadrao(List<InsumoPadrao> insumosPadrao) {
        this.insumosPadrao = insumosPadrao;
    }
}