package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.InsumoRepositorioMemoria;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EditarModeloStepDefinitions {

    private ModeloRepositorioMemoria modeloRepositorioMemoria;
    private InsumoRepositorioMemoria insumoRepositorioMemoria;
    private ModeloService modeloService;

    private String referenciaAlvo;
    private String novoNome;
    private String novaReferencia;

    private Modelo modeloAtualizado;
    private Throwable erro;

    @Before
    public void setup() {
        modeloRepositorioMemoria = new ModeloRepositorioMemoria();
        insumoRepositorioMemoria = new InsumoRepositorioMemoria();
        modeloService = new ModeloService(modeloRepositorioMemoria, insumoRepositorioMemoria);

        modeloRepositorioMemoria.limpar();
        insumoRepositorioMemoria.limpar();
        referenciaAlvo = null;
        novoNome = null;
        novaReferencia = null;
        modeloAtualizado = null;
        erro = null;
    }

    @Dado("que existem os seguintes modelos cadastrados:")
    public void queExistemOsSeguintesModelosCadastrados(DataTable tabela) {
        tabela.asMaps(String.class, String.class)
            .forEach(linha -> modeloService.cadastrarModelo(
                linha.get("referencia"),
                linha.get("nome"),
                null,
                List.of()
            ));
    }

    @Dado("que desejo editar o modelo com referência {string}")
    public void queDesejoEditarOModeloComReferencia(String referencia) {
        referenciaAlvo = referencia;
    }

    @E("informo o novo nome {string}")
    public void informoONovoNome(String nome) {
        novoNome = nome;
    }

    @E("informo a nova referência {string}")
    public void informoANovaReferencia(String novaRef) {
        novaReferencia = novaRef;
    }

    @Quando("o usuário solicita a edição do modelo")
    public void oUsuarioSolicitaAEdicaoDoModelo() {
        try {
            modeloAtualizado = modeloService.atualizarModelo(referenciaAlvo, novaReferencia, novoNome);
        } catch (Throwable t) {
            erro = t;
        }
    }

    @Então("o modelo deve ser atualizado com sucesso")
    public void oModeloDeveSerAtualizadoComSucesso() {
        assertNull(erro, "Não era esperado erro, mas ocorreu: " + (erro == null ? "" : erro.getMessage()));
        assertNotNull(modeloAtualizado, "Modelo atualizado não foi retornado.");
    }

    @E("os dados do modelo devem ser:")
    public void osDadosDoModeloDevemSer(DataTable tabela) {
        Map<String, String> esperado = tabela.asMaps(String.class, String.class)
            .stream()
            .collect(Collectors.toMap(
                linha -> linha.get("referencia"),
                linha -> linha.get("nome")
            ));

        List<Modelo> modelos = modeloService.listarTodos();

        esperado.forEach((referenciaEsperada, nomeEsperado) -> {
            Optional<Modelo> optModelo = modelos.stream()
                .filter(modelo -> modelo.getReferencia().equals(referenciaEsperada))
                .findFirst();

            assertTrue(optModelo.isPresent(), "Modelo não encontrado após edição: " + referenciaEsperada);
            assertEquals(nomeEsperado, optModelo.get().getNome(), "Nome divergente após edição");
        });
    }

    @Então("deve ocorrer um erro com a mensagem {string}")
    public void deveOcorrerUmErroComAMensagem(String mensagem) {
        assertNotNull(erro, "Era esperado erro, mas nenhum foi lançado.");
        assertEquals(mensagem, erro.getMessage(), "Mensagem de erro divergente.");
    }
}
