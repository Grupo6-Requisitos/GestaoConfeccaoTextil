package dev.com.confectextil.dominio.principal.fabrico;

import dev.com.confectextil.dominio.principal.Fabrico;

public class FabricoService {

    private final FabricoRepository repository;

    public FabricoService(FabricoRepository repository) {
        this.repository = repository;
    }

    // Autor: Arthur
    // Funcionalidades Cadastrar Fabrico
    public Fabrico cadastrarFabrico(String fabricoId, String nomeFantasia, String cnpj) {
        FabricoId id = FabricoId.novo(fabricoId);
        
        if (repository.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Fabrico já cadastrado");
        }

        Fabrico fabrico = new Fabrico(id, nomeFantasia, cnpj);
        repository.salvar(fabrico);
        return fabrico;
    }

    // Autor: Arthur
    // Funcionalidade Editar Fabrico
    public Fabrico editarFabrico(String fabricoId, String novoNomeFantasia, String novoCnpj) {
        FabricoId id = FabricoId.novo(fabricoId);
        
        Fabrico fabrico = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Fabrico não encontrado"));

        if (novoNomeFantasia != null && !novoNomeFantasia.isBlank()) {
            fabrico.setNomeFantasia(novoNomeFantasia);
        }
        
        if (novoCnpj != null && !novoCnpj.isBlank()) {
            fabrico.setCnpj(novoCnpj);
        }

        return repository.editar(fabrico);
    }

    public boolean existe(String fabricoId) {
        FabricoId id = FabricoId.novo(fabricoId);
        return repository.buscarPorId(id).isPresent();
    }

    public Fabrico buscarPorId(String fabricoId) {
        FabricoId id = FabricoId.novo(fabricoId);
        return repository.buscarPorId(id).orElse(null);
    }
}
