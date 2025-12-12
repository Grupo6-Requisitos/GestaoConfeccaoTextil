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

    // Criar etapa
    @PostMapping
    public ResponseEntity<EtapaDTO> criarEtapa(@RequestBody EtapaDTO dto) {
        Etapa etapa = etapaService.cadastrarEtapa(
                dto.getId(), 
                dto.getNome(), 
                dto.getOrdem(), 
                dto.getTipo()
        );

        return new ResponseEntity<>(toDTO(etapa, dto.getTipo()), HttpStatus.CREATED);
    }

    // Editar etapa
    @PutMapping("/{id}")
    public ResponseEntity<EtapaDTO> editarEtapa(@PathVariable String id, @RequestBody EtapaDTO dto) {
        Etapa etapa = etapaService.editarEtapa(id, dto.getNome(), dto.getOrdem());
        return ResponseEntity.ok(toDTO(etapa, dto.getTipo()));
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<EtapaDTO> buscarPorId(@PathVariable String id) {
        Etapa etapa = etapaService.buscarPorId(id);
        if (etapa == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(toDTO(etapa, null));
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<EtapaDTO>> listarTodas() {
        List<EtapaDTO> lista = etapaService.listarTodos().stream()
                .map(e -> toDTO(e, null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // Excluir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEtapa(@PathVariable String id) {
        etapaService.excluirEtapa(id);
        return ResponseEntity.noContent().build();
    }

    // Conversor interno
    private EtapaDTO toDTO(Etapa etapa, String tipo) {
        EtapaDTO dto = new EtapaDTO();
        dto.setId(etapa.getId().getValor());
        dto.setNome(etapa.getNome());
        dto.setOrdem(etapa.getOrdem());
        dto.setTipo(tipo);
        return dto;
    }

    // DTO
    public static class EtapaDTO {
        private String id;
        private String nome;
        private Integer ordem;
        private String tipo;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Integer getOrdem() { return ordem; }
        public void setOrdem(Integer ordem) { this.ordem = ordem; }

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
    }
}
