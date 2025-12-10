package dev.com.linnea.infraestrutura.persistencia.jpa.modelo;

import dev.com.linnea.aplicacao.principal.modelo.ModeloRepositorioAplicacao;
import dev.com.linnea.aplicacao.principal.modelo.ModeloResumo;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ModeloRepositorioAplicacaoImpl implements ModeloRepositorioAplicacao {

    private final ModeloRepositorySpringData springData;

    public ModeloRepositorioAplicacaoImpl(ModeloRepositorySpringData springData) {
        this.springData = springData;
    }

    @Override
    public List<ModeloResumo> listarResumo() {
        return springData.findAllProjectedBy();
    }
}
