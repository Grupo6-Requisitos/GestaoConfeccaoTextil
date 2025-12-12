package dev.com.confectextil.dominio.principal.etapa;

import dev.com.confectextil.dominio.principal.Etapa;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EtapaService {

    private final EtapaRepository repository;
    private List<EtapaStrategy> estrategias; // Mantemos, mas agora é opcional

    // 🔥 Construtor que os testes esperam
    public EtapaService(EtapaRepository repository) {
        this.repository = repository;
        this.estrategias = List.of(); // vazio por padrão para não quebrar
    }

    // 🔥 Construtor opcional para quando você usar estratégias na aplicação real
    public EtapaService(EtapaRepository repository, List<EtapaStrategy> estrategias) {
        this.repository = repository;
        this.estrategias = estrategias != null ? estrategias : List.of();
    }

    // 🔥 Método usado pelos testes — sem “tipo”
    public Etapa cadastrarEtapa(String etapaId, String nome, int ordem) {
        EtapaId id = EtapaId.novo(etapaId);

        if (repository.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Etapa já cadastrada");
        }

        Etapa etapa = new Etapa(id, nome, ordem);

        // aplica estratégia **somente se houver**
        aplicarEstrategias(etapa);

        repository.salvar(etapa);
        return etapa;
    }

    // 🔥 Seu método original com "tipo" continua disponível
    public Etapa cadastrarEtapa(String etapaId, String nome, int ordem, String tipo) {
        EtapaId id = EtapaId.novo(etapaId);

        if (repository.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Etapa já cadastrada");
        }

        Etapa etapa = new Etapa(id, nome, ordem);

        // aplica estratégia pelo tipo
        EtapaStrategy estrategia = estrategias.stream()
                .filter(e -> e.seAplica(tipo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de etapa inválido: " + tipo));

        estrategia.processarRegras(etapa);

        repository.salvar(etapa);
        return etapa;
    }

    // 🔥 Estratégias opcionais
    private void aplicarEstrategias(Etapa etapa) {
        if (estrategias == null || estrategias.isEmpty()) return;

        estrategias.forEach(e -> {
            if (e.seAplica(null)) {
                e.processarRegras(etapa);
            }
        });
    }

    public Etapa editarEtapa(String etapaId, String novoNome, Integer novaOrdem) {
        EtapaId id = EtapaId.novo(etapaId);

        Etapa etapa = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Etapa não encontrada"));

        etapa.atualizar(novoNome, novaOrdem);
        return repository.editar(etapa);
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

    public boolean existe(String etapaId) {
        EtapaId id = EtapaId.novo(etapaId);
        return repository.buscarPorId(id).isPresent();
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

    public List<Etapa> listarTodos() {
        return repository.listarTodos();
    }
}
