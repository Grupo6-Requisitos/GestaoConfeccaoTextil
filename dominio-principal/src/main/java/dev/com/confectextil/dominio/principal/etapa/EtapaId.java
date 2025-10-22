package dev.com.confectextil.dominio.principal.etapa;

import java.util.Objects; // Importar java.util.Objects para simplificar equals/hashCode

public class EtapaId {
    private final String valor;

    public EtapaId(String valor) {
        this.valor = valor;
    }

    public static EtapaId novo(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID da Etapa não pode ser vazio.");
        }
        return new EtapaId(id);
    }
    
    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtapaId etapaId = (EtapaId) o;
        return Objects.equals(valor, etapaId.valor); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return valor;
    }
}