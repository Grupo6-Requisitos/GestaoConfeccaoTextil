package dev.com.confectextil.dominio.principal.fabrico;

import dev.com.confectextil.dominio.principal.Fabrico;

class EditarFabricoPorIdTemplate extends FabricoOperacaoTemplate {

    private final FabricoId id;
    private final String novoNome;
    private final String novoCnpj;

    private Fabrico existente;

    EditarFabricoPorIdTemplate(FabricoRepository repo, FabricoId id, String novoNome, String novoCnpj) {
        super(repo);
        this.id = id;
        this.novoNome = novoNome;
        this.novoCnpj = novoCnpj;
    }

    @Override
    protected void validarEntrada() {
        if (id == null) {
            throw new IllegalArgumentException("ID do Fabrico é obrigatório");
        }
    }

    @Override
    protected Fabrico carregarOuCriar() {
        existente = repo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Fabrico não encontrado"));
        return existente;
    }

    @Override
    protected void aplicarAlteracoes(Fabrico fabrico) {
        if (novoNome != null && !novoNome.isBlank()) {
            fabrico.setNomeFantasia(novoNome);
        }

        if (novoCnpj != null && !novoCnpj.isBlank()) {
            String novoCnpjNormalizado = normalizarCnpj(novoCnpj);
            if (!novoCnpjNormalizado.equals(fabrico.getCnpj())) {
                throw new IllegalArgumentException("CNPJ é imutável para Fabrico");
            }
        }
    }

    @Override
    protected Fabrico persistir(Fabrico fabrico) {
        return repo.editar(fabrico);
    }
}
