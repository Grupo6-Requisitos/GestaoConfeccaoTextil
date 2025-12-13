package dev.com.linnea.apresentacao;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import dev.com.confectextil.dominio.principal.Fabrico;
import dev.com.confectextil.dominio.principal.Modelo;
import dev.com.confectextil.dominio.principal.insumo.InsumoId;
import dev.com.confectextil.dominio.principal.modelo.InsumoPadrao;
import dev.com.linnea.apresentacao.principal.fabrico.FabricoDto;
import dev.com.linnea.apresentacao.principal.modelo.InsumoPadraoDto;
import dev.com.linnea.apresentacao.principal.modelo.ModeloDto;
import dev.com.confectextil.dominio.principal.fabrico.FabricoId;
import dev.com.confectextil.dominio.principal.modelo.ModeloId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Primary
public class BackendMapeador extends ModelMapper {

    public BackendMapeador() {
        addConverter(new AbstractConverter<FabricoDto, Fabrico>() {
            @Override
            protected Fabrico convert(FabricoDto source) {
                boolean possuiId = source.id != null && !source.id.isBlank();
                return possuiId
                    ? new Fabrico(FabricoId.novo(source.id), source.nomeFantasia, source.cnpj)
                    : new Fabrico(source.nomeFantasia, source.cnpj);
            }
        });

        addConverter(new AbstractConverter<String, FabricoId>() {
            @Override
            protected FabricoId convert(String source) {
                return FabricoId.novo(source);
            }
        });

        addConverter(new AbstractConverter<String, FabricoId>() {
            @Override
            protected FabricoId convert(String source) {
                return FabricoId.novo(source);
            }
        });

        addConverter(new AbstractConverter<ModeloDto, Modelo>() {
            @Override
            protected Modelo convert(ModeloDto source) {
                if (source == null) {
                    return null;
                }
                boolean possuiId = source.id != null && !source.id.isBlank();
                List<InsumoPadrao> insumos = mapInsumosDtoParaDominio(source.insumosPadrao);
                return possuiId
                    ? new Modelo(ModeloId.novo(source.id), source.referencia, source.nome, source.imagemUrl, insumos)
                    : new Modelo(source.referencia, source.nome, source.imagemUrl);
            }
        });

        addConverter(new AbstractConverter<Modelo, ModeloDto>() {
            @Override
            protected ModeloDto convert(Modelo source) {
                if (source == null) {
                    return null;
                }
                ModeloDto dto = new ModeloDto();
                dto.id = source.getId() != null ? source.getId().valor() : null;
                dto.referencia = source.getReferencia();
                dto.nome = source.getNome();
                dto.imagemUrl = source.getImagemUrl();
                dto.insumosPadrao = mapInsumosDominioParaDto(source.getInsumosPadrao());
                return dto;
            }
        });
    }

    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        return source != null ? super.map(source, destinationType) : null;
    }

    private List<InsumoPadrao> mapInsumosDtoParaDominio(List<InsumoPadraoDto> insumosPadrao) {
        if (insumosPadrao == null) {
            return List.of();
        }
        return insumosPadrao.stream()
            .filter(Objects::nonNull)
            .map(dto -> new InsumoPadrao(InsumoId.novo(dto.insumoId), dto.quantidadeSugerida))
            .collect(Collectors.toList());
    }

    private List<InsumoPadraoDto> mapInsumosDominioParaDto(List<InsumoPadrao> insumosPadrao) {
        if (insumosPadrao == null) {
            return List.of();
        }
        return insumosPadrao.stream()
            .filter(Objects::nonNull)
            .map(ip -> {
                InsumoPadraoDto dto = new InsumoPadraoDto();
                dto.insumoId = ip.insumoId() != null ? ip.insumoId().valor() : null;
                dto.quantidadeSugerida = ip.quantidadeSugerida();
                return dto;
            })
            .collect(Collectors.toList());
    }
}