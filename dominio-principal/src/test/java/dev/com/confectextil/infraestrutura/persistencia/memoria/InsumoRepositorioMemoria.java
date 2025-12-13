package dev.com.confectextil.infraestrutura.persistencia.memoria;

import dev.com.confectextil.dominio.principal.Insumo;
import dev.com.confectextil.dominio.principal.insumo.InsumoId;
import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InsumoRepositorioMemoria implements InsumoRepository {

    private final Map<String, Insumo> armazenamento = new HashMap<>();

    @Override
    public void salvar(Insumo insumo) {
        armazenamento.put(insumo.getReferencia(), insumo);
    }

    @Override
    public Optional<Insumo> buscarPorReferencia(String referencia) {
        return Optional.ofNullable(armazenamento.get(referencia));
    }

    @Override
    public Optional<Insumo> buscarPorId(InsumoId id) {
        if (id == null) {
            return Optional.empty();
        }
        return armazenamento.values().stream()
                .filter(i -> id.equals(i.getId()))
                .findFirst();
    }

    public void limpar() {
        armazenamento.clear();
    }
}
