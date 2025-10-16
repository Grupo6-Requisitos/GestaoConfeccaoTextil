package dev.com.confectextil.dominio.principal.fabrico;

public record FabricoId(String valor) {

    public static FabricoId novo(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do Fabrico não pode ser nulo ou vazio.");
        }
        return new FabricoId(id);
    }

    public static FabricoId comValor(String valor) {
        return novo(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
