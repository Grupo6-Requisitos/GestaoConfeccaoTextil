package dev.com.linnea.infraestrutura.persistencia.jpa.modelo;

import dev.com.confectextil.dominio.principal.modelo.InsumoPadrao;
import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.modelo.ModeloId;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Entity
@Table(name = "modelo")
public class ModeloJpa {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(nullable = false)
    private String referencia;

    @Column(nullable = false)
    private String nome;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @ElementCollection
    @CollectionTable(name = "modelo_insumo_padrao", joinColumns = @JoinColumn(name = "modelo_id"))
    private List<InsumoPadraoJPA> insumosPadrao = new ArrayList<>();

    public ModeloJpa() {}

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public List<InsumoPadraoJPA> getInsumosPadrao() { return List.copyOf(insumosPadrao); }
    public void setInsumosPadrao(List<InsumoPadraoJPA> insumosPadrao) {
        this.insumosPadrao = insumosPadrao == null ? new ArrayList<>() : new ArrayList<>(insumosPadrao);
    }

    // ---------- conversões ----------
    public static ModeloJpa fromDomain(Modelo modelo) {
        Objects.requireNonNull(modelo, "Modelo de domínio não pode ser nulo.");
        ModeloJpa j = new ModeloJpa();

        // ModeloId é record ModeloId(String valor)
        ModeloId mid = modelo.getId();
        if (mid != null) {
            j.setId(mid.valor());
        }

        j.setReferencia(modelo.getReferencia());
        j.setNome(modelo.getNome());
        j.setImagemUrl(modelo.getImagemUrl());

        // converter InsumoPadrao -> InsumoPadraoJPA
        if (modelo.getInsumosPadrao() != null) {
            List<InsumoPadraoJPA> lista = modelo.getInsumosPadrao().stream()
                    .filter(Objects::nonNull)
                    .map(ip -> {
                        String insumoIdStr = extractStringId(ip.insumoId());
                        return new InsumoPadraoJPA(insumoIdStr, ip.quantidadeSugerida());
                    })
                    .collect(Collectors.toList());
            j.setInsumosPadrao(lista);
        }

        return j;
    }

    /**
     * Converte back para domínio. Se você tiver ModelMapper configurado, é possível
     * usar mapper.map(this, Modelo.class) em vez do fallback manual.
     */
    public Modelo toDomain(ModelMapper mapper) {
        if (mapper != null) {
            try {
                Modelo mapped = mapper.map(this, Modelo.class);
                if (mapped != null) return mapped;
            } catch (Exception ex) {
                // fallback manual abaixo
            }
        }

        // fallback manual: usa construtor completo que aceita ModeloId e lista de InsumoPadrao
        ModeloId modeloId = (this.id == null || this.id.isBlank()) ? null : new ModeloId(this.id);

        List<InsumoPadrao> insumos = this.insumosPadrao == null ? List.of() :
                this.insumosPadrao.stream()
                        .map(jip -> {
                            // InsumoId record não foi fornecido aqui; tentamos criar um objeto InsumoId via reflexão
                            Object insumoIdObj = tryCreateInsumoId(jip.getInsumoId());
                            if (insumoIdObj == null) {
                                // se não conseguirmos criar o InsumoId, tenta usar toString (isso pode ser adaptado)
                                return new InsumoPadrao(null, jip.getQuantidadeSugerida());
                            }
                            // InsumoPadrao aceita InsumoId (tipo exato). Aqui assumimos que InsumoId tem construtor(String)
                            return new InsumoPadrao((dev.com.confectextil.dominio.principal.insumo.InsumoId) insumoIdObj, jip.getQuantidadeSugerida());
                        })
                        .collect(Collectors.toList());

        if (modeloId != null) {
            return new Modelo(modeloId, this.referencia, this.nome, this.imagemUrl, insumos);
        } else {
            // usa construtor sem id
            Modelo m = new Modelo(this.referencia, this.nome, this.imagemUrl);
            // tenta setar insumos via setter
            try {
                m.setInsumosPadrao(insumos);
            } catch (Exception ignored) {}
            return m;
        }
    }

    // ---------- utilitários ----------
    private static String extractStringId(Object idLike) {
        if (idLike == null) return null;
        if (idLike instanceof String) return (String) idLike;
        // tenta métodos comuns: valor(), getValor(), getId(), toString()
        try {
            try {
                var m = idLike.getClass().getMethod("valor");
                Object r = m.invoke(idLike);
                if (r != null) return r.toString();
            } catch (NoSuchMethodException ignored) {}
            try {
                var m = idLike.getClass().getMethod("getValor");
                Object r = m.invoke(idLike);
                if (r != null) return r.toString();
            } catch (NoSuchMethodException ignored) {}
            try {
                var m = idLike.getClass().getMethod("getId");
                Object r = m.invoke(idLike);
                if (r != null) return r.toString();
            } catch (NoSuchMethodException ignored) {}
        } catch (Exception e) {
            // fallback
        }
        return idLike.toString();
    }

    /**
     * Tenta instanciar dev.com.confectextil.dominio.principal.insumo.InsumoId a partir de string.
     * Se não for possível (classe diferente), retorna null.
     */
    private static Object tryCreateInsumoId(String valor) {
        if (valor == null) return null;
        try {
            Class<?> cls = Class.forName("dev.com.confectextil.dominio.principal.insumo.InsumoId");
            // tenta construtor com String
            try {
                return cls.getConstructor(String.class).newInstance(valor);
            } catch (NoSuchMethodException ignored) {}
            // tenta factory método "novo"
            try {
                var m = cls.getMethod("novo", String.class);
                return m.invoke(null, valor);
            } catch (NoSuchMethodException ignored) {}
        } catch (ClassNotFoundException e) {
            // não existe a classe InsumoId no caminho; retorna null para fallback
        } catch (Exception ignored) {}
        return null;
    }

    // ---------- Embeddable para InsumoPadrao ----------
    @Embeddable
    public static class InsumoPadraoJPA {
        @Column(name = "insumo_id", length = 64)
        private String insumoId;

        @Column(name = "quantidade_sugerida")
        private double quantidadeSugerida;

        public InsumoPadraoJPA() {}

        public InsumoPadraoJPA(String insumoId, double quantidadeSugerida) {
            this.insumoId = insumoId;
            this.quantidadeSugerida = quantidadeSugerida;
        }

        public String getInsumoId() { return insumoId; }
        public void setInsumoId(String insumoId) { this.insumoId = insumoId; }

        public double getQuantidadeSugerida() { return quantidadeSugerida; }
        public void setQuantidadeSugerida(double quantidadeSugerida) { this.quantidadeSugerida = quantidadeSugerida; }
    }
}
