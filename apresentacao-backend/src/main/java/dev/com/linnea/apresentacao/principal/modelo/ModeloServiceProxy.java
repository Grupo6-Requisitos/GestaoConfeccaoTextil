package dev.com.linnea.apresentacao.principal.modelo;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.insumo.InsumoRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloRepository;
import dev.com.confectextil.dominio.principal.modelo.ModeloService;
import java.util.List;

@Service
@Primary
public class ModeloServiceProxy extends ModeloService {

    public ModeloServiceProxy(ModeloRepository modeloRepository, InsumoRepository insumoRepository) {
        super(modeloRepository, insumoRepository);
    }

    @Override
    public Modelo atualizarModelo(String referenciaAlvo, String novaReferencia, String novoNome, List<InsumoPadraoDTO> novosInsumos) {
        System.out.println("[AUDIT] Editando modelo " + referenciaAlvo);
        Modelo result = super.atualizarModelo(referenciaAlvo, novaReferencia, novoNome, novosInsumos);
        System.out.println("[AUDIT] Modelo " + referenciaAlvo + " editado com sucesso");
        return result;
    }

    @Override
    public Modelo atualizarModelo(String referenciaAlvo, String novaReferencia, String novoNome) {
        System.out.println("[AUDIT] Editando modelo " + referenciaAlvo);
        Modelo result = super.atualizarModelo(referenciaAlvo, novaReferencia, novoNome);
        System.out.println("[AUDIT] Modelo " + referenciaAlvo + " editado com sucesso");
        return result;
    }
}
