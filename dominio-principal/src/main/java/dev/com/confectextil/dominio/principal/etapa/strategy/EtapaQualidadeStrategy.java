package dev.com.confectextil.dominio.principal.etapa.strategy;
import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaStrategy;

public class EtapaQualidadeStrategy implements EtapaStrategy {

    @Override
    public boolean seAplica(String tipo) {
        return "QUALIDADE".equalsIgnoreCase(tipo);
    }

    @Override
    public void processarRegras(Etapa etapa) {
        // regra específica para qualidade
        etapa.alterarNome(etapa.getNome().toUpperCase());
    }
}