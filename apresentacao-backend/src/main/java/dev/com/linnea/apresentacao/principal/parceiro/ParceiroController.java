package dev.com.linnea.apresentacao.principal.parceiro;

import dev.com.linnea.aplicacao.principal.parceiro.ParceiroServicoAplicacao;
import dev.com.confectextil.dominio.principal.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ParceiroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/parceiros")
public class ParceiroController {
    private final ParceiroService parceiroService;
    private final ParceiroServicoAplicacao parceiroAppService;

    public ParceiroController(ParceiroService parceiroService, ParceiroServicoAplicacao parceiroAppService) {
        this.parceiroService = parceiroService;
        this.parceiroAppService = parceiroAppService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrarParceiro(@RequestBody CadastrarParceiroRequest request) {
        try {
            parceiroService.cadastrar(
                    request.id(),
                    request.nome(),
                    request.telefone()
            );
            Parceiro parceiro = parceiroService.buscarPorId(request.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(ParceiroResponse.fromDomain(parceiro));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Erro interno ao cadastrar parceiro"));
        }
    }

    @GetMapping
    public ResponseEntity<List<ParceiroResponse>> listarTodosParceiros() {
        try {
            var listaResumida = parceiroAppService.listarResumo();

            List<ParceiroResponse> response = listaResumida.stream()
                    .map(resumo -> new ParceiroResponse(
                            resumo.getId(),
                            resumo.getNome(),
                            resumo.getTelefone()
                    ))
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarParceiro(@PathVariable String id) {
        try {
            Parceiro parceiro = parceiroService.buscarPorId(id);
            if (parceiro == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Parceiro não encontrado"));
            }
            return ResponseEntity.ok(ParceiroResponse.fromDomain(parceiro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Erro interno ao buscar parceiro"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarParceiro(@PathVariable String id, @RequestBody CadastrarParceiroRequest request) {
        try {
            Parceiro atualizado = parceiroService.editar(
                id,
                request.nome(),
                request.telefone()
            );
            return ResponseEntity.ok(ParceiroResponse.fromDomain(atualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Erro interno ao editar parceiro"));
        }
    }

    public record CadastrarParceiroRequest(
            String id,
            String nome,
            String telefone
    ) {
        @Override
        public String id() { return id == null ? null : id.trim(); }
        @Override
        public String nome() { return nome == null ? null : nome.trim(); }
        @Override
        public String telefone() { return telefone == null ? null : telefone.trim(); }
    }

    public record ParceiroResponse(
            String id,
            String nome,
            String telefone
    ) {
        public static ParceiroResponse fromDomain(Parceiro parceiro) {
            return new ParceiroResponse(
                    parceiro.getId().getId(),
                    parceiro.getNome(),
                    parceiro.getTelefone()
            );
        }
    }

    public record ErrorResponse(String mensagem) {
    }
}
