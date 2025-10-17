package dev.com.confectextil.dominio.principal.insumo;

import java.util.Optional;

import dev.com.confectextil.dominio.principal.Insumo;

public interface InsumoRepository {
    void salvar(Insumo insumo);
    Optional<Insumo> buscarPorReferencia(String referencia);
}