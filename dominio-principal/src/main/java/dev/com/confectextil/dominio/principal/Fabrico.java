package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.fabrico.FabricoId;
import java.util.Objects;

public class Fabrico {
    
    private final FabricoId id;
    private String nomeFantasia;
    private String cnpj;

    public Fabrico(FabricoId id, String nomeFantasia, String cnpj) {
        this.id = Objects.requireNonNull(id, "ID do Fabrico não pode ser nulo.");
        
        setNomeFantasia(nomeFantasia);
        setCnpj(cnpj);
    }


    public Fabrico(String nomeFantasia, String cnpj) {
        this.id = new FabricoId();
        
        setNomeFantasia(nomeFantasia);
        setCnpj(cnpj);
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
