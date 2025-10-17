package dev.com.confectextil.dominio.principal.parceiro;

public class ParceiroService {
    private final ParceiroRepositorio repositorio;

    public ParceiroService (ParceiroRepositorio repositorio){
        this.repositorio = repositorio;
    }
    public void cadastrar(String id, String nome, String telefone){
        ParceiroId parceiroId = new ParceiroId(id);
        Parceiro parceiro = new Parceiro(parceiroId, nome, telefone);
        repositorio.salvar(parceiro);
    }
}