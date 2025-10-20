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
    private Parceiro parceiroSalvo;
    private List<Parceiro> listaDeParceiros;
    private String idCadastro, nomeCadastro, telefoneCadastro;

    @Before
    public void setup() {
        repositorio = new ParceiroRepositorioMemoria();
        service = new ParceiroService(repositorio);
        excecaoCapturada = null;
        parceiroSalvo = null;
        listaDeParceiros = null;
    }

    @Dado("que eu tenho um ID {string}, nome {string} e telefone {string}")
    public void queEuTenhoUmIdNomeETelefone(String id, String nome, String telefone) {
        this.idCadastro = id;
        this.nomeCadastro = nome;
        this.telefoneCadastro = telefone;
    }

    @Quando("eu cadastrar o parceiro")
    public void euCadastrarOParceiro() {
        try {
            service.cadastrar(idCadastro, nomeCadastro, telefoneCadastro);
        } catch (Exception e) {
            excecaoCapturada = e;
        }
    }

    @Entao("o parceiro com ID {string} deve estar registrado no sistema")
    public void oParceiroComIdDeveEstarRegistradoNoSistema(String idEsperado) {
        this.parceiroSalvo = repositorio.buscarPorId(new ParceiroId(idEsperado)).orElse(null);
        assertNotNull(parceiroSalvo, "Parceiro nao foi salvo no repositorio");
    }

    @Dado("que existe um Parceiro cadastrado com id {string}, nome {string} e telefone {string}")
    public void que_existe_um_parceiro_cadastrado_com_id_nome_e_telefone(String id, String nome, String telefone) {
        service.cadastrar(id, nome, telefone);
    }

    @Quando("eu edito o Parceiro {string} alterando:")
    public void eu_edito_o_parceiro_alterando(String id, DataTable dataTable) {
        Map<String, String> alteracoes = dataTable.asMap();
        try {
            this.parceiroSalvo = service.editar(id, alteracoes.get("nome"), alteracoes.get("telefone"));
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Quando("eu tento editar o Parceiro {string} alterando:")
    public void eu_tento_editar_o_parceiro_alterando(String id, DataTable dataTable) {
        Map<String, String> alteracoes = dataTable.asMap();
        try {
            this.parceiroSalvo = service.editar(id, alteracoes.get("nome"), alteracoes.get("telefone"));
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Entao("o parceiro com id {string} deve ser atualizado com sucesso")
    public void o_parceiro_com_id_deve_ser_atualizado_com_sucesso(String id) {
        assertNull(excecaoCapturada, "Uma exceção foi lançada inesperadamente.");
        Parceiro parceiroAtualizado = repositorio.buscarPorId(new ParceiroId(id)).orElse(null);
        assertNotNull(parceiroAtualizado, "Parceiro não encontrado após a edição.");
        this.parceiroSalvo = parceiroAtualizado;
    }

    @Dado("que não existe um Parceiro com id {string}")
    public void que_nao_existe_um_parceiro_com_id(String id) {
        assertFalse(repositorio.buscarPorId(new ParceiroId(id)).isPresent());
    }

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
        assertEquals(2, listaDeParceiros.size());
        assertTrue(listaDeParceiros.stream().anyMatch(p -> p.getNome().equals("Thiago")), "Parceiro 'Thiago' não encontrado na lista.");
        assertTrue(listaDeParceiros.stream().anyMatch(p -> p.getNome().equals("Maria")), "Parceiro 'Maria' não encontrado na lista.");
    }

    @Entao("o sistema deve informar que nao existem parceiros cadastrados")
    public void oSistemaDeveInformarQueNaoExistemParceirosCadastrados(){
        assertTrue(listaDeParceiros.isEmpty(), "A lista de parceiros deveria estar vazia.");
    }

    @Entao("o nome do parceiro deve ser {string}")
    public void o_nome_do_parceiro_deve_ser(String nomeEsperado) {
        assertNotNull(parceiroSalvo, "O parceiro precisa ser verificado primeiro.");
        assertEquals(nomeEsperado, parceiroSalvo.getNome());
    }

    @Entao("o telefone do parceiro deve ser {string}")
    public void o_telefone_do_parceiro_deve_ser(String telefoneEsperado) {
        assertNotNull(parceiroSalvo, "O parceiro precisa ser verificado primeiro.");
        assertEquals(telefoneEsperado, parceiroSalvo.getTelefone());
    }

    @Entao("uma excecao deve ser lancada com a mensagem {string}")
    public void uma_excecao_deve_ser_lancada_com_a_mensagem(String mensagemEsperada) {
        assertNotNull(excecaoCapturada, "Nenhuma exceção foi lançada, mas era esperada.");
        assertTrue(excecaoCapturada.getMessage().contains(mensagemEsperada));
    }
}