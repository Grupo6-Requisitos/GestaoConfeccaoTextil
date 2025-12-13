package dev.com.linnea.apresentacao.principal.modelo;

import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import dev.com.linnea.aplicacao.principal.modelo.ModeloResumo;
import dev.com.linnea.aplicacao.principal.modelo.ModeloServicoAplicacao;
import dev.com.linnea.apresentacao.BackendMapeador;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/modelos")
public class ModeloControlador {

    private final ModeloService modeloService;
    private final ModeloServicoAplicacao modeloServicoConsulta;
    private final BackendMapeador mapeador;

    public ModeloControlador(ModeloService modeloService, ModeloServicoAplicacao modeloServicoConsulta, BackendMapeador mapeador) {
        this.modeloService = modeloService;
        this.modeloServicoConsulta = modeloServicoConsulta;
        this.mapeador = mapeador;
    }

    @GetMapping
    public List<ModeloResumo> listar() {
        // Exemplo de uso do iterator vindo da camada de aplicação (pattern Iterator)
        var iter = modeloServicoConsulta.iterarTodosResumo();
        return streamFromIterable(iter).toList();
    }

    @GetMapping("/{referencia}")
    public ModeloResumo buscar(@PathVariable String referencia) {
        ModeloResumo resumo = modeloServicoConsulta.listarEspecificoResumo(referencia);
        if (resumo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado");
        }
        return resumo;
    }

    @PostMapping
    public ModeloDto criar(@RequestBody ModeloDto dto) {
        var insumos = dto.insumosPadrao == null ? List.<dev.com.confectextil.dominio.principal.modelo.ModeloService.InsumoPadraoDTO>of()
            : dto.insumosPadrao.stream()
                .map(ip -> new dev.com.confectextil.dominio.principal.modelo.ModeloService.InsumoPadraoDTO(ip.insumoId, ip.quantidadeSugerida))
                .collect(Collectors.toList());

        var criado = modeloService.cadastrarModelo(dto.referencia, dto.nome, dto.imagemUrl, insumos);
        return mapeador.map(criado, ModeloDto.class);
    }

    @PutMapping("/{referencia}")
    public ModeloDto atualizar(@PathVariable String referencia, @RequestBody ModeloDto dto) {
        try {
            var insumos = dto.insumosPadrao == null ? List.<dev.com.confectextil.dominio.principal.modelo.ModeloService.InsumoPadraoDTO>of()
                : dto.insumosPadrao.stream()
                    .map(ip -> new dev.com.confectextil.dominio.principal.modelo.ModeloService.InsumoPadraoDTO(ip.insumoId, ip.quantidadeSugerida))
                    .collect(Collectors.toList());

            var atualizado = modeloService.atualizarModelo(referencia, dto.referencia, dto.nome, insumos);
            return mapeador.map(atualizado, ModeloDto.class);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{referencia}")
    public void remover(@PathVariable String referencia) {
        try {
            modeloService.removerModeloPorReferencia(referencia);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private <T> java.util.stream.Stream<T> streamFromIterable(Iterable<T> iterable) {
        return java.util.stream.StreamSupport.stream(iterable.spliterator(), false);
    }
}