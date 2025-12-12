
package dev.com.linnea;

import static org.springframework.boot.SpringApplication.run;

import java.io.IOException;
import java.util.List;

import dev.com.confectextil.dominio.principal.etapa.EtapaRepository;
import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import dev.com.confectextil.dominio.principal.etapa.EtapaStrategy;
import dev.com.confectextil.dominio.principal.etapa.strategy.EtapaProducaoStrategy;
import dev.com.confectextil.dominio.principal.etapa.strategy.EtapaQualidadeStrategy;
import dev.com.linnea.aplicacao.principal.modelo.ModeloRepositorioAplicacao;
import dev.com.linnea.aplicacao.principal.modelo.ModeloServicoAplicacao;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;


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
        List<EtapaStrategy> estrategias = List.of(
            new EtapaProducaoStrategy(),
            new EtapaQualidadeStrategy()
        );
        return new EtapaService(etapaRepository, estrategias);
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