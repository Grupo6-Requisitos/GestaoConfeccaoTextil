package dev.com.confectextil.dominio.principal.etapa.strategy;
import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaStrategy;

public class EtapaProducaoStrategy implements EtapaStrategy {

    @Override
    public boolean seAplica(String tipo) {
        return "PRODUCAO".equalsIgnoreCase(tipo);
    }

    @Override
    public void processarRegras(Etapa etapa) {
        // regra específica para produção
        if (etapa.getOrdem() < 1) {
            throw new IllegalArgumentException("Ordem inválida para etapa de produção");
        }
        etapa.alterarNome("PROD-" + etapa.getNome());
    }
}


