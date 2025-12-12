package dev.com.linnea.apresentacao.principal.etapa;

import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/etapas")
public class EtapaController {

    private final EtapaService etapaService;

    public EtapaController(EtapaService etapaService) {
        this.etapaService = etapaService;
    }

    // ---------- Criar nova etapa ----------
    @PostMapping
    public ResponseEntity<Etapa> criarEtapa(@RequestBody EtapaDTO dto) {
        Etapa etapa = etapaService.cadastrarEtapa(dto.getId(), dto.getNome(), dto.getOrdem());
        return new ResponseEntity<>(etapa, HttpStatus.CREATED);
    }

    // ---------- Editar etapa ----------
    @PutMapping("/{id}")
    public ResponseEntity<Etapa> editarEtapa(@PathVariable String id, @RequestBody EtapaDTO dto) {
        Etapa etapa = etapaService.editarEtapa(id, dto.getNome(), dto.getOrdem());
        return ResponseEntity.ok(etapa);
    }

    // ---------- Buscar etapa por ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<Etapa> buscarPorId(@PathVariable String id) {
        Etapa etapa = etapaService.buscarPorId(id);
        if (etapa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(etapa);
    }

    // ---------- Listar todas as etapas ----------
    @GetMapping
    public ResponseEntity<List<EtapaDTO>> listarTodas() {
        List<EtapaDTO> etapas = etapaService.listarTodos().stream()
                .map(e -> {
                    EtapaDTO dto = new EtapaDTO();
                    dto.setId(e.getId().getValor());
                    dto.setNome(e.getNome());
                    dto.setOrdem(e.getOrdem());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(etapas);
    }

    // ---------- Excluir etapa ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEtapa(@PathVariable String id) {
        etapaService.excluirEtapa(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- DTO simples para transferência ----------
    public static class EtapaDTO {
        private String id;
        private String nome;
        private Integer ordem;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Integer getOrdem() { return ordem; }
        public void setOrdem(Integer ordem) { this.ordem = ordem; }
    }
}
