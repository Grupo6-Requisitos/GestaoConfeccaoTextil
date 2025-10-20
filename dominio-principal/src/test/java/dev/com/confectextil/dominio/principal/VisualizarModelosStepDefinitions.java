package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.InsumoRepositorioMemoria;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizarModelosStepDefinitions {

    private ModeloRepository modeloRepository;
    private InsumoRepository insumoRepository; // Adicionado para um setup mais robusto
    private ModeloService modeloService;
    private List<Modelo> listaDeModelosResultante;

    @Before
    public void setUp() {
        // Criamos repositórios limpos para cada cenário
        this.modeloRepository = new ModeloRepositorioMemoria();
        this.insumoRepository = new InsumoRepositorioMemoria(); // Usamos um repo real em vez de nulo
        this.modeloService = new ModeloService(modeloRepository, insumoRepository);
        this.listaDeModelosResultante = Collections.emptyList();
    }

    @Dado("que existem os seguintes modelos já cadastrados no sistema:")
    public void que_existem_os_seguintes_modelos_ja_cadastrados_no_sistema(DataTable dataTable) {
        List<Map<String, String>> modelos = dataTable.asMaps();
        for (Map<String, String> modelo : modelos) {
            // Usamos o serviço para cadastrar os modelos de pré-condição
            // Passamos uma lista vazia de insumos, pois não são necessários para este cenário
            modeloService.cadastrarModelo(
                modelo.get("referencia"),
                modelo.get("nome"),
                null, // imagemUrl não é necessária aqui
                Collections.emptyList()
            );
        }
    }

    @Quando("eu solicitar a lista de todos os modelos")
    public void eu_solicitar_a_lista_de_todos_os_modelos() {
        // Esta é a ação que estamos a testar: chamar o método do serviço
        this.listaDeModelosResultante = modeloService.listarTodos();
    }

    @Entao("eu devo receber uma lista com {int} modelos")
    public void eu_devo_receber_uma_lista_com_modelos(Integer quantidade) {
        // Verificamos se o resultado da ação está correto
        assertNotNull(this.listaDeModelosResultante);
        assertEquals(quantidade, this.listaDeModelosResultante.size());
    }

    @Entao("a lista deve conter um modelo com referencia {string} e nome {string}")
    public void a_lista_deve_conter_um_modelo_com_referencia_e_nome(String referencia, String nome) {
        // Verificamos os detalhes do resultado
        boolean encontrado = this.listaDeModelosResultante.stream()
            .anyMatch(modelo -> 
                modelo.getReferencia().equals(referencia) && modelo.getNome().equals(nome)
            );
        assertTrue(encontrado, "O modelo com referência " + referencia + " e nome " + nome + " não foi encontrado na lista.");
    }

    @Dado("que não existem modelos cadastrados no sistema")
    public void que_nao_existem_modelos_cadastrados_no_sistema() {
        // O @Before já garante que o repositório está vazio. Este passo é apenas para legibilidade.
        // Podemos adicionar uma asserção para garantir.
        assertTrue(modeloService.listarTodos().isEmpty());
    }

    @Entao("eu devo receber uma lista vazia")
    public void eu_devo_receber_uma_lista_vazia() {
        // Verificamos o resultado do cenário de "caminho vazio"
        assertNotNull(this.listaDeModelosResultante);
        assertTrue(this.listaDeModelosResultante.isEmpty());
    }
}