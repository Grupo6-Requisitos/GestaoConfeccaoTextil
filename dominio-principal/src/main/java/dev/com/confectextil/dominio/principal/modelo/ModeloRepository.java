package dev.com.confectextil.dominio.principal.modelo;

import java.util.List;
import java.util.Optional;

import dev.com.confectextil.dominio.principal.Modelo;

public interface ModeloRepository {
    void salvar(Modelo modelo);
    Optional<Modelo> buscarPorReferencia(String referencia);
    List<Modelo> listarTodos();
}