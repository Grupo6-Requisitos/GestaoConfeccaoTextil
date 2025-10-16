package dev.com.linnea.dominio.principal.insumo;

import java.util.Optional;
import java.util.List;

public interface InsumoRepository {

    void salvar(Insumo insumo);

    Optional<Insumo> buscarPorId(InsumoId insumoId);

    Optional<Insumo> buscarPorReferencia(String referencia);
    
    List<Insumo> listarTodos();
}