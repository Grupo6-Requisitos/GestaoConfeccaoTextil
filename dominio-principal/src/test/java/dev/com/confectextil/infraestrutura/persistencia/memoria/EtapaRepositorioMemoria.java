package dev.com.confectextil.infraestrutura.persistencia.memoria;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaId;
import dev.com.confectextil.dominio.principal.etapa.EtapaRepository;

public class EtapaRepositorioMemoria implements EtapaRepository {
    
    private final Map<EtapaId, Etapa> armazenamento = new HashMap<>();

    @Override
    public void salvar(Etapa etapa) {
        if (etapa == null) {
            throw new IllegalArgumentException("Etapa não pode ser nulo");
        }
        armazenamento.put(etapa.getId(), etapa);
    }

    @Override
    public Etapa editar(Etapa etapa) {
        if (etapa == null) {
            throw new IllegalArgumentException("Etapa não pode ser nulo");
        }
        armazenamento.put(etapa.getId(), etapa);
        return etapa;
    }

    @Override
    public Optional<Etapa> buscarPorId(EtapaId etapaId) {
        return Optional.ofNullable(armazenamento.get(etapaId));
    }

    public boolean existsById(EtapaId etapaId) {
        return armazenamento.containsKey(etapaId);
    }

    public void limpar() {
        armazenamento.clear();
    }

    @Override
    public void excluir(EtapaId etapaId){
        armazenamento.remove(etapaId);
    }
}