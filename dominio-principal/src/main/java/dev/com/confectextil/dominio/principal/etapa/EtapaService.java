package dev.com.confectextil.dominio.principal.etapa;

import dev.com.confectextil.dominio.principal.Etapa;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EtapaService {

    private final EtapaRepository repository;
    private final List<EtapaStrategy> estrategias;

    public EtapaService(EtapaRepository repository) {
        this.repository = repository;
        this.estrategias = List.of();
    }

    public EtapaService(EtapaRepository repository, List<EtapaStrategy> estrategias) {
        this.repository = repository;
        this.estrategias = estrategias != null ? estrategias : List.of();
    }

    public Etapa cadastrarEtapa(String etapaId, String nome, int ordem, String tipo) {
        EtapaId id = EtapaId.novo(etapaId);

        if (repository.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Etapa já cadastrada");
        }

        // --- VALIDAÇÃO DE ORDEM ÚNICA RECOMENDADA ---
        boolean ordemExiste = repository.listarTodos().stream()
                .anyMatch(e -> e.getOrdem() == ordem);

        if (ordemExiste) {
            throw new IllegalArgumentException("Já existe uma etapa com a ordem " + ordem + ". Escolha outra.");
        }
        // --------------------------------------------

        // CRIA O OBJETO PASSANDO O TIPO
        Etapa etapa = new Etapa(id, nome, ordem, tipo);

        if (tipo != null && !tipo.isBlank() && estrategias != null && !estrategias.isEmpty()) {
            String tipoNormalizado = tipo.trim().toUpperCase();
            estrategias.stream()
                    .filter(e -> e.seAplica(tipoNormalizado))
                    .findFirst()
                    .ifPresent(e -> e.processarRegras(etapa));
        }

        repository.salvar(etapa);
        return etapa;
    }

    public Etapa cadastrarEtapa(String etapaId, String nome, int ordem) {
        return cadastrarEtapa(etapaId, nome, ordem, null);
    }

    public Etapa editarEtapa(String etapaId, String novoNome, Integer novaOrdem) {
        EtapaId id = EtapaId.novo(etapaId);

        Etapa etapa = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Etapa não encontrada"));

        etapa.atualizar(novoNome, novaOrdem);
        return repository.editar(etapa);
    }

    public List<Etapa> listarTodos() {
        return repository.listarTodos();
    }

    public Etapa buscarPorId(String etapaId) {
        EtapaId id = EtapaId.novo(etapaId);
        return repository.buscarPorId(id).orElse(null);
    }

    public void excluirEtapa(String etapaId) {
        EtapaId id = EtapaId.novo(etapaId);
        Etapa etapa = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Etapa não encontrada para exclusão."));
        repository.excluir(etapa.getId());
    }

    public boolean existe(String etapaId) {
        EtapaId id = EtapaId.novo(etapaId);
        return repository.buscarPorId(id).isPresent();
    }

    public void reordenarEtapas(Map<String, Integer> novasOrdensPorId) {
        if (novasOrdensPorId == null || novasOrdensPorId.isEmpty()) {
            throw new IllegalArgumentException("A lista de reordenação não pode ser nula ou vazia.");
        }

        List<Integer> ordens = novasOrdensPorId.values().stream()
                .filter(o -> o != null && o > 0)
                .collect(Collectors.toList());

        long ordensDistintas = ordens.stream().distinct().count();
        if (ordensDistintas != ordens.size()) {
            throw new IllegalArgumentException("Valores de ordem duplicados não são permitidos");
        }

        novasOrdensPorId.forEach((idString, novaOrdem) -> {
            if (novaOrdem == null || novaOrdem <= 0) {
                throw new IllegalArgumentException("A nova ordem deve ser um número positivo.");
            }

            EtapaId id = EtapaId.novo(idString);
            Etapa etapa = repository.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Etapa não encontrada"));

            etapa.alterarOrdem(novaOrdem);
            repository.editar(etapa);
        });
    }
}