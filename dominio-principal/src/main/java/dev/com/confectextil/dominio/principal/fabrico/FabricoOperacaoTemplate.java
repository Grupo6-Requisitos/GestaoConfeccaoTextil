package dev.com.confectextil.dominio.principal.fabrico;

import dev.com.confectextil.dominio.principal.Fabrico;

/**
 * Template Method para operações de Fabrico.
 * Define o esqueleto de salvar/editar, permitindo ganchos de validação e pós-processamento.
 */
abstract class FabricoOperacaoTemplate {

    protected final FabricoRepository repo;

    protected FabricoOperacaoTemplate(FabricoRepository repo) {
        this.repo = repo;
    }

    public final Fabrico executar() {
        validarEntrada();
        Fabrico alvo = carregarOuCriar();
        aplicarAlteracoes(alvo);
        validarNegocio(alvo);
        Fabrico persisted = persistir(alvo);
        posPersistir(persisted);
        return persisted;
    }

    protected abstract void validarEntrada();

    protected abstract Fabrico carregarOuCriar();

    protected abstract void aplicarAlteracoes(Fabrico fabrico);

    protected void validarNegocio(Fabrico fabrico) {
        // gancho opcional
    }

    protected Fabrico persistir(Fabrico fabrico) {
        repo.salvar(fabrico);
        return fabrico;
    }

    protected void posPersistir(Fabrico fabrico) {
        // gancho opcional
    }

    protected String normalizarCnpj(String cnpj) {
        if (cnpj == null) {
            return null;
        }
        String digits = cnpj.replaceAll("\\D", "");
        if (digits.length() == 14) {
            return String.format("%s.%s.%s/%s-%s",
                    digits.substring(0, 2),
                    digits.substring(2, 5),
                    digits.substring(5, 8),
                    digits.substring(8, 12),
                    digits.substring(12, 14));
        }
        return cnpj.trim();
    }
}
