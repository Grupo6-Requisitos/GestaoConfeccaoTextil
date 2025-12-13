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
        return new CadastrarFabricoTemplate(repository, fabricoId, nomeFantasia, cnpj).executar();
    }

    public Fabrico editarFabrico(String fabricoId, String novoNomeFantasia, String novoCnpj) {
        FabricoId id = FabricoId.novo(fabricoId);
        return new EditarFabricoPorIdTemplate(repository, id, novoNomeFantasia, novoCnpj).executar();
    }

    public Fabrico cadastrarFabrico(Fabrico fabrico) {
        if (fabrico == null) {
            throw new IllegalArgumentException("Fabrico não pode ser nulo");
        }

        String idValor = fabrico.getId() != null ? fabrico.getId().valor() : java.util.UUID.randomUUID().toString();
        return new CadastrarFabricoTemplate(
            repository,
            idValor,
            fabrico.getNomeFantasia(),
            fabrico.getCnpj()
        ).executar();
    }

    public boolean existe(String fabricoId) {
        FabricoId id = FabricoId.novo(fabricoId);
        return repository.buscarPorId(id).isPresent();
    }

    public Fabrico buscarPorId(String fabricoId) {
        FabricoId id = FabricoId.novo(fabricoId);
        return repository.buscarPorId(id).orElse(null);
    }

    public Fabrico editarFabricoPorCnpj(String cnpjAlvo, String novoNomeFantasia) {
        return new EditarFabricoPorCnpjTemplate(repository, cnpjAlvo, novoNomeFantasia).executar();
    }
}
