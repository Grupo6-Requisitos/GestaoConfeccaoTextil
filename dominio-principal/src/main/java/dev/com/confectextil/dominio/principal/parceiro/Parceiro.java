package dev.com.confectextil.dominio.principal.parceiro;

import java.util.Objects;

public class Parceiro {
    private final ParceiroId id;
    private String nome;
    private String telefone;

    public Parceiro(ParceiroId id, String nome, String telefone){
        if (id==null) {
            throw new IllegalArgumentException("Parceiro precisa de um ID válido");
        }
        this.id = id;
        alterarNome(nome);
        alterarTelefone(telefone);
    }

    public ParceiroId getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getTelefone(){
        return telefone;
    }

    public void alterarNome(String nome){
        if (nome==null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome de parceiro esta vazio");
        }
        if (nome.length() < 3){
            throw new IllegalArgumentException("O nome deve ter no minimo 3 caracteres");
        }
        this.nome=nome;
    }
    public void alterarTelefone(String telefone){
        if (telefone==null || telefone.trim().isEmpty()){
            throw new IllegalArgumentException("O telefone de parceiro esta vazio");
        }
        if (!telefone.matches("\\d{11}")) {
            throw new IllegalArgumentException("O telefone deve conter exatamente 11 digitos numericos");
        }
        this.telefone=telefone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Parceiro p)) return false;
        return Objects.equals(id, p.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
