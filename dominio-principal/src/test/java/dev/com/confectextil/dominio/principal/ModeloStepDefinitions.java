package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;
import dev.com.confectextil.dominio.principal.insumo.InsumoService;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.InsumoRepositorioMemoria;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.pt.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ModeloStepDefinitions {

    private InsumoRepository insumoRepository;
    private ModeloRepository modeloRepository;
    private InsumoService insumoService;
    private ModeloService modeloService;

    private Exception excecaoCapturada;
    private Map<String, String> dadosDoModelo;
    private List<ModeloService.InsumoPadraoDTO> listaInsumosPadrao;
    private String referenciaEmContexto;

    @Before
    public void setup() {
        insumoRepository = new InsumoRepositorioMemoria();
        modeloRepository = new ModeloRepositorioMemoria();
        insumoService = new InsumoService(insumoRepository);
        modeloService = new ModeloService(modeloRepository, insumoRepository);

        excecaoCapturada = null;
        dadosDoModelo = null;
        listaInsumosPadrao = Collections.emptyList();
        referenciaEmContexto = null;
    }

    // Novo: registro do DataTableType para converter direto para InsumoPadraoDTO
    @DataTableType
    public ModeloService.InsumoPadraoDTO insumoPadraoDTOEntry(Map<String, String> entry) {
        String referencia = entry.get("insumo_referencia");
        String qtdStr = entry.get("quantidade_sugerida").replace(",", "."); // trata vírgula
        double quantidade = Double.parseDouble(qtdStr);
        return new ModeloService.InsumoPadraoDTO(referencia, quantidade);
    }

    private void tentarCadastrarModelo() {
        if (excecaoCapturada == null) {
            try {
                referenciaEmContexto = dadosDoModelo.get("referencia");
                modeloService.cadastrarModelo(
                        dadosDoModelo.get("referencia"),
                        dadosDoModelo.get("nome"),
                        dadosDoModelo.get("imagemUrl"),
                        listaInsumosPadrao
                );
            } catch (Exception e) {
                excecaoCapturada = e;
            }
        }
    }

    @Dado("que eu sou um usuário {string} autenticado")
    public void queEuSouUmUsuarioAutenticado(String perfil) {
        // Nenhuma ação necessária, pois o sistema não implementa autenticação para os testes.
    }

    @Dado("não existe um modelo com a referência {string}")
    public void naoExisteUmModeloComAReferencia(String referencia) {
        assertFalse(modeloRepository.buscarPorReferencia(referencia).isPresent());
    }

    @Dado("existem os seguintes insumos cadastrados no sistema:")
    public void existemOsSeguintesInsumosCadastradosNoSistema(DataTable tabela) {
        for (Map<String, String> linha : tabela.asMaps()) {
            insumoService.cadastrarInsumo(
                    linha.get("referencia"),
                    linha.get("nome"),
                    linha.get("unidade")
            );
        }
    }

    @Quando("eu tento cadastrar um novo modelo com os seguintes dados:")
    public void euTentoCadastrarUmNovoModeloComOsSeguintesDados(DataTable tabela) {
        dadosDoModelo = tabela.asMap();
    }

    // Alterado: usa conversão direta para lista de InsumoPadraoDTO
    @E("com a seguinte lista de insumos padrão:")
    public void comASeguinteListaDeInsumosPadrao(List<ModeloService.InsumoPadraoDTO> lista) {
        listaInsumosPadrao = lista;
    }

    @Entao("o modelo com a referência {string} deve ser salvo com sucesso")
    public void oModeloComAReferenciaDeveSerSalvoComSucesso(String referencia) {
        tentarCadastrarModelo();
        assertNull(excecaoCapturada, "Não esperava exceção ao salvar modelo.");

        var modeloOpt = modeloRepository.buscarPorReferencia(referencia);
        assertTrue(modeloOpt.isPresent(), "Modelo não encontrado no repositório.");
        assertEquals(referencia, modeloOpt.get().getReferencia());
    }

    @Entao("o modelo salvo deve ter o nome {string}")
    public void oModeloSalvoDeveTerONome(String nomeEsperado) {
        var modeloOpt = modeloRepository.buscarPorReferencia(referenciaEmContexto);
        assertTrue(modeloOpt.isPresent(), "Modelo para verificação não encontrado.");
        assertEquals(nomeEsperado, modeloOpt.get().getNome());
    }

    @Entao("o modelo salvo deve ter {int} insumos padrão")
    public void oModeloSalvoDeveTerInsumosPadrao(Integer quantidadeEsperada) {
        var modeloOpt = modeloRepository.buscarPorReferencia(referenciaEmContexto);
        assertTrue(modeloOpt.isPresent(), "Modelo para verificação não encontrado.");
        assertEquals(quantidadeEsperada, modeloOpt.get().getInsumosPadrao().size());
    }

    @Entao("o modelo salvo deve conter o insumo de referência {string} com quantidade sugerida {double}")
    public void oModeloSalvoDeveConterOInsumoDeReferenciaComQuantidadeSugerida(String referenciaInsumo, double quantidadeEsperada) {
        var modeloOpt = modeloRepository.buscarPorReferencia(referenciaEmContexto);
        assertTrue(modeloOpt.isPresent(), "Modelo para verificação não encontrado.");

        var modelo = modeloOpt.get();

        var insumoOpt = insumoRepository.buscarPorReferencia(referenciaInsumo);
        assertTrue(insumoOpt.isPresent(), "Insumo para verificação não encontrado.");

        var idInsumo = insumoOpt.get().getId();

        boolean encontrado = modelo.getInsumosPadrao().stream()
                .anyMatch(ip -> ip.insumoId().equals(idInsumo) &&
                        Math.abs(ip.quantidadeSugerida() - quantidadeEsperada) < 0.000001);

        assertTrue(encontrado,
                "O insumo com referência '" + referenciaInsumo + "' e quantidade sugerida " + quantidadeEsperada + " não foi encontrado no modelo.");
    }

    @Dado("já existe um modelo cadastrado com a referência {string}")
    public void jaExisteUmModeloCadastradoComAReferencia(String referencia) {
        modeloService.cadastrarModelo(referencia, "Modelo Existente", null, Collections.emptyList());
    }

    @Entao("o sistema deve retornar um erro informando que {string}")
    public void oSistemaDeveRetornarUmErroInformandoQue(String mensagemEsperada) {
        tentarCadastrarModelo();
        assertNotNull(excecaoCapturada, "Esperava exceção, mas nenhuma foi lançada.");
        assertTrue(excecaoCapturada.getMessage().contains(mensagemEsperada),
                "Mensagem de erro esperada não encontrada. Esperado: '" + mensagemEsperada + "', Recebido: '" + excecaoCapturada.getMessage() + "'");
    }

    @Dado("existem os seguintes modelos já cadastrados:")
    public void existemOsSeguintesModelosJaCadastrados(DataTable tabela) {
        for (Map<String, String> linha : tabela.asMaps()) {
            modeloService.cadastrarModelo(linha.get("referencia"), linha.get("nome"), null, Collections.emptyList());
        }
    }

    @Quando("eu cadastro um novo modelo com referência {string} e nome {string}")
    public void euCadastroUmNovoModeloComReferenciaENome(String referencia, String nome) {
        dadosDoModelo = Map.of("referencia", referencia, "nome", nome);
        listaInsumosPadrao = Collections.emptyList();
        referenciaEmContexto = referencia;
        tentarCadastrarModelo();
    }

    @Quando("eu peço a lista completa de modelos")
    public void euPecoAListaCompletaDeModelos() {
        // A ação de obter a lista será feita no passo de validação.
    }

    @Entao("a lista de modelos deve conter {int} itens")
    public void aListaDeModelosDeveConterItens(int quantidadeEsperada) {
        List<Modelo> modelos = modeloService.listarTodos();
        assertEquals(quantidadeEsperada, modelos.size());
    }

    @Entao("a lista de modelos deve incluir um modelo com referência {string} e nome {string}")
    public void aListaDeModelosDeveIncluirUmModeloComReferenciaENome(String referencia, String nome) {
        List<Modelo> modelos = modeloService.listarTodos();
        boolean encontrado = modelos.stream()
                .anyMatch(m -> m.getReferencia().equals(referencia) && m.getNome().equals(nome));
        assertTrue(encontrado, "Modelo com referência " + referencia + " e nome " + nome + " não foi encontrado na lista.");
    }

    @Dado("não existe um insumo com a referência {string}")
    public void naoExisteUmInsumoComAReferencia(String referencia) {
        assertFalse(insumoRepository.buscarPorReferencia(referencia).isPresent());
    }

    @Dado("que não existem modelos cadastrados no sistema")
    public void queNaoExistemModelosCadastradosNoSistema() {
        assertTrue(modeloService.listarTodos().isEmpty());
    }

    @Entao("eu devo receber uma lista vazia")
    public void euDevoReceberUmaListaVazia() {
        List<Modelo> modelos = modeloService.listarTodos();
        assertNotNull(modelos);
        assertTrue(modelos.isEmpty());
    }

    // ** IMPLEMENTAÇÃO DO STEP QUE ESTAVA FALTANDO - 1 **
    @Dado("que existem os seguintes modelos já cadastrados no sistema:")
    public void queExistemOsSeguintesModelosJaCadastradosNoSistema(DataTable tabela) {
        for (Map<String, String> linha : tabela.asMaps()) {
            modeloService.cadastrarModelo(linha.get("referencia"), linha.get("nome"), null, Collections.emptyList());
        }
    }

    // ** IMPLEMENTAÇÃO DO STEP QUE ESTAVA FALTANDO - 2 **
    @Entao("a lista retornada deve conter {int} modelos, incluindo um com referência {string} e nome {string}")
    public void aListaRetornadaDeveConterModelosIncluindoUmComReferenciaENome(Integer quantidadeEsperada, String referencia, String nome) {
        List<Modelo> modelos = modeloService.listarTodos();
        assertEquals(quantidadeEsperada.intValue(), modelos.size(), "Quantidade de modelos diferente do esperado.");

        boolean encontrado = modelos.stream()
                .anyMatch(m -> m.getReferencia().equals(referencia) && m.getNome().equals(nome));
        assertTrue(encontrado, "Modelo com referência " + referencia + " e nome " + nome + " não foi encontrado na lista.");
    }

}
