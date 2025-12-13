package dev.com.linnea.apresentacao.principal.etapa;

import dev.com.confectextil.dominio.principal.Etapa;
import dev.com.confectextil.dominio.principal.etapa.EtapaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/etapas")
@CrossOrigin(origins = "*")
public class EtapaController {

    private final EtapaService etapaService;

    public EtapaController(EtapaService etapaService) {
        this.etapaService = etapaService;
    }

    @PostMapping
    public ResponseEntity<?> criarEtapa(@RequestBody EtapaDTO dto) {
        // PRINT DE DEPURAÇÃO - INÍCIO
        System.out.println("\n========================================");
        System.out.println(">>> [JAVA] RECEBENDO POST /api/etapas");
        System.out.println(">>> [JAVA] PAYLOAD BRUTO:");
        System.out.println("    ID:    " + dto.getId());
        System.out.println("    Nome:  " + dto.getNome());
        System.out.println("    Ordem: " + dto.getOrdem());
        System.out.println("    Tipo:  " + dto.getTipo());
        System.out.println("========================================\n");

        try {
            System.out.println(">>> [JAVA] Processando tipo (toUpperCase)...");
            String tipo = dto.getTipo() != null ? dto.getTipo().trim().toUpperCase() : null;
            System.out.println(">>> [JAVA] Tipo processado: " + tipo);

            System.out.println(">>> [JAVA] Chamando etapaService.cadastrarEtapa()...");
            Etapa etapa = etapaService.cadastrarEtapa(
                    dto.getId(),
                    dto.getNome(),
                    dto.getOrdem(),
                    tipo
            );

            System.out.println("<<< [JAVA] SUCESSO! Etapa criada no banco.");
            System.out.println("<<< [JAVA] ID Final: " + etapa.getId().getValor());

            return new ResponseEntity<>(toDTO(etapa), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // PRINT DE ERRO DE VALIDAÇÃO
            System.out.println("\n!!! [JAVA] ERRO DE VALIDAÇÃO (400) !!!");
            System.out.println("Mensagem: " + e.getMessage());
            e.printStackTrace(); // Mostra onde no código deu erro
            System.out.println("----------------------------------------\n");

            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));

        } catch (Exception e) {
            // PRINT DE ERRO GERAL
            System.out.println("\n!!! [JAVA] ERRO INTERNO (500) !!!");
            System.out.println("Mensagem: " + e.getMessage());
            e.printStackTrace(); // Mostra a pilha de erro completa
            System.out.println("----------------------------------------\n");

            return ResponseEntity.internalServerError().body(new ErrorResponse("Erro interno: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtapaDTO> editarEtapa(@PathVariable String id, @RequestBody EtapaDTO dto) {
        System.out.println(">>> [JAVA] Editando etapa ID: " + id);
        Etapa etapa = etapaService.editarEtapa(id, dto.getNome(), dto.getOrdem());
        return ResponseEntity.ok(toDTO(etapa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtapaDTO> buscarPorId(@PathVariable String id) {
        System.out.println(">>> [JAVA] Buscando por ID: " + id);
        Etapa etapa = etapaService.buscarPorId(id);
        if (etapa == null) {
            System.out.println("!!! [JAVA] ID não encontrado.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(etapa));
    }

    @GetMapping
    public ResponseEntity<List<EtapaDTO>> listarTodas() {
        System.out.println(">>> [JAVA] Listando todas as etapas...");
        List<EtapaDTO> lista = etapaService.listarTodos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        System.out.println("<<< [JAVA] Total encontrado: " + lista.size());
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEtapa(@PathVariable String id) {
        System.out.println(">>> [JAVA] Excluindo ID: " + id);
        etapaService.excluirEtapa(id);
        return ResponseEntity.noContent().build();
    }

    private EtapaDTO toDTO(Etapa etapa) {
        EtapaDTO dto = new EtapaDTO();
        dto.setId(etapa.getId().getValor());
        dto.setNome(etapa.getNome());
        dto.setOrdem(etapa.getOrdem());
        dto.setTipo(etapa.getTipo());
        return dto;
    }

    public record ErrorResponse(String message) {}

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

        @Override
        public String toString() {
            return "EtapaDTO{id='" + id + "', nome='" + nome + "', ordem=" + ordem + ", tipo='" + tipo + "'}";
        }
    }
}