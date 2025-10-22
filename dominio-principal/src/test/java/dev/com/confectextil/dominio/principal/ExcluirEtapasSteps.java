package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.etapa.EtapaId;
import dev.com.confectextil.dominio.principal.etapa.EtapaRepository;
import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.EtapaRepositorioMemoria;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import org.junit.jupiter.api.Assertions;
import java.util.Optional;

public class ExcluirEtapasSteps {
    private EtapaRepository repository;
    private EtapaService service;
    private String mensagemRetorno;
    private String idEmContexto;

    @Before
    public void setup(){
        repository = new EtapaRepositorioMemoria();
        service = new EtapaService(repository);
        idEmContexto = null;
        mensagemRetorno = null;
    }

    @Dado("que existe uma etapa cadastrada com ID {string}")
    public void excluirUmaEtapaExistente(String id){
        this.idEmContexto = id;
        Etapa etapa = new Etapa(EtapaId.novo(id), "Corte", 1);
        repository.salvar(etapa);
    }

    @Dado("que não existe uma etapa cadastrada com ID {string}")
    public void naoExisteEtapaCadastradaComId(String id){
        this.idEmContexto = id;
        Assertions.assertTrue(repository.buscarPorId(EtapaId.novo(id)).isEmpty());
    }

    @Quando("eu solicitar a exclusao da etapa com ID {string}")
    public void euSolicittarExclusao(String id){
        try {
            service.excluirEtapa(id);
            mensagemRetorno = "Etapa excluída com sucesso.";
        } catch (Exception e) {
            mensagemRetorno = e.getMessage();
        }
    }

    @Entao("o sistema deve remover a etapa do cadastro")
    public void removeEtapa() {
        Optional<Etapa> etapa = repository.buscarPorId(EtapaId.novo(idEmContexto));
        Assertions.assertTrue(etapa.isEmpty());
    }
    @Entao("o sistema deve informar {string}")
    public void sistemainforma(String mensagem){
        Assertions.assertEquals(mensagem, mensagemRetorno);
    }
}