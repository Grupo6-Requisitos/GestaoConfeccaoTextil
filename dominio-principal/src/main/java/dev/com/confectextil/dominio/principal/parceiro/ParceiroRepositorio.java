package dev.com.confectextil.dominio.principal.parceiro;

import java.util.List;

public interface ParceiroRepositorio {
    void salvar(Parceiro parceiro);
    Parceiro buscarPorId(ParceiroId id);
    List<Parceiro> listarTodos();
}