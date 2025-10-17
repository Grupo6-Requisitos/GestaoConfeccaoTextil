package dev.com.confectextil.infraestrutura.persistencia.memoria;

import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModeloRepositorioMemoria implements ModeloRepository {

    private final Map<String, Modelo> armazenamento = new HashMap<>();

    @Override
    public void salvar(Modelo modelo) {
        armazenamento.put(modelo.getReferencia(), modelo);
    }

    @Override
    public Optional<Modelo> buscarPorReferencia(String referencia) {
        return Optional.ofNullable(armazenamento.get(referencia));
    }

    @Override
    public List<Modelo> listarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    public void limpar() {
        armazenamento.clear();
    }
}