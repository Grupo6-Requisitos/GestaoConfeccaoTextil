package dev.com.protactic.dominio.principal;

import dev.com.protactic.dominio.principal.cadastroAtleta.*;
import dev.com.protactic.mocks.JogadorMock;
import io.cucumber.java.pt.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class CadastroDeAtletaFeature {

    private Clube meuClube;
    private Clube outroClube;
    private Jogador jogador;
    private CadastroDeAtletaService cadastroDeAtletaService;
    private boolean resultadoContratacao;
    private Date dataAtual;

    private IJogadorRepository jogadorRepo;
    private IClubeRepository clubeRepo;

    @Dado("que o ano atual é {int}")
    public void que_o_ano_atual_e(Integer ano) {
        this.meuClube = new Clube("Meu Time FC");
        this.outroClube = new Clube("Rival AC");

        this.jogadorRepo = new JogadorMock();
        this.clubeRepo = new ClubeRepository();
        this.cadastroDeAtletaService = new CadastroDeAtletaService(jogadorRepo, clubeRepo);
    }

    @Dado("que um {string} com contrato ativo em outro clube existe")
    public void que_um_jogador_com_contrato_em_outro_clube_existe(String posicao) {
        this.jogador = new Jogador("Jogador_" + posicao);
        this.jogador.setPosicao(posicao);

        Contrato contratoExistente = new Contrato(outroClube);
        this.jogador.setContrato(contratoExistente);
        outroClube.adicionarJogador(this.jogador);
    }

    @Dado("que um {string} sem contrato existe")
    public void que_um_jogador_sem_contrato_existe(String posicao) {
        this.jogador = new Jogador("Jogador_" + posicao);
        this.jogador.setPosicao(posicao);
    }

    @Dado("estamos em {int} de {word} \\(dentro da janela de transferências)")
    public void estamos_dentro_da_janela(Integer dia, String mes) {
        this.dataAtual = criarData(dia, mes, 2025);
        System.out.println("Data: " + dia + " de " + mes + " (dentro da janela)");
    }

    @Dado("estamos em {int} de {word} \\(fora da janela de transferências)")
    public void estamos_fora_da_janela(Integer dia, String mes) {
        this.dataAtual = criarData(dia, mes, 2025);
        System.out.println("Data: " + dia + " de " + mes + " (fora da janela)");
    }

    private Date criarData(int dia, String mesNome, int ano) {
        Map<String, Integer> meses = new HashMap<>();
        meses.put("janeiro", 1);
        meses.put("fevereiro", 2);
        meses.put("março", 3);
        meses.put("abril", 4);
        meses.put("maio", 5);
        meses.put("junho", 6);
        meses.put("julho", 7);
        meses.put("agosto", 8);
        meses.put("setembro", 9);
        meses.put("outubro", 10);
        meses.put("novembro", 11);
        meses.put("dezembro", 12);

        int mes = meses.get(mesNome.toLowerCase());

        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes - 1, dia, 0, 0, 0);
        return cal.getTime();
    }

    @Quando("eu tentar cadastrar esse atleta no meu clube")
    public void eu_tentar_cadastrar_esse_atleta_no_meu_clube() {
        this.resultadoContratacao = cadastroDeAtletaService.contratar(meuClube, jogador, this.dataAtual);
    }

    @Então("não conseguirei realizar a contratação")
    public void nao_conseguirei_realizar_a_contratacao() {
        // Jogador deveria estar com contrato ativo antes da tentativa
        assertNotNull(jogador.getContrato(), "O jogador deveria ter contrato ativo antes da contratação.");

        assertFalse(this.resultadoContratacao, "A contratação deveria ter falhado.");
        assertFalse(meuClube.possuiJogador(jogador.getNome()), "O jogador não deveria ter sido adicionado.");
        assertEquals(outroClube, jogador.getClube(), "O jogador deveria permanecer no clube original.");
    }

    @Então("o registro do atleta será adicionado à lista de atletas do clube")
    public void o_registro_do_atleta_sera_adicionado_a_lista_de_atletas_do_clube() {
        // Jogador deveria estar sem contrato antes da tentativa
        if (jogador.getContrato() == null) {
            assertNull(jogador.getContrato(), "O jogador deveria estar sem contrato antes da contratação.");
        }

        assertTrue(this.resultadoContratacao, "A contratação deveria ter sido bem-sucedida.");
        assertTrue(meuClube.possuiJogador(jogador.getNome()), "O meu clube deveria ter o novo jogador.");
        assertEquals(meuClube, jogador.getClube(), "O clube do jogador deveria ser agora o meu clube.");
        assertNotNull(jogador.getContrato(), "Um novo contrato deveria ter sido criado.");
        assertEquals("ATIVO", jogador.getContrato().getStatus(), "O contrato deveria estar ativo.");
    }
}