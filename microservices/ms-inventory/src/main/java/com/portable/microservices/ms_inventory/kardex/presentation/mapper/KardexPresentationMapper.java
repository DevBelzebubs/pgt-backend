package com.portable.microservices.ms_inventory.kardex.presentation.mapper;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.presentation.dto.KardexResponse;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.product.domain.model.Product;

@Component
public class KardexPresentationMapper {
    public KardexResponse toResponse(Kardex kardex, Movement movement, Product product) {
        return new KardexResponse(
            kardex.id(),
            kardex.movimientoId(),
            kardex.productoId(),
            product != null ? product.descripcion() : null,
            product != null ? product.codProd() : null,
            movement != null && movement.fecha() != null ? movement.fecha().toString() : null,
            movement != null && movement.tipo() != null ? movement.tipo().name() : null,
            movement != null ? movement.docRef() : null,
            kardex.stockAnterior(),
            kardex.cantIngreso(),
            kardex.cantSalida(),
            kardex.stockActual(),
            kardex.costoProm()
        );
    }
}