package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.EtapaRepositorioMemoria;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EtapaStepDefinitions {

  // --- VARIÁVEIS DE ESTADO ---
  private final EtapaRepositorioMemoria etapaRepository;
  private final EtapaService etapaService;
  private Etapa etapaAtual; 
  private Exception ultimaExcecao; 
  
  // --- CONSTRUTOR E SETUP ---

  public EtapaStepDefinitions() {
    this.etapaRepository = new EtapaRepositorioMemoria();
    this.etapaService = new EtapaService(this.etapaRepository);
  }

  @Before
  public void setup() {
    this.etapaRepository.limpar();
    this.ultimaExcecao = null;
    this.etapaAtual = null;
  }

    private String obterNomeEtapa(Map<String, String> dados) {
        String nomeValor = dados.get("nome");
        if (nomeValor == null || nomeValor.trim().isEmpty()) {
            return "";
        }
        return nomeValor.trim();
    }

  
  @Dado("que o sistema de gestão de produção está ativo")
  public void que_o_sistema_de_gestão_de_produção_está_ativo() {
  }

  @Dado("que não existe uma etapa cadastrada com id {string}")
  public void que_não_existe_uma_etapa_cadastrada_com_id(String etapaId) {
    assertFalse(etapaService.existe(etapaId), "A etapa com ID " + etapaId + " deveria não existir.");
  }
  
  @Dado("que já existe uma etapa cadastrada com id {string}")
  public void que_ja_existe_uma_etapa_cadastrada_com_id(String etapaId) {
    try {
      etapaService.cadastrarEtapa(etapaId, "Etapa de Setup para Duplicidade", 1);
      assertTrue(etapaService.existe(etapaId), "A Etapa com ID " + etapaId + " deveria existir.");
    } catch (IllegalArgumentException e) {
      fail("Falha no setup: " + e.getMessage());
    }
  }

  @Dado("que existe uma etapa com os seguintes dados:")
  public void que_existe_uma_etapa_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> row = dataTable.asMaps(String.class, String.class).get(0);
    String id = row.get("id");
    String nome = row.get("nome");
    int ordem = Integer.parseInt(row.get("ordem"));
    
    try {
      this.etapaAtual = etapaService.cadastrarEtapa(id, nome, ordem);
    } catch (IllegalArgumentException e) {
      fail("Falha no setup: Não foi possível criar a etapa " + id + ". Detalhe: " + e.getMessage());
    }
  }


  @Dado("que existem as etapas com os seguintes dados:")
  @Dado("que existem as etapas com os seguintes dados e ordem:")
  public void que_existem_as_etapas_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
    for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
      String id = row.get("id");
      String nome = row.get("nome");
      int ordem = Integer.parseInt(row.get("ordem"));
      
      try {
        etapaService.cadastrarEtapa(id, nome, ordem);
      } catch (IllegalArgumentException e) {
        fail("Falha no setup de Etapa: " + e.getMessage());
      }
    }
  }


  @Quando("eu cadastro uma nova etapa com os seguintes dados:")
  public void eu_cadastro_uma_nova_etapa_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> dados = dataTable.asMaps(String.class, String.class).get(0); 
    String id = dados.get("id");
    String nome = obterNomeEtapa(dados);
    int ordem = Integer.parseInt(dados.get("ordem"));
    
    try {
      this.etapaAtual = etapaService.cadastrarEtapa(id, nome, ordem);
      this.ultimaExcecao = null;
    } catch (Exception e) {
      this.ultimaExcecao = e;
    }
  }

  @Quando("eu tento cadastrar outra etapa com id {string}")
  public void eu_tento_cadastrar_outra_etapa_com_id(String etapaId) {
    try {
      etapaService.cadastrarEtapa(etapaId, "Tentativa Duplicada", 99);
      this.ultimaExcecao = null;
    } catch (Exception e) {
      this.ultimaExcecao = e; 
    }
  }

  @Quando("eu tento cadastrar uma etapa com os seguintes dados:")
  public void eu_tento_cadastrar_uma_etapa_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> dados = dataTable.asMaps(String.class, String.class).get(0);
    String id = dados.get("id");
    String nome = obterNomeEtapa(dados); // <--- CORREÇÃO APLICADA
    int ordem = Integer.parseInt(dados.get("ordem"));
    
    try {
      etapaService.cadastrarEtapa(id, nome, ordem);
      this.ultimaExcecao = null;
    } catch (Exception e) {
      this.ultimaExcecao = e; 
    }
  }
  
  @Quando("eu edito a etapa {string} alterando:")
  public void eu_edito_a_etapa_alterando(String id, io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> alteracoes = dataTable.asMap(String.class, String.class); 
    
    String novoNome = alteracoes.get("nome"); 
    Integer novaOrdem = alteracoes.containsKey("ordem") ? Integer.valueOf(alteracoes.get("ordem")) : null; 

    try {
      etapaService.editarEtapa(id, novoNome, novaOrdem);
      this.ultimaExcecao = null;
    } catch (Exception e) {
      this.ultimaExcecao = e; 
    }
  }
  
  @Quando("eu tento editar a etapa {string} alterando:")
  public void eu_tento_editar_a_etapa_alterando(String id, io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> alteracoes = dataTable.asMap(String.class, String.class); 
    
    String novoNome = alteracoes.get("nome"); 
    Integer novaOrdem = alteracoes.containsKey("ordem") ? Integer.valueOf(alteracoes.get("ordem")) : null;

    try {
      etapaService.editarEtapa(id, novoNome, novaOrdem);
      this.ultimaExcecao = null;
    } catch (Exception e) {
      this.ultimaExcecao = e; 
    }
  }
  
  @Quando("eu tento atualizar a ordem das etapas com os seguintes dados:")
  @Quando("eu atualizo a ordem das etapas com os seguintes dados:")
  public void eu_atualizo_a_ordem_das_etapas_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
    Map<String, Integer> novasOrdens = new HashMap<>();
    
    for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
      novasOrdens.put(row.get("id"), Integer.parseInt(row.get("novaOrdem")));
    }
    
    try {
      etapaService.reordenarEtapas(novasOrdens);
      this.ultimaExcecao = null; 
    } catch (Exception e) {
      this.ultimaExcecao = e; 
    }
  }


  
  @Então("a etapa {string} deve ser salva com sucesso")
  public void a_etapa_deve_ser_salva_com_sucesso(String etapaId) {
    assertTrue(etapaService.existe(etapaId), "A Etapa deveria existir no repositório");
    this.etapaAtual = etapaService.buscarPorId(etapaId);
    assertNotNull(this.etapaAtual);
  }

  @Então("o nome da etapa deve ser {string}")
  public void o_nome_da_etapa_deve_ser(String nomeEsperado) {
    assertEquals(nomeEsperado, this.etapaAtual.getNome(), "O nome da etapa não corresponde ao esperado.");
  }
  

  @Então("a etapa {string} deve ter o nome {string}")
  public void a_etapa_deve_ter_o_nome(String etapaId, String nomeEsperado) {
    Etapa etapaEditada = etapaService.buscarPorId(etapaId);
    assertNotNull(etapaEditada, "Etapa não encontrada após a edição.");
    assertEquals(nomeEsperado, etapaEditada.getNome(), "O nome da etapa não corresponde ao nome editado.");
    this.etapaAtual = etapaEditada;
  }
  
 @Então("a ordem deve ser {int}")
  public void a_ordem_deve_ser(Integer ordemEsperada) {
    assertEquals(ordemEsperada.intValue(), this.etapaAtual.getOrdem(), "A ordem da etapa não corresponde ao esperado.");
  }
  
  @Então("a ordem deve permanecer {int}")
  public void a_ordem_deve_permanecer(Integer ordemEsperada) {
    Etapa etapaEditada = etapaService.buscarPorId(this.etapaAtual.getId().getValor());
    assertEquals(ordemEsperada.intValue(), etapaEditada.getOrdem(), "A ordem deveria ter permanecido, mas foi alterada.");
  }

  @Então("o sistema deve rejeitar o cadastro da Etapa")
  public void o_sistema_deve_rejeitar_o_cadastro_da_etapa() {
    assertNotNull(ultimaExcecao, "Deveria ter lançado uma exceção, mas o cadastro foi aceito.");
  }
  
  @Então("as etapas devem ser reordenadas com sucesso")
  public void as_etapas_devem_ser_reordenadas_com_sucesso() {
    assertNull(ultimaExcecao, "A reordenação deveria ter sido bem-sucedida, mas lançou exceção: " + (ultimaExcecao != null ? ultimaExcecao.getMessage() : ""));
  }
  
  @Então("a etapa {string} deve ter a ordem {int}")
  public void a_etapa_deve_ter_a_ordem(String etapaId, Integer ordemEsperada) {
    Etapa etapaEncontrada = etapaService.buscarPorId(etapaId);
    assertNotNull(etapaEncontrada, "A etapa não foi encontrada após a reordenação.");
    assertEquals(ordemEsperada.intValue(), etapaEncontrada.getOrdem(), "A ordem da etapa " + etapaId + " está incorreta.");
  }
  
  @Então("o sistema deve rejeitar a operação")
  public void o_sistema_deve_rejeitar_a_operacao() {
    assertNotNull(ultimaExcecao, "A operação deveria ter sido rejeitada, mas não lançou exceção.");
  }
  
  @Então("a exceção deve conter a mensagem {string}")
  public void a_exceção_deve_conter_a_mensagem(String mensagemEsperada) {
    assertNotNull(ultimaExcecao, "Nenhuma exceção foi lançada. A operação falhou silenciosamente.");
    assertTrue(ultimaExcecao.getMessage().contains(mensagemEsperada), 
      "Mensagem de erro esperada: '" + mensagemEsperada + "', mas recebeu: '" + ultimaExcecao.getMessage() + "'");
  }
    

}