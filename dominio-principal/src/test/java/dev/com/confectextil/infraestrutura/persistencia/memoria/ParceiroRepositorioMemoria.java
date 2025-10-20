package dev.com.confectextil.infraestrutura.persistencia.memoria;

import dev.com.confectextil.dominio.principal.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroId;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParceiroRepositorioMemoria implements ParceiroRepositorio {

    private final Map<ParceiroId, Parceiro> armazenamento = new HashMap<>();

    @Override
    public void salvar(Parceiro parceiro) {
        armazenamento.put(parceiro.getId(), parceiro);
    }

    @Override
    public Parceiro editar(Parceiro parceiro) {
        armazenamento.put(parceiro.getId(), parceiro);
        return parceiro;
    }

    @Override
    public Optional<Parceiro> buscarPorId(ParceiroId id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    @Override
    public List<Parceiro> listarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    public void limpar() {
        armazenamento.clear();
    }
}