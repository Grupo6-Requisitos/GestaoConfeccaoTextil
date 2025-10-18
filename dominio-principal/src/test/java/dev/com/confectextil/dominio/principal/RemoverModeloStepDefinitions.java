package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.insumo.InsumoId;
import dev.com.confectextil.dominio.principal.modelo.InsumoPadrao;
import dev.com.confectextil.dominio.principal.modelo.ModeloId;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import io.cucumber.datatable.DataTable;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class RemoverModeloStepDefinitions {

    // Repositório fake em memória
    private ModeloRepositorioMemoria modeloRepository;

    // Estado do cenário
    private String referenciaAlvo;
    private Throwable erro;

    @Before
    public void setup() {
        this.modeloRepository = new ModeloRepositorioMemoria();
        this.modeloRepository.limpar();
        this.referenciaAlvo = null;
        this.erro = null;
    }

    // ========== Contexto ==========

    @Dado("que existem os seguintes modelos cadastrados para remoção:")
    public void queExistemOsSeguintesModelosCadastradosParaRemocao(DataTable tabela) {
        for (Map<String, String> row : tabela.asMaps(String.class, String.class)) {
            String modeloId   = row.get("modeloId");
            String referencia = row.get("referencia");
            String nome       = row.get("nome");

            // 2 insumos mockados (lista não-nula)
            List<InsumoPadrao> insumosMock = criar2InsumosMock();

            Modelo modelo = new Modelo(
                    new ModeloId(modeloId),
                    referencia,
                    nome,
                    null,               // imagemUrl
                    insumosMock
            );

            modeloRepository.salvar(modelo);
        }
    }

    // ========== Dados da remoção ==========

    @Dado("que desejo remover o modelo com referência {string}")
    public void queDesejoRemoverOModeloComReferencia(String referencia) {
        this.referenciaAlvo = referencia;
    }

    // ========== Ação ==========

    @Quando("o usuário solicita a remoção do modelo")
    public void oUsuarioSolicitaARemocaoDoModelo() {
        try {
            if (referenciaAlvo == null || referenciaAlvo.isBlank()) {
                throw new IllegalArgumentException("Referência alvo não informada");
            }

            var opt = modeloRepository.buscarPorReferencia(referenciaAlvo);
            if (opt.isEmpty()) {
                throw new IllegalStateException("O modelo com referência " + referenciaAlvo + " não foi encontrado");
            }

            modeloRepository.removerPorReferencia(referenciaAlvo);

        } catch (Throwable t) {
            this.erro = t;
        }
    }

    // ========== Asserts de sucesso ==========

    @Então("o modelo deve ser removido com sucesso")
    public void oModeloDeveSerRemovidoComSucesso() {
        assertNull(erro, "Não era esperado erro, mas ocorreu: " + (erro == null ? "" : erro.getMessage()));
    }

    @E("o modelo com referência {string} não deve existir")
    public void oModeloComReferenciaNaoDeveExistir(String referencia) {
        assertTrue(modeloRepository.buscarPorReferencia(referencia).isEmpty(),
                "Modelo com referência " + referencia + " ainda existe após remoção.");
    }

    @E("os modelos restantes devem ser:")
    public void osModelosRestantesDevemSer(DataTable tabela) {
        // esperado (referencia -> nome)
        Map<String, String> esperado = tabela.asMaps(String.class, String.class)
                .stream()
                .collect(Collectors.toMap(
                        r -> r.get("referencia"),
                        r -> r.get("nome"),
                        (a,b) -> a,
                        LinkedHashMap::new
                ));

        // obtido (referencia -> nome)
        Map<String, String> obtido = modeloRepository.listarTodos().stream()
                .collect(Collectors.toMap(
                        Modelo::getReferencia,
                        Modelo::getNome
                ));

        assertEquals(esperado.size(), obtido.size(),
                "Quantidade de modelos restantes diverge do esperado.");

        for (Map.Entry<String, String> e : esperado.entrySet()) {
            assertTrue(obtido.containsKey(e.getKey()),
                    "Modelo esperado não encontrado: " + e.getKey());
            assertEquals(e.getValue(), obtido.get(e.getKey()),
                    "Nome divergente para a referência " + e.getKey());
        }
    }

    // ========== Asserts de erro ==========

    @Então("deve ocorrer um erro ao remover com a mensagem {string}")
    public void deveOcorrerUmErroAoRemoverComAMensagem(String mensagem) {
        assertNotNull(erro, "Era esperado erro, mas nenhum foi lançado.");
        assertEquals(mensagem, erro.getMessage(), "Mensagem de erro divergente.");
    }

    // ========== Helpers ==========

    /** Cria sempre 2 insumos mockados com quantidades padrão (lista não-nula). */
    private List<InsumoPadrao> criar2InsumosMock() {
        List<InsumoPadrao> lista = new ArrayList<>();

        Insumo tecido = new Insumo(
                new InsumoId(UUID.randomUUID().toString()),
                "INS-001",
                "Tecido Algodão",
                "metro",
                12.50
        );

        Insumo linha = new Insumo(
                new InsumoId(UUID.randomUUID().toString()),
                "INS-002",
                "Linha Costura",
                "rolo",
                2.30
        );

        lista.add(new InsumoPadrao(tecido.getId(), 1.5));
        lista.add(new InsumoPadrao(linha.getId(), 0.25));
        return lista;
    }
}
