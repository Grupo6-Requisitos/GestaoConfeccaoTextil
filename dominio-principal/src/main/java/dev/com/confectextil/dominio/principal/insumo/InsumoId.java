package dev.com.confectextil.dominio.principal.insumo;

public record InsumoId(String valor) {
    public static InsumoId novo(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("ID do Insumo não pode ser nulo ou vazio.");
        }
        return new InsumoId(valor);
    }

    public InsumoId() {
        this(java.util.UUID.randomUUID().toString());
    }
}