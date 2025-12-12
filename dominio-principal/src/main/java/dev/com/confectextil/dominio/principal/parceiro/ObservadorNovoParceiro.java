package dev.com.confectextil.dominio.principal.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;

public interface ObservadorNovoParceiro {
    void executadoAposCadastro(Parceiro parceiroSalvo);
}