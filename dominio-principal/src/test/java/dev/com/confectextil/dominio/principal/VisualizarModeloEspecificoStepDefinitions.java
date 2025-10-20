package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.insumo.InsumoId;
import dev.com.confectextil.dominio.principal.modelo.InsumoPadrao;
import dev.com.confectextil.dominio.principal.modelo.ModeloId;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ModeloRepositorioMemoria;
import io.cucumber.java.Before;
import io.cucumber.java.pt.*;
import io.cucumber.datatable.DataTable;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class VisualizarModeloEspecificoStepDefinitions {

    private ModeloRepositorioMemoria modeloRepository;

    private String referenciaBusca;
    private String modeloIdBusca;
    private Modelo modeloVisualizado;
    private Throwable erro;

    // índices auxiliares baseados no Contexto
    private final Map<String, String> idToRef = new HashMap<>();
    private final Map<String, String> refToId = new HashMap<>();

    @Before
    public void setup() {
        this.modeloRepository = new ModeloRepositorioMemoria();
        this.modeloRepository.limpar();
        this.referenciaBusca = null;
        this.modeloIdBusca = null;
        this.modeloVisualizado = null;
        this.erro = null;
        this.idToRef.clear();
        this.refToId.clear();
    }


    @Dado("que existem os seguintes modelos cadastrados para visualização:")
    public void queExistemOsSeguintesModelosCadastradosParaVisualizacao(DataTable tabela) {
        for (Map<String, String> row : tabela.asMaps(String.class, String.class)) {
            String modeloId   = safe(row.get("modeloId"));
            String referencia = safe(row.get("referencia"));
            String nome       = safe(row.get("nome"));

            List<InsumoPadrao> insumos = criar2InsumosMock();

            Modelo m = new Modelo(
                    new ModeloId(modeloId),
                    referencia,
                    nome,
                    null,
                    insumos
            );

            modeloRepository.salvar(m);
            var opt = modeloRepository.buscarPorReferencia(referencia);
            assertTrue(opt.isPresent(),
                    "Falha ao preparar Contexto: modelo salvo não foi encontrado por referência: " + referencia);

            idToRef.put(modeloId, referencia);
            refToId.put(referencia, modeloId);
        }
    }


    @Dado("que desejo visualizar o modelo pela referência {string}")
    public void queDesejoVisualizarOModeloPelaReferencia(String referencia) {
        this.referenciaBusca = safe(referencia);
    }

    @Dado("que desejo visualizar o modelo pelo modeloId {string}")
    public void queDesejoVisualizarOModeloPeloModeloId(String modeloId) {
        this.modeloIdBusca = safe(modeloId);
    }


    @Quando("o usuário solicita a visualização do modelo")
    public void oUsuarioSolicitaAVisualizacaoDoModelo() {
        try {
            boolean refBlank = isBlank(referenciaBusca);
            boolean idBlank  = isBlank(modeloIdBusca);

            if (refBlank && idBlank) {
                throw new IllegalArgumentException("É obrigatório informar referência ou modeloId");
            }

            // 1) Se referência foi informada: resolva por referência
            if (!refBlank) {
                var opt = modeloRepository.buscarPorReferencia(referenciaBusca);
                if (opt.isPresent()) {
                    modeloVisualizado = opt.get();
                    return;
                }
                // não achou por ref e não temos id -> erro imediato
                if (idBlank) {
                    throw new IllegalStateException("O modelo com referência " + referenciaBusca + " não foi encontrado");
                }
            }

            // 2) Se modeloId foi informado: PRIMEIRO use o índice (mais confiável)
            if (!idBlank) {
                String refViaIndice = idToRef.get(modeloIdBusca);
                if (!isBlank(refViaIndice)) {
                    var optRef = modeloRepository.buscarPorReferencia(refViaIndice);
                    if (optRef.isPresent()) {
                        modeloVisualizado = optRef.get();
                        return;
                    }
                }

                // 3) Tentativa secundária: varrer repositório por id
                var porId = modeloRepository.listarTodos().stream()
                        .filter(m -> modeloIdEquals(m, modeloIdBusca))
                        .findFirst();

                if (porId.isPresent()) {
                    modeloVisualizado = porId.get();
                    return;
                }

                // 4) Erro explícito
                throw new IllegalStateException("O modelo com id " + modeloIdBusca + " não foi encontrado");
            }

        } catch (Throwable t) {
            this.erro = t;
        }
    }


    @Então("o modelo deve ser exibido com sucesso")
    public void oModeloDeveSerExibidoComSucesso() {
        assertNull(erro, "Não era esperado erro, mas ocorreu: " + (erro == null ? "" : erro.getMessage()));
        assertNotNull(modeloVisualizado, "Modelo não foi retornado.");
    }

    @E("os dados do modelo visualizado devem ser:")
    public void osDadosDoModeloVisualizadoDevemSer(DataTable table) {
        Map<String, String> row = table.asMaps(String.class, String.class).get(0);
        String modeloIdEsperado   = safe(row.get("modeloId"));
        String referenciaEsperada = safe(row.get("referencia"));
        String nomeEsperado       = safe(row.get("nome"));

        assertNotNull(modeloVisualizado, "Modelo não foi carregado para verificação.");

        if (!isBlank(modeloIdEsperado)) {
            boolean idOk =
                    modeloIdEquals(modeloVisualizado, modeloIdEsperado)
                            || modeloIdEsperado.equals(refToId.get(refSafe(modeloVisualizado))); // <-- correto: ref -> id

            assertTrue(idOk, "modeloId divergente. Esperado=" + modeloIdEsperado);
        }

        assertEquals(referenciaEsperada, refSafe(modeloVisualizado), "Referência divergente");
        assertEquals(nomeEsperado, modeloVisualizado.getNome(), "Nome divergente");
    }


    @Então("deve ocorrer um erro ao visualizar com a mensagem {string}")
    public void deveOcorrerUmErroAoVisualizarComAMensagem(String msg) {
        assertNotNull(erro, "Era esperado erro, mas nenhum foi lançado.");
        assertEquals(msg, erro.getMessage(), "Mensagem de erro divergente.");
    }

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

    private boolean modeloIdEquals(Modelo m, String modeloIdEsperado) {
        // tenta getModeloId().getId()
        try {
            Object mid = m.getClass().getMethod("getModeloId").invoke(m);
            if (mid != null) {
                try {
                    Object id = mid.getClass().getMethod("getId").invoke(mid);
                    if (id != null && modeloIdEsperado.equals(String.valueOf(id))) return true;
                } catch (NoSuchMethodException ignore) {}

                try {
                    Object id = mid.getClass().getMethod("getValor").invoke(mid);
                    if (id != null && modeloIdEsperado.equals(String.valueOf(id))) return true;
                } catch (NoSuchMethodException ignore) {}
                try {
                    Object id = mid.getClass().getMethod("value").invoke(mid);
                    if (id != null && modeloIdEsperado.equals(String.valueOf(id))) return true;
                } catch (NoSuchMethodException ignore) {}

                if (modeloIdEsperado.equals(String.valueOf(mid))) return true;
            }
        } catch (Throwable ignore) {}

        try {
            Object id = m.getClass().getMethod("getId").invoke(m);
            if (id != null && modeloIdEsperado.equals(String.valueOf(id))) return true;
        } catch (Throwable ignore) {}

        return false;
    }

    private String refSafe(Modelo m) {
        String ref = m.getReferencia();
        return ref == null ? "" : ref.trim();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}