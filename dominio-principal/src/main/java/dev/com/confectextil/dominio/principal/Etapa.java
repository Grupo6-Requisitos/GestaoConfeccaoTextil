package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.etapa.EtapaId;
import java.util.Objects;

public class Etapa {

    private final EtapaId id;
    private String nome;
    private int ordem;
    private String tipo; // <--- NOVO CAMPO PARA PERSISTIR

    // Construtor completo com tipo
    public Etapa(EtapaId id, String nome, int ordem, String tipo) {
        this.id = id;
        setNome(nome);
        setordem(ordem);
        this.tipo = tipo;
    }

    // Construtor de compatibilidade (passa null no tipo)
    public Etapa(EtapaId id, String nome, int ordem) {
        this(id, nome, ordem, null);
    }

    public EtapaId getId() { return id; }
    public String getNome() { return nome; }
    public int getOrdem() { return ordem; }
    public String getTipo() { return tipo; } // <--- GETTER DO TIPO

    public void atualizar(String novoNome, Integer novaOrdem) {
        if (novoNome != null && !novoNome.isBlank()) {
            alterarNome(novoNome);
        }
        if (novaOrdem != null) {
            alterarOrdem(novaOrdem);
        }
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }
    public void setordem(int ordem) {
        validarOrdem(ordem);
        this.ordem = ordem;
    }
    public void alterarNome(String novoNome) {
        validarNome(novoNome);
        this.nome = novoNome;
    }
    public void alterarOrdem(int novaOrdem) {
        validarOrdem(novaOrdem);
        this.ordem = novaOrdem;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da Etapa é obrigatório.");
        }
    }
    private void validarOrdem(int ordem) {
        if (ordem <= 0) {
            throw new IllegalArgumentException("A ordem da Etapa deve ser um número positivo.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Etapa etapa = (Etapa) obj;
        return id.equals(etapa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}