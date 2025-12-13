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
        cnpj = normalizarCnpj(cnpj);
        FabricoId id = FabricoId.novo(fabricoId);
        
        if (repository.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Fabrico já cadastrado");
        }
        repository.buscarPorCnpj(cnpj).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe Fabrico cadastrado com este CNPJ");
        });

        Fabrico fabrico = new Fabrico(id, nomeFantasia, cnpj);
        repository.salvar(fabrico);
        return fabrico;
    }

    public Fabrico editarFabrico(String fabricoId, String novoNomeFantasia, String novoCnpj) {
        FabricoId id = FabricoId.novo(fabricoId);
        
        Fabrico fabrico = repository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Fabrico não encontrado"));

        if (novoNomeFantasia != null && !novoNomeFantasia.isBlank()) {
            fabrico.setNomeFantasia(novoNomeFantasia);
        }
        
        if (novoCnpj != null && !novoCnpj.isBlank() && !normalizarCnpj(novoCnpj).equals(fabrico.getCnpj())) {
            throw new IllegalArgumentException("CNPJ é imutável para Fabrico");
        }

        return repository.editar(fabrico);
    }

    public Fabrico cadastrarFabrico(Fabrico fabrico) {
        if (fabrico == null) {
            throw new IllegalArgumentException("Fabrico não pode ser nulo");
        }

        String cnpjNormalizado = normalizarCnpj(fabrico.getCnpj());

        repository.buscarPorCnpj(cnpjNormalizado).ifPresent(existing -> {
            throw new IllegalArgumentException("Já existe Fabrico cadastrado com este CNPJ");
        });

        String idValor = fabrico.getId() != null ? fabrico.getId().valor() : java.util.UUID.randomUUID().toString();

        return cadastrarFabrico(
            idValor,
            fabrico.getNomeFantasia(),
            cnpjNormalizado
        );
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
        if (cnpjAlvo == null || cnpjAlvo.isBlank()) {
            throw new IllegalArgumentException("CNPJ alvo é obrigatório");
        }

        String cnpjNormalizado = normalizarCnpj(cnpjAlvo);
        var fabricoOpt = repository.buscarPorCnpj(cnpjNormalizado);
        if (fabricoOpt.isEmpty() && !cnpjNormalizado.equals(cnpjAlvo.trim())) {
            // tenta também com o valor original (pode já estar formatado no banco)
            fabricoOpt = repository.buscarPorCnpj(cnpjAlvo.trim());
        }
        if (fabricoOpt.isEmpty()) {
            // última tentativa: comparar apenas os dígitos
            String somenteDigitos = cnpjAlvo.replaceAll("\\D", "");
            if (!somenteDigitos.isBlank() && !somenteDigitos.equals(cnpjNormalizado)) {
                fabricoOpt = repository.buscarPorCnpj(somenteDigitos);
            }
        }

        Fabrico fabrico = fabricoOpt
            .orElseThrow(() -> new IllegalArgumentException("Fabrico não encontrado"));

        if (novoNomeFantasia != null && !novoNomeFantasia.isBlank()) {
            fabrico.setNomeFantasia(novoNomeFantasia);
        }

        return repository.editar(fabrico);
    }

    private String normalizarCnpj(String cnpj) {
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
