package dev.com.confectextil.infraestrutura.persistencia.memoria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.com.confectextil.dominio.principal.Fabrico;
import dev.com.confectextil.dominio.principal.fabrico.FabricoId;
import dev.com.confectextil.dominio.principal.fabrico.FabricoRepository;

public class FabricoRepositorioMemoria implements FabricoRepository {

    private final Map<FabricoId, Fabrico> armazenamento = new HashMap<>();

     private final List<Fabrico> fabricos = new ArrayList<>();

    @Override
    public void salvar(Fabrico fabrico) {
        if (fabrico == null) {
            throw new IllegalArgumentException("Fabrico não pode ser nulo");
        }
        armazenamento.put(fabrico.getId(), fabrico);
    }

    @Override
    public Fabrico editar(Fabrico fabrico) {
        if (fabrico == null) {
            throw new IllegalArgumentException("Fabrico não pode ser nulo");
        }
        armazenamento.put(fabrico.getId(), fabrico);
        return fabrico;
    }

     @Override
    public Optional<Fabrico> buscarPorCnpj(String cnpj) {
        return fabricos.stream()
                       .filter(f -> f.getCnpj().equals(cnpj))
                       .findFirst();
    }

    @Override
    public Optional<Fabrico> buscarPorId(FabricoId fabricoId) {
        return Optional.ofNullable(armazenamento.get(fabricoId));
    }

    public boolean existsById(FabricoId fabricoId) {
        return armazenamento.containsKey(fabricoId);
    }

    public void limpar() {
        armazenamento.clear();
    }
}
