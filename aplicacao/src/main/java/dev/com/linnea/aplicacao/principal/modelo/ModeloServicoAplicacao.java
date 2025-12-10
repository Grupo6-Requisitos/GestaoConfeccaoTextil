package dev.com.linnea.aplicacao.principal.modelo;

import java.util.List;

public class ModeloServicoAplicacao {

    private final ModeloRepositorioAplicacao repositorio;

    public ModeloServicoAplicacao(ModeloRepositorioAplicacao repositorio) {

        if(repositorio == null) {
            throw new IllegalArgumentException("Repositorio não pode ser nulo ou vazio.");
        }
        this.repositorio = repositorio;
    }

    public List<ModeloResumo> listarModelosResumo() {
        return repositorio.listarResumo();
    }
}
