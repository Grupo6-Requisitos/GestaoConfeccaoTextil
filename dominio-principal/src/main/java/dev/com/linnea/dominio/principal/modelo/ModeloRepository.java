package dev.com.linnea.dominio.principal.modelo;

import java.util.List;
import java.util.Optional;

public interface ModeloRepository {

    void salvar(Modelo modelo);

    Optional<Modelo> buscarPorReferencia(String referencia);
    
    Optional<Modelo> buscarPorId(ModeloId modeloId);

    List<Modelo> listarTodos();
}