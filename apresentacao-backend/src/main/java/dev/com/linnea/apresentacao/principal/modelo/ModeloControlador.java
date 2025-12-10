package dev.com.linnea.apresentacao.principal.modelo;

// import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import dev.com.linnea.aplicacao.principal.modelo.ModeloResumo;
import dev.com.linnea.aplicacao.principal.modelo.ModeloServicoAplicacao;

// import java.net.URI;
import java.util.List;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/modelos")
public class ModeloControlador {

    private final ModeloRepository repository;
    private final ModeloServicoAplicacao servicoRepository;

    public ModeloControlador(ModeloRepository repository, ModeloServicoAplicacao servicoRepository) {
        this.repository = repository;
        this.servicoRepository = servicoRepository;
    }

    @GetMapping
    public List<ModeloResumo> listar() {
        return servicoRepository.listarModelosResumo();
    }

    // @GetMapping("/{referencia}")
    // public ResponseEntity<Modelo> buscar(@PathVariable String referencia) {
    //     return repository.buscarPorReferencia(referencia)
    //             .map(ResponseEntity::ok)
    //             .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // @PostMapping
    // public ResponseEntity<Modelo> criar(@RequestBody ModeloRequisicao dto, UriComponentsBuilder uriBuilder) {
    //     Modelo novo = new Modelo(dto.referencia(), dto.nome(), dto.imagemUrl());
    //     repository.salvar(novo);
    //     URI location = uriBuilder.path("/modelos/{referencia}").buildAndExpand(novo.getReferencia()).toUri();
    //     return ResponseEntity.created(location).body(novo);
    // }

    // @PutMapping("/{referencia}")
    // public ResponseEntity<Modelo> atualizar(@PathVariable String referencia, @RequestBody ModeloRequisicao dto) {
    //     return repository.buscarPorReferencia(referencia)
    //             .map(existente -> {
    //                 Modelo atualizado = new Modelo(
    //                         existente.getId(),
    //                         dto.referencia() != null ? dto.referencia() : existente.getReferencia(),
    //                         dto.nome() != null ? dto.nome() : existente.getNome(),
    //                         dto.imagemUrl() != null ? dto.imagemUrl() : existente.getImagemUrl(),
    //                         existente.getInsumosPadrao()
    //                 );
    //                 repository.atualizar(referencia, atualizado);
    //                 return ResponseEntity.ok(atualizado);
    //             })
    //             .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // @DeleteMapping("/{referencia}")
    // public ResponseEntity<Void> deletar(@PathVariable String referencia) {
    //     if (repository.buscarPorReferencia(referencia).isEmpty()) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     repository.removerPorReferencia(referencia);
    //     return ResponseEntity.noContent().build();
    // }

    // public record ModeloRequisicao(String referencia, String nome, String imagemUrl) {}
}
