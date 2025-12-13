package dev.com.confectextil.dominio.principal.modelo;

public record ModeloId(String valor) {
    public static ModeloId novo(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("ID do Modelo não pode ser nulo ou vazio.");
        }
        return new ModeloId(valor);
    }

    public ModeloId() {
        this(java.util.UUID.randomUUID().toString());
    }

    public String getValor() {
        return valor;
    }
}