package dev.com.linnea.aplicacao.principal.modelo;

public interface ModeloResumo {
    String getId();

	String getNome();

    String getReferencia();

    String getImagemUrl();

    java.util.List<InsumoPadraoResumo> getInsumosPadrao();
}