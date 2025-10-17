package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.parceiro.*;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ParceiroRepositorioMock;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

import static org.junit.jupiter.api.Assertions.*;

public class CadastrarParceiroSteps {

    private ParceiroRepositorio repositorio;
    private ParceiroService service;
    private String id;
    private String nome;
    private String telefone;
    private Exception excecaoCapturada;

    @Before
    public void setup() {
        repositorio = new ParceiroRepositorioMock();
        service = new ParceiroService(repositorio);
        excecaoCapturada = null;
    }

    @Dado("que eu tenho um ID {string}, nome {string} e telefone {string}")
    public void queEuTenhoUmIdNomeETelefone(String id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    @Quando("eu cadastrar o parceiro")
    public void euCadastrarOParceiro() {
        try {
            service.cadastrar(id, nome, telefone);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o parceiro com ID {string} deve estar registrado no sistema")
    public void oParceiroComIdDeveEstarRegistradoNoSistema(String idEsperado) {
        Parceiro parceiro = repositorio.buscarPorId(new ParceiroId(idEsperado));
        assertNotNull(parceiro, "Parceiro nao foi salvo no repositorio");
    }

    @Entao("o nome do parceiro deve ser {string}")
    public void oNomeDoParceiroDeveSer(String nomeEsperado) {
        Parceiro parceiro = repositorio.buscarPorId(new ParceiroId(id));
        assertEquals(nomeEsperado, parceiro.getNome());
    }
    @Entao("o telefone do parceiro deve ser {string}")
    public void oTelefoneDoParceiroDeveSer(String telefoneEsperado) {
        Parceiro parceiro = repositorio.buscarPorId(new ParceiroId(id));
        assertEquals(telefoneEsperado, parceiro.getTelefone());
    }
    @Entao("uma excecao deve ser lancada com a mensagem {string}")
    public void umaExcecaoDeveSerLancadaComAMensagem(String mensagemEsperada) {
        assertNotNull(excecaoCapturada, "Nenhuma exceção foi lançada");
        assertEquals(mensagemEsperada, excecaoCapturada.getMessage());
    }
}
