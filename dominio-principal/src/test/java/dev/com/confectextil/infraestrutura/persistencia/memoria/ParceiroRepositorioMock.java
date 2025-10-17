package dev.com.confectextil.infraestrutura.persistencia.memoria;

import dev.com.confectextil.dominio.principal.parceiro.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroId;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParceiroRepositorioMock implements ParceiroRepositorio {
    private final Map<String, Parceiro> parceiros = new HashMap<>();

    @Override
    public void salvar(Parceiro parceiro) {
        parceiros.put(parceiro.getId().getId(), parceiro);
    }
    @Override
    public Parceiro buscarPorId(ParceiroId id) {
        return parceiros.get(id.getId());
    }
    @Override
    public List<Parceiro> listarTodos() {
        return new ArrayList<>(parceiros.values());
    }
}