package dev.com.confectextil.dominio.principal.etapa;

import dev.com.confectextil.dominio.principal.Etapa;

public interface EtapaStrategy {
    boolean seAplica(String tipo);
    void processarRegras(Etapa etapa);
}
