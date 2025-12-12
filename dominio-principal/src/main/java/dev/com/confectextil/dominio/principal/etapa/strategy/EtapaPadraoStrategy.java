package dev.com.confectextil.dominio.principal.etapa.strategy;

import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaStrategy;


public class EtapaPadraoStrategy implements EtapaStrategy {

    @Override
    public boolean seAplica(String tipo) {
        return "PADRAO".equalsIgnoreCase(tipo);
    }

    @Override
    public void processarRegras(Etapa etapa) {
        etapa.alterarNome("PAD-" + etapa.getNome());
    }
}
