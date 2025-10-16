package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.fabrico.FabricoId;
import java.util.Objects;

public class Fabrico {
    
    private final FabricoId id;
    private String nomeFantasia;
    private String cnpj;

    public Fabrico(FabricoId id, String nomeFantasia, String cnpj) {
        if (id == null) {
            throw new IllegalArgumentException("ID do Fabrico não pode ser nulo.");
        }

        if (nomeFantasia == null || nomeFantasia.isBlank()) {
            throw new IllegalArgumentException("O nome fantasia do Fabrico é obrigatório.");
        }

        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("O CNPJ do Fabrico é obrigatório.");
        }

        if (!cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public FabricoId getId() { 
        return id; 
    }

    public String getNomeFantasia() { 
        return nomeFantasia; 
    }

    public void setNomeFantasia(String nomeFantasia) { 
        if (nomeFantasia != null && !nomeFantasia.isBlank()) {
            this.nomeFantasia = nomeFantasia;
        }
    }

    public String getCnpj() { 
        return cnpj; 
    }

    public void setCnpj(String cnpj) { 
        if (cnpj != null && !cnpj.isBlank()) {
            if (!cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
                throw new IllegalArgumentException("CNPJ inválido");
            }
            this.cnpj = cnpj;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fabrico fabrico = (Fabrico) o;
        return Objects.equals(id, fabrico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Fabrico{" +
                "id=" + id +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
