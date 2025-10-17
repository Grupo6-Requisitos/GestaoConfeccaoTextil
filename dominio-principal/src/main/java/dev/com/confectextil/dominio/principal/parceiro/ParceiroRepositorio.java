package dev.com.confectextil.dominio.principal.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;

import java.util.List;

public interface ParceiroRepositorio {
    void salvar(Parceiro parceiro);
    Parceiro buscarPorId(ParceiroId id);
    List<Parceiro> listarTodos();
}