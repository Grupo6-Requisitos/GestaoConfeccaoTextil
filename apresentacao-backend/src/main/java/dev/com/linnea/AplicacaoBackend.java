package dev.com.linnea;

import static org.springframework.boot.SpringApplication.run;

import java.io.IOException;

import dev.com.confectextil.dominio.principal.etapa.EtapaRepository;
import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import dev.com.linnea.aplicacao.principal.modelo.ModeloRepositorioAplicacao;
import dev.com.linnea.aplicacao.principal.modelo.ModeloServicoAplicacao;
import dev.com.confectextil.dominio.principal.parceiro.ObservadorNovoParceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroRepositorio;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroService;
import dev.com.linnea.aplicacao.principal.parceiro.ParceiroRepositorioAplicacao;
import dev.com.linnea.aplicacao.principal.parceiro.ParceiroServicoAplicacao;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

import java.util.List;

@SpringBootApplication
public class AplicacaoBackend {

	@Bean
	public ModeloServicoAplicacao modeloServicoAplicacao(ModeloRepositorioAplicacao repositorio) {
		return new ModeloServicoAplicacao(repositorio);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	  @Bean
    public EtapaService etapaService(EtapaRepository etapaRepository) {
        return new EtapaService(etapaRepository);
    }

	@Bean
	public ParceiroService parceiroService(ParceiroRepositorio repositorio, List<ObservadorNovoParceiro> observadores) {
		return new ParceiroService(repositorio, observadores);
	}

	@Bean
	public ParceiroServicoAplicacao parceiroServicoAplicacao(ParceiroService parceiroService,ParceiroRepositorioAplicacao repositorioAplicacao) {
		return new ParceiroServicoAplicacao(parceiroService, repositorioAplicacao);
	}

	/* TEM QUE TROCAR
	@Bean
	public AutorServico autorServico(AutorRepositorio repositorio) {
		return new AutorServico(repositorio);
	}

	@Bean
	public ExemplarServico exemplarServico(ExemplarRepositorio repositorio) {
		return new ExemplarServico(repositorio);
	}

	@Bean
	public ExemplarServicoAplicacao exemplarServicoAplicacao(ExemplarRepositorioAplicacao repositorio) {
		return new ExemplarServicoAplicacao(repositorio);
	}

	@Bean
	public EmprestimoServico emprestimoServico(ExemplarRepositorio exemplarRepositorio, EventoBarramento barramento) {
		return new EmprestimoServico(exemplarRepositorio, barramento);
	}

	@Bean
	public LivroServico livroServico(LivroRepositorio repositorio) {
		return new LivroServico(repositorio);
	}

	@Bean
	public LivroServicoAplicacao livroServicoAplicacao(LivroRepositorioAplicacao repositorio) {
		return new LivroServicoAplicacao(repositorio);
	}

	@Bean
	public EmprestimoRegistroServicoAplicacao emprestimoRegistroServicoAplicacao(
			EmprestimoRegistroRepositorio repositorio, EmprestimoRegistroRepositorioAplicacao repositorioAplicacao,
			EventoBarramento servico) {
		return new EmprestimoRegistroServicoAplicacao(repositorio, repositorioAplicacao, servico);
	}
*/

	public static void main(String[] args) throws IOException {
		run(AplicacaoBackend.class, args);
	}

}