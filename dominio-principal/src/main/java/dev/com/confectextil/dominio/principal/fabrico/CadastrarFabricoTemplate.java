package dev.com.confectextil.dominio.principal.fabrico;

import dev.com.confectextil.dominio.principal.Fabrico;
import dev.com.confectextil.dominio.principal.fabrico.FabricoId;

class CadastrarFabricoTemplate extends FabricoOperacaoTemplate {

    private final String idValor;
    private final String nomeFantasia;
    private final String cnpjOriginal;

    CadastrarFabricoTemplate(FabricoRepository repo, String idValor, String nomeFantasia, String cnpjOriginal) {
        super(repo);
        this.idValor = idValor;
        this.nomeFantasia = nomeFantasia;
        this.cnpjOriginal = cnpjOriginal;
    }

    private Fabrico novoFabrico;
    private String cnpjNormalizado;

    @Override
    protected void validarEntrada() {
        if (nomeFantasia == null || nomeFantasia.isBlank()) {
            throw new IllegalArgumentException("Nome fantasia é obrigatório");
        }
        if (cnpjOriginal == null || cnpjOriginal.isBlank()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
        cnpjNormalizado = normalizarCnpj(cnpjOriginal);
    }

    @Override
    protected Fabrico carregarOuCriar() {
        FabricoId id = FabricoId.novo(idValor);
        novoFabrico = new Fabrico(id, nomeFantasia, cnpjNormalizado);
        return novoFabrico;
    }

    @Override
    protected void aplicarAlteracoes(Fabrico fabrico) {
        // Nenhuma alteração adicional necessária para criação
    }

    @Override
    protected void validarNegocio(Fabrico fabrico) {
        if (repo.buscarPorId(fabrico.getId()).isPresent()) {
            throw new IllegalArgumentException("Fabrico já cadastrado");
        }
        repo.buscarPorCnpj(cnpjNormalizado).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe Fabrico cadastrado com este CNPJ");
        });
    }
}
