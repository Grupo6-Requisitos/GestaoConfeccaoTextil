package dev.com.confectextil.dominio.principal.parceiro;

import dev.com.confectextil.dominio.principal.Parceiro;
import java.util.List;

public class ParceiroService {
    private final ParceiroRepositorio repositorio;
    private final List<ObservadorNovoParceiro> observadores;
    public ParceiroService(ParceiroRepositorio repositorio, List<ObservadorNovoParceiro> observadores){
        this.repositorio = repositorio;
        this.observadores = observadores;
    }

    public void cadastrar(String id, String nome, String telefone){
        ParceiroId parceiroId = new ParceiroId(id);
        if (repositorio.buscarPorId(parceiroId).isPresent()) {
            throw new IllegalArgumentException("Já existe um parceiro com o identificador fornecido.");
        }
        Parceiro parceiro = new Parceiro(parceiroId, nome, telefone);
        repositorio.salvar(parceiro);
        notificarObservadores(parceiro);
    }

    private void notificarObservadores(Parceiro parceiro) {
        for (ObservadorNovoParceiro obs : observadores) {
            obs.executadoAposCadastro(parceiro);
        }
    }

    public Parceiro editar(String id, String novoNome, String novoTelefone) {
        ParceiroId parceiroId = new ParceiroId(id);
        Parceiro parceiro = repositorio.buscarPorId(parceiroId)
            .orElseThrow(() -> new IllegalArgumentException("Parceiro não encontrado"));

        if (novoNome != null && !novoNome.isBlank()) {
            parceiro.alterarNome(novoNome);
        }
        if (novoTelefone != null && !novoTelefone.isBlank()) {
            parceiro.alterarTelefone(novoTelefone);
        }

        return repositorio.editar(parceiro);
    }

    public Parceiro buscarPorId(String id) {
        ParceiroId parceiroId = new ParceiroId(id);
        return repositorio.buscarPorId(parceiroId)
            .orElseThrow(() -> new IllegalArgumentException("Parceiro não encontrado"));
    }

    public List<Parceiro> listarTodos(){
        return repositorio.listarTodos();
    }
}