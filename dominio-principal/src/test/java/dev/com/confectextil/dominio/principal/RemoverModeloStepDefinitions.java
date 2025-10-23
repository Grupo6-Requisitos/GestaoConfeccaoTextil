package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.InsumoRepositorioMemoria;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RemoverModeloStepDefinitions {

    private ModeloRepositorioMemoria modeloRepositorioMemoria;
    private InsumoRepositorioMemoria insumoRepositorioMemoria;
    private ModeloService modeloService;

    private String referenciaAlvo;
    private Throwable erro;

    @Before
    public void setup() {
        modeloRepositorioMemoria = new ModeloRepositorioMemoria();
        insumoRepositorioMemoria = new InsumoRepositorioMemoria();
        modeloService = new ModeloService(modeloRepositorioMemoria, insumoRepositorioMemoria);

        modeloRepositorioMemoria.limpar();
        insumoRepositorioMemoria.limpar();
        referenciaAlvo = null;
        erro = null;
    }

    @Dado("que existem os seguintes modelos cadastrados para remoção:")
    public void queExistemOsSeguintesModelosCadastradosParaRemocao(DataTable tabela) {
        tabela.asMaps(String.class, String.class)
            .forEach(linha -> modeloService.cadastrarModelo(
                linha.get("referencia"),
                linha.get("nome"),
                null,
                List.of()
            ));
    }

    @Dado("que desejo remover o modelo com referência {string}")
    public void queDesejoRemoverOModeloComReferencia(String referencia) {
        referenciaAlvo = referencia;
    }

    @Quando("o usuário solicita a remoção do modelo")
    public void oUsuarioSolicitaARemocaoDoModelo() {
        try {
            modeloService.removerModeloPorReferencia(referenciaAlvo);
        } catch (Throwable t) {
            erro = t;
        }
    }

    @Então("o modelo deve ser removido com sucesso")
    public void oModeloDeveSerRemovidoComSucesso() {
        assertNull(erro, "Não era esperado erro, mas ocorreu: " + (erro == null ? "" : erro.getMessage()));
    }

    @E("o modelo com referência {string} não deve existir")
    public void oModeloComReferenciaNaoDeveExistir(String referencia) {
        boolean existe = modeloService.listarTodos().stream()
            .anyMatch(modelo -> modelo.getReferencia().equals(referencia));

        assertFalse(existe, "Modelo com referência " + referencia + " ainda existe após remoção.");
    }

    @E("os modelos restantes devem ser:")
    public void osModelosRestantesDevemSer(DataTable tabela) {
        Map<String, String> esperado = tabela.asMaps(String.class, String.class)
            .stream()
            .collect(Collectors.toMap(
                linha -> linha.get("referencia"),
                linha -> linha.get("nome"),
                (a, b) -> a,
                LinkedHashMap::new
            ));

        Map<String, String> obtido = modeloService.listarTodos().stream()
            .collect(Collectors.toMap(
                Modelo::getReferencia,
                Modelo::getNome
            ));

        assertEquals(esperado.size(), obtido.size(), "Quantidade de modelos restantes diverge do esperado.");
        esperado.forEach((referencia, nome) -> {
            assertTrue(obtido.containsKey(referencia), "Modelo esperado não encontrado: " + referencia);
            assertEquals(nome, obtido.get(referencia), "Nome divergente para a referência " + referencia);
        });
    }

    @Então("deve ocorrer um erro ao remover com a mensagem {string}")
    public void deveOcorrerUmErroAoRemoverComAMensagem(String mensagem) {
        assertNotNull(erro, "Era esperado erro, mas nenhum foi lançado.");
        assertEquals(mensagem, erro.getMessage(), "Mensagem de erro divergente.");
    }
}
