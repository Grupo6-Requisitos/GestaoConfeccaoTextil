package dev.com.confectextil.dominio.principal.fabrico;

import dev.com.confectextil.dominio.principal.Fabrico;
import java.util.Optional;

public interface FabricoRepository {

    void salvar(Fabrico fabrico);

    Fabrico editar(Fabrico fabrico);

    Optional<Fabrico> buscarPorId(FabricoId fabricoId);

    Optional<Fabrico> buscarPorCnpj(String cnpj);
}