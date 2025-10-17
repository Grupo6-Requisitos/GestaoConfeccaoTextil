package dev.com.confectextil.dominio.principal;

import dev.com.confectextil.dominio.principal.parceiro.ParceiroRepositorio;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroService;
import dev.com.confectextil.infraestrutura.persistencia.memoria.ParceiroRepositorioMock;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizarParceirosSteps {
    public ParceiroRepositorio repositorio;
    public ParceiroService service;
    private List<Parceiro> resultado;

    @Dado("que existem parceiros cadastrados no sistema")
    public void queExisteUmParceiroCadastrado(){
        repositorio = new ParceiroRepositorioMock();
        service = new ParceiroService(repositorio);
        service.cadastrar("P001", "Thiago", "81999998888");
        service.cadastrar("P002", "Maria", "81988887777");
    }
    @Dado("que nao existem parceiros cadastrados no sistema")
    public void queNaoExistemParceirosCadastradosNoSistema(){
        repositorio = new ParceiroRepositorioMock();
        service = new ParceiroService(repositorio);
    }

    @Quando("solicito para listar todos os parceiros")
    public void solicitoParaListarTodosOsParceiros(){
        resultado = service.listarTodos();
    }

    @Entao("o sistema deve exibir a lista de parceiros com seus nomes e telefones")
    public void sistemaExibe() {
        assertEquals(2, resultado.size());
        assertEquals("Thiago", resultado.get(0).getNome());
        assertEquals("Maria", resultado.get(1).getNome());
    }

    @Entao("o sistema deve informar que nao existem parceiros cadastrados")
    public void sistemaInforma(){
        assertTrue(resultado.isEmpty(), "A lista de parceiros esta vazia");
    }
}