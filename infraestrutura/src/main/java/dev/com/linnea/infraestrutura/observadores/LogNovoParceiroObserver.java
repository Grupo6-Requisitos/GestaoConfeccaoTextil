package dev.com.linnea.infraestrutura.observadores;

import dev.com.confectextil.dominio.principal.Parceiro;
import dev.com.confectextil.dominio.principal.parceiro.ObservadorNovoParceiro;
import org.springframework.stereotype.Component;

@Component
public class LogNovoParceiroObserver implements ObservadorNovoParceiro {

    @Override
    public void executadoAposCadastro(Parceiro parceiroSalvo) {
        System.out.println("====================================");
        System.out.println("Novo parceiro cadastrado!");
        System.out.println("ID: " + parceiroSalvo.getId().getId());
        System.out.println("Nome: " + parceiroSalvo.getNome());
        System.out.println("====================================");
    }
}