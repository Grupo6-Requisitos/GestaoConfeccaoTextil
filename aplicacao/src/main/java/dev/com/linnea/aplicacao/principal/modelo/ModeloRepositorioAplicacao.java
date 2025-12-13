package dev.com.linnea.aplicacao.principal.modelo;

import java.util.List;

public interface ModeloRepositorioAplicacao {
    List<ModeloResumo> listarTodosResumo();
    ModeloResumo listarEspecificoResumo(String referencia);
    Iterable<ModeloResumo> iterarTodosResumo();
}