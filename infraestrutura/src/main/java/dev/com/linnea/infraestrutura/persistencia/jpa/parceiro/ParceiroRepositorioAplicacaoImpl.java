package dev.com.linnea.infraestrutura.persistencia.jpa.parceiro;

import dev.com.linnea.aplicacao.principal.parceiro.ParceiroRepositorioAplicacao;
import dev.com.linnea.aplicacao.principal.parceiro.ParceiroResumo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParceiroRepositorioAplicacaoImpl implements ParceiroRepositorioAplicacao {
    private final ParceiroRepositorySpringData springData;

    public ParceiroRepositorioAplicacaoImpl(ParceiroRepositorySpringData springData) {
        this.springData = springData;
    }

    @Override
    public List<ParceiroResumo> listarTodosResumido() {
        return springData.findAllProjectedBy();
    }
}