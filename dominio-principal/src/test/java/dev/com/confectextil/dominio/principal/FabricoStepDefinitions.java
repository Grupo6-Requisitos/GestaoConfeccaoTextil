package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.fabrico.FabricoRepository;
import dev.com.confectextil.dominio.principal.fabrico.FabricoService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.FabricoRepositorioMemoria;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class FabricoStepDefinitions {

    private FabricoService fabricoService;
    private FabricoRepository fabricoRepository;
    private Fabrico fabricoCadastrado;
    private Exception excecaoCapturada;
    private Map<String, String> dadosFabrico;

    @Before
    public void setUp() {
        // Inicializa o repositório e o serviço antes de cada cenário
        this.fabricoRepository = new FabricoRepositorioMemoria();
        this.fabricoService = new FabricoService(fabricoRepository);
        this.excecaoCapturada = null;
    }

    @Dado("que o sistema está ativo no ano de {int}")
    public void que_o_sistema_esta_ativo_no_ano_de(Integer ano) {
        System.out.println("Sistema inicializado para o ano: " + ano);
    }

    @Dado("que não existe um Fabrico com id {string}")
    public void que_nao_existe_um_fabrico_com_id(String fabricoId) {
        assertFalse(fabricoService.existe(fabricoId), 
            "O Fabrico com ID " + fabricoId + " não deveria existir");
    }

    @Dado("que já existe um Fabrico cadastrado com id {string}")
    public void que_ja_existe_um_fabrico_cadastrado_com_id(String fabricoId) {
        // Cadastra um Fabrico para simular que já existe
        fabricoService.cadastrarFabrico(
            fabricoId, 
            "Fabrico Existente", 
            "11.222.333/0001-44"
        );
        assertTrue(fabricoService.existe(fabricoId), 
            "O Fabrico com ID " + fabricoId + " deveria existir");
    }

    @Dado("que existe um Fabrico com os seguintes dados:")
    public void que_existe_um_fabrico_com_os_seguintes_dados(DataTable dataTable) {
        Map<String, String> dados = dataTable.asMap(String.class, String.class);
        
        this.fabricoCadastrado = fabricoService.cadastrarFabrico(
            dados.get("fabricoId"),
            dados.get("nomeFantasia"),
            dados.get("cnpj")
        );
        
        assertNotNull(this.fabricoCadastrado, "O Fabrico deveria ter sido cadastrado");
        assertTrue(fabricoService.existe(dados.get("fabricoId")), 
            "O Fabrico deveria existir no repositório");
    }

    @Quando("eu cadastro um novo Fabrico com os seguintes dados:")
    public void eu_cadastro_um_novo_fabrico_com_os_seguintes_dados(DataTable dataTable) {
        this.dadosFabrico = dataTable.asMap(String.class, String.class);
        
        try {
            this.fabricoCadastrado = fabricoService.cadastrarFabrico(
                dadosFabrico.get("fabricoId"),
                dadosFabrico.get("nomeFantasia"),
                dadosFabrico.get("cnpj")
            );
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Quando("eu tento cadastrar outro Fabrico com id {string}")
    public void eu_tento_cadastrar_outro_fabrico_com_id(String fabricoId) {
        try {
            fabricoService.cadastrarFabrico(
                fabricoId,
                "Novo Fabrico",
                "55.666.777/0001-88"
            );
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Quando("eu edito o Fabrico {string} alterando:")
    public void eu_edito_o_fabrico_alterando(String fabricoId, DataTable dataTable) {
        Map<String, String> alteracoes = dataTable.asMap(String.class, String.class);
        
        try {
            this.fabricoCadastrado = fabricoService.editarFabrico(
                fabricoId,
                alteracoes.get("nomeFantasia"),
                alteracoes.get("cnpj")
            );
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Quando("eu tento editar o Fabrico {string} alterando:")
    public void eu_tento_editar_o_fabrico_alterando(String fabricoId, DataTable dataTable) {
        Map<String, String> alteracoes = dataTable.asMap(String.class, String.class);
        
        try {
            fabricoService.editarFabrico(
                fabricoId,
                alteracoes.get("nomeFantasia"),
                alteracoes.get("cnpj")
            );
        } catch (Exception e) {
            this.excecaoCapturada = e;
        }
    }

    @Então("o Fabrico {string} deve ser salvo com sucesso")
    public void o_fabrico_deve_ser_salvo_com_sucesso(String fabricoId) {
        assertNotNull(this.fabricoCadastrado, "O Fabrico deveria ter sido cadastrado");
        assertEquals(fabricoId, this.fabricoCadastrado.getId().toString(), 
            "O ID do Fabrico deveria ser " + fabricoId);
        assertTrue(fabricoService.existe(fabricoId), 
            "O Fabrico deveria existir no repositório");
    }

    @Então("o CNPJ deve ser {string}")
    public void o_cnpj_deve_ser(String cnpjEsperado) {
        assertNotNull(this.fabricoCadastrado, "O Fabrico deveria existir");
        assertEquals(cnpjEsperado, this.fabricoCadastrado.getCnpj(), 
            "O CNPJ deveria ser " + cnpjEsperado);
    }

    @Então("o sistema deve rejeitar o cadastro por CNPJ inválido")
    public void o_sistema_deve_rejeitar_o_cadastro_por_cnpj_invalido() {
        assertNotNull(this.excecaoCapturada, 
            "Deveria ter lançado uma exceção para CNPJ inválido");
        assertTrue(this.excecaoCapturada instanceof IllegalArgumentException, 
            "A exceção deveria ser IllegalArgumentException");
        assertTrue(this.excecaoCapturada.getMessage().contains("CNPJ inválido"), 
            "A mensagem deveria indicar CNPJ inválido");
    }

    @Então("o sistema deve rejeitar o cadastro")
    public void o_sistema_deve_rejeitar_o_cadastro() {
        assertNotNull(this.excecaoCapturada, 
            "Deveria ter lançado uma exceção ao tentar cadastrar ID duplicado");
    }

    @Então("exibir a mensagem de erro {string}")
    public void exibir_a_mensagem_de_erro(String mensagemEsperada) {
        assertNotNull(this.excecaoCapturada, "Deveria ter lançado uma exceção");
        assertEquals(mensagemEsperada, this.excecaoCapturada.getMessage(), 
            "A mensagem de erro deveria ser: " + mensagemEsperada);
    }

    @Então("o Fabrico deve ter o nome {string}")
    public void o_fabrico_deve_ter_o_nome(String nomeEsperado) {
        assertNotNull(this.fabricoCadastrado, "O Fabrico deveria existir");
        assertEquals(nomeEsperado, this.fabricoCadastrado.getNomeFantasia(), 
            "O nome fantasia deveria ser " + nomeEsperado);
    }

    @Então("o CNPJ deve permanecer {string}")
    public void o_cnpj_deve_permanecer(String cnpjEsperado) {
        assertNotNull(this.fabricoCadastrado, "O Fabrico deveria existir");
        assertEquals(cnpjEsperado, this.fabricoCadastrado.getCnpj(), 
            "O CNPJ deveria permanecer " + cnpjEsperado);
    }

    @Então("o sistema deve exibir a mensagem de erro {string}")
    public void o_sistema_deve_exibir_a_mensagem_de_erro(String mensagemEsperada) {
        assertNotNull(this.excecaoCapturada, "Deveria ter lançado uma exceção");
        assertEquals(mensagemEsperada, this.excecaoCapturada.getMessage(), 
            "A mensagem de erro deveria ser: " + mensagemEsperada);
    }
}
