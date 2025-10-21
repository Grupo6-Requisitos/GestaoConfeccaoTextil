package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.etapa.EtapaId;

public class Etapa {

    private final EtapaId id;
    private String nome;
    private int ordem;

    public Etapa(EtapaId id, String nome, int ordem) {
        validarNome(nome);
        validarOrdem(ordem);

        if (id == null) {
            throw new IllegalArgumentException("ID da Etapa não pode ser nulo.");
        }

        this.id = id;
        this.nome = nome;
        this.ordem = ordem;
    }


    public EtapaId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getOrdem() {
        return ordem;
    }


    public void atualizar(String novoNome, Integer novaOrdem) {
        if (novoNome != null && !novoNome.isBlank()) {
            alterarNome(novoNome);
        }

        if (novaOrdem != null) {
            alterarOrdem(novaOrdem);
        }
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
    public String toString() {
        return "Etapa [id=" + id + ", nome=" + nome + ", ordem=" + ordem + "]";
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
        return id.hashCode();
    }
}
