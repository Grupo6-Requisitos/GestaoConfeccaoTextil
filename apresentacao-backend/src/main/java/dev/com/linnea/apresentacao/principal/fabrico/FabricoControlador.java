package dev.com.linnea.apresentacao.principal.fabrico;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import dev.com.confectextil.dominio.principal.Fabrico;
import dev.com.confectextil.dominio.principal.fabrico.FabricoService;
import dev.com.linnea.apresentacao.BackendMapeador;

@RestController
@RequestMapping("/fabrico")
public class FabricoControlador {
    
    private final FabricoService fabricoService;
    private final BackendMapeador mapeador;

    public FabricoControlador(FabricoService fabricoService, BackendMapeador mapeador) {
        this.fabricoService = fabricoService;
        this.mapeador = mapeador;
    }

    @PostMapping
    public Fabrico cadastrarFabrico(@RequestBody FabricoDto dto) {
        dto.id = null;
        var fabrico = mapeador.map(dto, Fabrico.class);
        return fabricoService.cadastrarFabrico(fabrico);
    }

    @PutMapping(path = "editar")
    public Fabrico atualizarFabricoPorCnpj(@RequestBody FabricoDto dto) {
        try {
            if (dto.cnpj == null || dto.cnpj.isBlank()) {
                throw new IllegalArgumentException("O CNPJ é obrigatório para edição por CNPJ");
            }

            return fabricoService.editarFabricoPorCnpj(dto.cnpj, dto.nomeFantasia);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
