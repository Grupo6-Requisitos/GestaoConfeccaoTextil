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

    public List<ModeloResumo> listarTodosResumo() {
        return repositorio.listarTodosResumo();
    }

    public Iterable<ModeloResumo> iterarTodosResumo() {
        return repositorio.iterarTodosResumo();
    }

    public ModeloResumo listarEspecificoResumo(String referencia) {
        return repositorio.listarEspecificoResumo(referencia);
    }
}