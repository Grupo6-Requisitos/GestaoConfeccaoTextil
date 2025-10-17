package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;
import dev.com.confectextil.dominio.principal.insumo.InsumoService;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.InsumoRepositorioMemoria;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ModeloStepDefinitions {

    private InsumoRepository insumoRepository;
    private ModeloRepository modeloRepository;
    private InsumoService insumoService;
    private ModeloService modeloService;

    private Exception excecaoCapturada;
    private Modelo modeloSalvo;
    private List<Modelo> listaDeModelosResultante;
    private Map<String, String> dadosDoModelo;
    private List<ModeloService.InsumoPadraoDTO> dadosDosInsumos;

    @Before
    public void setUp() {
        this.insumoRepository = new InsumoRepositorioMemoria();
        this.modeloRepository = new ModeloRepositorioMemoria();
        this.insumoService = new InsumoService(insumoRepository);
        this.modeloService = new ModeloService(modeloRepository, insumoRepository);
        this.excecaoCapturada = null;
        this.modeloSalvo = null;
        this.dadosDosInsumos = Collections.emptyList();
    }

    @Dado("que eu sou um usuário {string} autenticado")
    public void que_eu_sou_um_usuario_autenticado(String perfil) {
    }

    @Dado("não existe um modelo com a referência {string}")
    public void nao_existe_um_modelo_com_a_referencia(String referencia) {
        assertFalse(modeloRepository.buscarPorReferencia(referencia).isPresent());
    }

    @Dado("existem os seguintes insumos cadastrados no sistema:")
    public void existem_os_seguintes_insumos_cadastrados_no_sistema(DataTable dataTable) {
        List<Map<String, String>> linhas = dataTable.asMaps();
        for (var linha : linhas) {
            insumoService.cadastrarInsumo(
                linha.get("referencia"),
                linha.get("nome"),
                linha.get("unidade")
            );
        }
    }

    @Quando("eu tento cadastrar um novo modelo com os seguintes dados:")
    public void eu_tento_cadastrar_um_novo_modelo_com_os_seguintes_dados(DataTable dataTable) {
        this.dadosDoModelo = dataTable.asMap();
    }

    @E("com a seguinte lista de insumos padrão:")
    public void com_a_seguinte_lista_de_insumos_padrao(DataTable dataTable) {
        this.dadosDosInsumos = dataTable.asMaps().stream()
            .map(linha -> {
                double quantidade = parseQuantidade(linha.get("quantidade_sugerida"));
                return new ModeloService.InsumoPadraoDTO(
                    linha.get("insumo_referencia"),
                    quantidade
                );
            })
            .collect(Collectors.toList());
    }

    private void executarCadastro() {
        if (excecaoCapturada == null && modeloSalvo == null) {
            try {
                modeloSalvo = modeloService.cadastrarModelo(
                    dadosDoModelo.get("referencia"),
                    dadosDoModelo.get("nome"),
                    dadosDoModelo.get("imagemUrl"),
                    dadosDosInsumos
                );
            } catch (Exception e) {
                this.excecaoCapturada = e;
            }
        }
    }

    @Entao("o modelo com a referência {string} deve ser salvo com sucesso")
    public void o_modelo_com_a_referencia_deve_ser_salvo_com_sucesso(String referencia) {
        executarCadastro();
        assertNull(excecaoCapturada, "Uma exceção foi lançada inesperadamente: " + (excecaoCapturada != null ? excecaoCapturada.getMessage() : ""));
        assertNotNull(modeloSalvo, "O modelo não foi salvo.");
        assertEquals(referencia, modeloSalvo.getReferencia());
    }

    @Entao("o modelo salvo deve ter o nome {string}")
    public void o_modelo_salvo_deve_ter_o_nome(String nome) {
        assertEquals(nome, modeloSalvo.getNome());
    }

    @Entao("o modelo salvo deve ter {int} insumos padrão")
    public void o_modelo_salvo_deve_ter_insumos_padrao(Integer quantidade) {
        assertEquals(quantidade, modeloSalvo.getInsumosPadrao().size());
    }

    @Entao("^o modelo salvo deve conter o insumo de referência \"([^\"]*)\" com quantidade sugerida ([0-9.,]+)$")
    public void o_modelo_salvo_deve_conter_o_insumo_de_referencia_com_quantidade_sugerida(String refInsumo, String qtdRaw) {
        executarCadastro();
        assertNotNull(modeloSalvo, "Modelo não está salvo — verifique falhas anteriores.");

        final double expected = parseQuantidade(qtdRaw);
        final double EPS = 1e-6;

        System.out.println("DEBUG - Insumos padrao do modelo (insumoId -> quantidade):");
        modeloSalvo.getInsumosPadrao().forEach(ip -> {
            try { System.out.println(" - insumoId=" + ip.insumoId() + " quantidade=" + ip.quantidadeSugerida()); }
            catch (Exception ignored) {}
        });

        boolean encontrado = modeloSalvo.getInsumosPadrao().stream()
            .anyMatch(insumoPadrao -> {
                Insumo insumo = insumoRepository.buscarPorReferencia(refInsumo).orElse(null);
                if (insumo == null) return false;
                double actual = insumoPadrao.quantidadeSugerida();
                System.out.println(String.format("DEBUG-COMP: ref='%s' insumoIdEsperado=%s insumoIdAtual=%s expected=%.6f actual=%.6f",
                        refInsumo, insumo.getId(), insumoPadrao.insumoId(), expected, actual));
                return insumoPadrao.insumoId().equals(insumo.getId()) && Math.abs(actual - expected) < EPS;
            });

        if (!encontrado) {
            final String lista = modeloSalvo.getInsumosPadrao().stream()
                .map(ip -> ip.insumoId() + ":" + ip.quantidadeSugerida())
                .collect(Collectors.joining(", "));
            fail("O insumo " + refInsumo + " com quantidade " + expected + " não foi encontrado no modelo. Insumos atuais: [" + lista + "]");
        }
    }

    @Dado("já existe um modelo cadastrado com a referência {string}")
    public void ja_existe_um_modelo_cadastrado_com_a_referencia(String referencia) {
        modeloService.cadastrarModelo(referencia, "Modelo Duplicado", null, Collections.emptyList());
    }

    @Entao("o sistema deve retornar um erro informando que {string}")
    public void o_sistema_deve_retornar_um_erro_informando_que(String mensagem) {
        executarCadastro();
        assertNotNull(excecaoCapturada, "Uma exceção era esperada, mas não foi lançada.");
        assertTrue(excecaoCapturada.getMessage().contains(mensagem));
    }

    @Dado("existem os seguintes modelos já cadastrados:")
    public void existem_os_seguintes_modelos_ja_cadastrados(DataTable dataTable) {
        dataTable.asMaps().forEach(linha ->
            modeloService.cadastrarModelo(linha.get("referencia"), linha.get("nome"), null, Collections.emptyList())
        );
    }

    @Quando("eu cadastro um novo modelo com referência {string} e nome {string}")
    public void eu_cadastro_um_novo_modelo_com_referencia_e_nome(String referencia, String nome) {
        this.dadosDoModelo = Map.of("referencia", referencia, "nome", nome);
        executarCadastro();
    }

    @E("eu peço a lista completa de modelos")
    public void eu_peco_a_lista_completa_de_modelos() {
        this.listaDeModelosResultante = modeloService.listarTodos();
    }

    @Entao("a lista de modelos deve conter {int} itens")
    public void a_lista_de_modelos_deve_conter_itens(Integer quantidade) {
        assertEquals(quantidade, listaDeModelosResultante.size());
    }

    @Entao("a lista de modelos deve incluir um modelo com referência {string} e nome {string}")
    public void a_lista_de_modelos_deve_incluir_um_modelo_com_referencia_e_nome(String referencia, String nome) {
        boolean encontrado = listaDeModelosResultante.stream()
            .anyMatch(modelo -> modelo.getReferencia().equals(referencia) && modelo.getNome().equals(nome));
        assertTrue(encontrado, "O modelo " + referencia + " não foi encontrado na lista.");
    }

    @Dado("não existe um insumo com a referência {string}")
    public void nao_existe_um_insumo_com_a_referencia(String referencia) {
        assertFalse(insumoRepository.buscarPorReferencia(referencia).isPresent());
    }

    private double parseQuantidade(String qtdRaw) {
        String raw = (qtdRaw == null ? "0" : qtdRaw.trim()).replaceAll("\\s+", "").replace(",", ".");
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException nf) {
            try {
                NumberFormat nfFormat = NumberFormat.getInstance(Locale.US);
                return nfFormat.parse(raw).doubleValue();
            } catch (ParseException pe) {
                throw new RuntimeException("Erro ao converter quantidade esperada: '" + qtdRaw + "'", pe);
            }
        }
    }
}
