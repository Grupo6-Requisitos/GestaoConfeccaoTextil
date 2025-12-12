package dev.com.linnea.aplicacao.principal.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParceiroServicoAplicacao {
    private final ParceiroService parceiroService;
    private final ParceiroRepositorioAplicacao parceiroRepoAplicacao;

    public ParceiroServicoAplicacao(ParceiroService parceiroService,ParceiroRepositorioAplicacao parceiroRepoAplicacao) {
        this.parceiroService = parceiroService;
        this.parceiroRepoAplicacao = parceiroRepoAplicacao;
    }

    public List<Parceiro> listarTodos() {
        return parceiroService.listarTodos();
    }

    public List<ParceiroResumo> listarResumo() {
        return parceiroRepoAplicacao.listarTodosResumido();
    }
}
