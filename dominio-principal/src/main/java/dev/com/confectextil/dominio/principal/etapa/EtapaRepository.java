package dev.com.confectextil.dominio.principal.etapa;

import dev.com.confectextil.dominio.principal.Etapa;
import java.util.Optional;


public interface EtapaRepository {
    
    void salvar(Etapa etapa);

    Etapa editar(Etapa etapa);

    Optional<Etapa> buscarPorId(EtapaId etapaId);
    
}
