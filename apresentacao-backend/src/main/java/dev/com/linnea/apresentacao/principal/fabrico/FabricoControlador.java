package dev.com.linnea.apresentacao.principal.fabrico;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.com.confectextil.dominio.principal.Fabrico;
import dev.com.confectextil.dominio.principal.fabrico.FabricoService;
import dev.com.linnea.apresentacao.BackendMapeador;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/fabricos")
public class FabricoControlador {
    
    private final FabricoService fabricoService;
    private final BackendMapeador mapeador;

    public FabricoControlador(FabricoService fabricoService, BackendMapeador mapeador) {
        this.fabricoService = fabricoService;
        this.mapeador = mapeador;
    }

    @GetMapping
    public List<FabricoDto> listar() {
        return fabricoService.listarTodos().stream()
                .map(f -> mapeador.map(f, FabricoDto.class))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        Fabrico encontrado = fabricoService.buscarPorId(id);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroResposta("Fabrico não encontrado"));
        }
        return ResponseEntity.ok(mapeador.map(encontrado, FabricoDto.class));
    }

    @PostMapping
    public ResponseEntity<?> cadastrarFabrico(@RequestBody FabricoDto dto) {
        try {
            dto.id = null;
            var fabrico = mapeador.map(dto, Fabrico.class);
            var salvo = fabricoService.cadastrarFabrico(fabrico);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapeador.map(salvo, FabricoDto.class));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResposta(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFabrico(@PathVariable String id, @RequestBody FabricoDto dto) {
        try {
            var atualizado = fabricoService.editarFabrico(id, dto.nomeFantasia, dto.cnpj);
            return ResponseEntity.ok(mapeador.map(atualizado, FabricoDto.class));
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage() != null && e.getMessage().toLowerCase().contains("não encontrado")
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(new ErroResposta(e.getMessage()));
        }
    }

    public record ErroResposta(String mensagem) {
    }
}
