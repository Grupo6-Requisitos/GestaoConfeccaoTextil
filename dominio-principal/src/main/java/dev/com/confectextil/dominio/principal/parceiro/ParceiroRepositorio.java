package dev.com.confectextil.dominio.principal.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;
import java.util.List;
import java.util.Optional;

public interface ParceiroRepositorio {
    void salvar(Parceiro parceiro);
    Parceiro editar(Parceiro parceiro);
    Optional<Parceiro> buscarPorId(ParceiroId id);
    List<Parceiro> listarTodos();
}