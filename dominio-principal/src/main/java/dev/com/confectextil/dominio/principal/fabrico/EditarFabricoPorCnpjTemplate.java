package dev.com.confectextil.dominio.principal.fabrico;

import java.util.Optional;

import dev.com.confectextil.dominio.principal.Fabrico;

class EditarFabricoPorCnpjTemplate extends FabricoOperacaoTemplate {

    private final String cnpjAlvo;
    private final String novoNome;

    private Fabrico existente;
    private String cnpjNormalizado;

    EditarFabricoPorCnpjTemplate(FabricoRepository repo, String cnpjAlvo, String novoNome) {
        super(repo);
        this.cnpjAlvo = cnpjAlvo;
        this.novoNome = novoNome;
    }

    @Override
    protected void validarEntrada() {
        if (cnpjAlvo == null || cnpjAlvo.isBlank()) {
            throw new IllegalArgumentException("CNPJ alvo é obrigatório");
        }
        cnpjNormalizado = normalizarCnpj(cnpjAlvo);
    }

    @Override
    protected Fabrico carregarOuCriar() {
        existente = buscarComFallback(cnpjAlvo, cnpjNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Fabrico não encontrado"));
        return existente;
    }

    @Override
    protected void aplicarAlteracoes(Fabrico fabrico) {
        if (novoNome != null && !novoNome.isBlank()) {
            fabrico.setNomeFantasia(novoNome);
        }
    }

    @Override
    protected Fabrico persistir(Fabrico fabrico) {
        return repo.editar(fabrico);
    }

    private Optional<Fabrico> buscarComFallback(String cnpjOriginal, String cnpjNormalizado) {
        Optional<Fabrico> fabricoOpt = repo.buscarPorCnpj(cnpjNormalizado);
        if (fabricoOpt.isEmpty() && !cnpjNormalizado.equals(cnpjOriginal.trim())) {
            fabricoOpt = repo.buscarPorCnpj(cnpjOriginal.trim());
        }
        if (fabricoOpt.isEmpty()) {
            String somenteDigitos = cnpjOriginal.replaceAll("\\D", "");
            if (!somenteDigitos.isBlank() && !somenteDigitos.equals(cnpjNormalizado)) {
                fabricoOpt = repo.buscarPorCnpj(somenteDigitos);
            }
        }
        return fabricoOpt;
    }
}
