package dev.com.linnea;

import static org.springframework.boot.SpringApplication.run;

import java.io.IOException;
import java.util.List;

import dev.com.confectextil.dominio.principal.etapa.EtapaRepository;
import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import dev.com.confectextil.dominio.principal.etapa.EtapaStrategy;
import dev.com.confectextil.dominio.principal.etapa.strategy.EtapaProducaoStrategy;
import dev.com.confectextil.dominio.principal.etapa.strategy.EtapaQualidadeStrategy;
import dev.com.confectextil.dominio.principal.fabrico.FabricoRepository;
import dev.com.confectextil.dominio.principal.fabrico.FabricoService;
import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloService;
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


@SpringBootApplication
public class AplicacaoBackend {

	@Bean
	public FabricoService fabricoService(FabricoRepository fabricoRepo) {
    return new FabricoService(fabricoRepo);
	}
	

	@Bean
	public ModeloService modeloService(ModeloRepository modeloRepo, InsumoRepository insumoRepo) {
    return new ModeloService(modeloRepo, insumoRepo);
	}

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

	@Bean
	public ParceiroService parceiroService(ParceiroRepositorio repositorio, List<ObservadorNovoParceiro> observadores) {
		return new ParceiroService(repositorio, observadores);
	}

	@Bean
	public ParceiroServicoAplicacao parceiroServicoAplicacao(ParceiroService parceiroService,ParceiroRepositorioAplicacao repositorioAplicacao) {
		return new ParceiroServicoAplicacao(parceiroService, repositorioAplicacao);
	}

	public static void main(String[] args) throws IOException {
		run(AplicacaoBackend.class, args);
	}

}