package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.parceiro.*;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ParceiroRepositorioMemoria;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParceiroStepDefinitions {

    private ParceiroRepositorio repositorio;
    private ParceiroService service;
    private Exception excecaoCapturada;
    private String idEmContexto;
    private String nomeEmContexto;
    private String telefoneEmContexto;
    private List<Parceiro> listaDeParceiros;

    @Before
    public void setup() {
        repositorio = new ParceiroRepositorioMemoria();
        service = new ParceiroService(repositorio);
        excecaoCapturada = null;
        idEmContexto = null;
        listaDeParceiros = null;
        nomeEmContexto = null;
        telefoneEmContexto = null;
    }
    //Cadastrar
    @Dado("que eu tenho um ID {string}, nome {string} e telefone {string}")
    public void queEuTenhoUmIdNomeETelefone(String id, String nome, String telefone) {
        this.idEmContexto = id;
        this.nomeEmContexto = nome;
        this.telefoneEmContexto = telefone;
    }

    @Quando("eu cadastrar o parceiro")
    public void euCadastrarOParceiro() {
        try {
            service.cadastrar(this.idEmContexto, this.nomeEmContexto, this.telefoneEmContexto);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Entao("o parceiro com ID {string} deve estar registrado no sistema")
    public void oParceiroComIdDeveEstarRegistradoNoSistema(String idEsperado) {
        assertTrue(repositorio.buscarPorId(new ParceiroId(idEsperado)).isPresent(), "Parceiro nao foi salvo no repositorio");
    }

    @Entao("uma excecao deve ser lancada com a mensagem {string}")
    public void uma_excecao_deve_ser_lancada_com_a_mensagem(String mensagemEsperada) {
        assertNotNull(excecaoCapturada, "Nenhuma exceção foi lançada, mas era esperada.");
        assertTrue(excecaoCapturada.getMessage().contains(mensagemEsperada));
    }

    @Entao("o nome do parceiro deve ser {string}")
    public void o_nome_do_parceiro_deve_ser(String nomeEsperado) {
        Parceiro parceiro = repositorio.buscarPorId(new ParceiroId(idEmContexto))
                .orElseThrow(() -> new AssertionError("Parceiro não encontrado no repositório para verificação."));
        assertEquals(nomeEsperado, parceiro.getNome());
    }

    @Entao("o telefone do parceiro deve ser {string}")
    public void o_telefone_do_parceiro_deve_ser(String telefoneEsperado) {
        Parceiro parceiro = repositorio.buscarPorId(new ParceiroId(idEmContexto))
                .orElseThrow(() -> new AssertionError("Parceiro não encontrado no repositório para verificação."));
        assertEquals(telefoneEsperado, parceiro.getTelefone());
    }

    //Visualizar
    @Dado("que existem parceiros cadastrados no sistema")
    public void queExistemParceirosCadastradosNoSistema() {
        service.cadastrar("P001", "Thiago", "81999998888");
        service.cadastrar("P002", "Maria", "81988887777");
    }

    @Dado("que nao existem parceiros cadastrados no sistema")
    public void queNaoExistemParceirosCadastradosNoSistema(){
        assertTrue(service.listarTodos().isEmpty());
    }

    @Quando("solicito para listar todos os parceiros")
    public void solicitoParaListarTodosOsParceiros(){
        this.listaDeParceiros = service.listarTodos();
    }

    @Entao("o sistema deve exibir a lista de parceiros com seus nomes e telefones")
    public void oSistemaDeveExibirAListaDeParceiros() {
        assertNotNull(listaDeParceiros);
        assertEquals(2, listaDeParceiros.size());
        assertTrue(listaDeParceiros.stream().anyMatch(p -> p.getNome().equals("Thiago")));
        assertTrue(listaDeParceiros.stream().anyMatch(p -> p.getNome().equals("Maria")));
    }

    @Entao("o sistema deve informar que nao existem parceiros cadastrados")
    public void oSistemaDeveInformarQueNaoExistemParceirosCadastrados(){
        assertNotNull(listaDeParceiros);
        assertTrue(listaDeParceiros.isEmpty());
    }



    @Dado("que existe um Parceiro cadastrado com id {string}, nome {string} e telefone {string}")
    public void que_existe_um_parceiro_cadastrado_com_id_nome_e_telefone(String id, String nome, String telefone) {
        this.idEmContexto = id;
        service.cadastrar(id, nome, telefone);
    }

    @Quando("eu edito o Parceiro {string} alterando:")
    public void eu_edito_o_parceiro_alterando(String id, DataTable dataTable) {
        this.idEmContexto = id;
        Map<String, String> alteracoes = dataTable.asMap();
        try {
            service.editar(id, alteracoes.get("nome"), alteracoes.get("telefone"));
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }
    
    @Quando("eu tento editar o Parceiro {string} alterando:")
    public void eu_tento_editar_o_parceiro_alterando(String id, DataTable dataTable) {
        this.idEmContexto = id;
        Map<String, String> alteracoes = dataTable.asMap();
        try {
            service.editar(id, alteracoes.get("nome"), alteracoes.get("telefone"));
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Entao("o parceiro com id {string} deve ser atualizado com sucesso")
    public void o_parceiro_com_id_deve_ser_atualizado_com_sucesso(String id) {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(new ParceiroId(id)).isPresent());
    }
    
    @Dado("que não existe um Parceiro com id {string}")
    public void que_nao_existe_um_parceiro_com_id(String id) {
        this.idEmContexto = id;
        assertFalse(repositorio.buscarPorId(new ParceiroId(id)).isPresent());
    }

    @Quando("eu solicitar os detalhes do parceiro com id {string}")
    public void eu_solicitar_os_detalhes_do_parceiro_com_id(String id) {
        this.idEmContexto = id;
        try {
            service.buscarPorId(id);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Quando("eu tento solicitar os detalhes do parceiro com id {string}")
    public void eu_tento_solicitar_os_detalhes_do_parceiro_com_id(String id) {
        this.idEmContexto = id;
        try {
            service.buscarPorId(id);
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Entao("eu devo receber os detalhes do parceiro")
    public void eu_devo_receber_os_detalhes_do_parceiro() {
        assertNull(excecaoCapturada);
        assertTrue(repositorio.buscarPorId(new ParceiroId(idEmContexto)).isPresent());
    }
}