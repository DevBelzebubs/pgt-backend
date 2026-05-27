package com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;

    public ProductJpaEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }

        return ProductJpaEntity.builder()
                .id_producto(product.id())
                .id_categoria(categoryMapper.toEntityById(product.categoryId()))
                .id_marca(brandMapper.toEntityById(product.brandId()))
                .cod_prod(product.codProd())
                .cod_anexo(product.codAnexo())
                .descripcion(product.descripcion())
                .modelos_compatibles(product.modelosCompatibles())
                .pre_com(product.preCom())
                .pre_ven(product.preVen())
                .estado(product.estado())
                .fec_creacion(toOffsetDateTime(product.fecCreacion()))
                .stock_minimo(0)
                .build();
    }

    public Product toDomain(ProductJpaEntity entity, Integer stockTotal) {
        if (entity == null) return null;
        return new Product(
                entity.getId_producto(),
                entity.getId_categoria() != null ? entity.getId_categoria().getId() : null,
                entity.getId_marca() != null ? entity.getId_marca().getId() : null,
                entity.getCod_prod(),
                entity.getCod_anexo(),
                entity.getDescripcion(),
                entity.getModelos_compatibles(),
                entity.getPre_com(),
                entity.getPre_ven(),
                entity.getEstado() != null ? entity.getEstado() : false,
                entity.getFec_creacion() != null ? entity.getFec_creacion().toZonedDateTime() : null,
                stockTotal
        );
    }

    private OffsetDateTime toOffsetDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.toOffsetDateTime();
    }

    private ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault());
    }
}