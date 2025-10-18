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

public class EditarModeloStepDefinitions {

    private ModeloRepositorioMemoria modeloRepository;

    private String referenciaAlvo;
    private String novoNome;
    private String novaReferencia;

    private Modelo modeloResultante;
    private Throwable erro;

    @Before
    public void setup() {
        this.modeloRepository = new ModeloRepositorioMemoria();
        this.modeloRepository.limpar();
        this.referenciaAlvo = null;
        this.novoNome = null;
        this.novaReferencia = null;
        this.modeloResultante = null;
        this.erro = null;
    }

    @Dado("que existem os seguintes modelos cadastrados:")
    public void queExistemOsSeguintesModelosCadastrados(DataTable tabela) {
        for (Map<String, String> row : tabela.asMaps(String.class, String.class)) {
            String modeloId   = row.get("modeloId");
            String referencia = row.get("referencia");
            String nome       = row.get("nome");

            List<InsumoPadrao> insumosMock = criarInsumosMock();

            Modelo modelo = new Modelo(
                    new ModeloId(modeloId),
                    referencia,
                    nome,
                    null,
                    insumosMock
            );

            modeloRepository.salvar(modelo);
        }
    }

    /**
     * Cria 2 insumos mockados (sempre os mesmos).
     */
    private List<InsumoPadrao> criarInsumosMock() {
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


    @Dado("que desejo editar o modelo com referência {string}")
    public void queDesejoEditarOModeloComReferencia(String referencia) {
        this.referenciaAlvo = referencia;
    }

    @E("informo o novo nome {string}")
    public void informoONovoNome(String nome) {
        this.novoNome = nome;
    }

    @E("informo a nova referência {string}")
    public void informoANovaReferencia(String novaRef) {
        this.novaReferencia = novaRef;
    }

    @Quando("o usuário solicita a edição do modelo")
    public void oUsuarioSolicitaAEdicaoDoModelo() {
        try {
            if (referenciaAlvo == null || referenciaAlvo.isBlank()) {
                throw new IllegalArgumentException("Referência alvo não informada");
            }

            // Busca o modelo existente (Optional)
            var optExistente = modeloRepository.buscarPorReferencia(referenciaAlvo);
            if (optExistente.isEmpty()) {
                throw new IllegalStateException("O modelo com referência " + referenciaAlvo + " não foi encontrado");
            }

            var existente = optExistente.get();
            String refFinal  = existente.getReferencia();
            String nomeFinal = existente.getNome();

            // Atualizar nome (se solicitado)
            if (novoNome != null) {
                if (novoNome.isBlank()) {
                    throw new IllegalArgumentException("O nome do modelo é obrigatório");
                }
                nomeFinal = novoNome;
            }

            // Atualizar referência (se solicitado)
            if (novaReferencia != null) {
                if (novaReferencia.isBlank()) {
                    throw new IllegalArgumentException("A nova referência é obrigatória");
                }
                if (!novaReferencia.equals(referenciaAlvo)) {
                    // Garante unicidade da nova referência
                    if (modeloRepository.buscarPorReferencia(novaReferencia).isPresent()) {
                        throw new IllegalStateException("Já existe um modelo com a referência " + novaReferencia);
                    }
                    // Remover o registro antigo (chave = referência antiga)
                    modeloRepository.removerPorReferencia(existente.getReferencia());
                    refFinal = novaReferencia;
                }
            }

            // Persiste o "novo estado" com a referência/nome finais
            var atualizado = new Modelo(
                    existente.getId(),
                    refFinal,
                    nomeFinal,
                    existente.getImagemUrl(),
                    existente.getInsumosPadrao()
            );
            modeloRepository.salvar(atualizado);
            this.modeloResultante = atualizado;

        } catch (Throwable t) {
            this.erro = t;
        }
    }


    @Então("o modelo deve ser atualizado com sucesso")
    public void oModeloDeveSerAtualizadoComSucesso() {
        assertNull(erro, "Não era esperado erro, mas ocorreu: " + (erro == null ? "" : erro.getMessage()));
        assertNotNull(modeloResultante, "Modelo atualizado não foi retornado.");
    }

    @E("os dados do modelo devem ser:")
    public void osDadosDoModeloDevemSer(DataTable tabela) {
        Map<String, String> row = tabela.asMaps(String.class, String.class).get(0);
        String refEsperada = row.get("referencia");
        String nomeEsperado = row.get("nome");

        Optional<Modelo> optModelo = modeloRepository.buscarPorReferencia(refEsperada);
        assertTrue(optModelo.isPresent(), "Modelo não encontrado após edição: " + refEsperada);

        Modelo m = optModelo.get();
        assertEquals(nomeEsperado, m.getNome(), "Nome divergente após edição");
    }

    @Então("deve ocorrer um erro com a mensagem {string}")
    public void deveOcorrerUmErroComAMensagem(String mensagem) {
        assertNotNull(erro, "Era esperado erro, mas nenhum foi lançado.");
        assertEquals(mensagem, erro.getMessage(), "Mensagem de erro divergente.");
    }
}
