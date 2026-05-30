package com.portable.microservices.ms_inventory.kardex.presentation.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.model.MetodoCosto;
import com.portable.microservices.ms_inventory.kardex.domain.service.KardexCostoSimulator;
import com.portable.microservices.ms_inventory.kardex.domain.service.KardexCostoSimulator.KardexEntry;
import com.portable.microservices.ms_inventory.kardex.presentation.dto.KardexResponse;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.product.domain.model.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KardexPresentationMapper {

    private final KardexCostoSimulator costSimulator;

    public List<KardexResponse> toResponseList(
            List<Kardex> kardexList,
            MetodoCosto metodo,
            Function<UUID, Movement> movementResolver,
            Function<UUID, Product> productResolver) {

        if (kardexList.isEmpty()) return List.of();

        Map<UUID, Movement> movMap = new HashMap<>();
        for (Kardex k : kardexList) {
            if (!movMap.containsKey(k.movimientoId())) {
                Movement m = movementResolver.apply(k.movimientoId());
                if (m != null) movMap.put(k.movimientoId(), m);
            }
        }

        Map<UUID, List<Kardex>> byProduct = kardexList.stream()
            .collect(Collectors.groupingBy(Kardex::productoId));

        Map<UUID, BigDecimal> costMap = new HashMap<>();
        for (List<Kardex> group : byProduct.values()) {
            List<KardexEntry> entries = new ArrayList<>();
            for (Kardex k : group) {
                Movement m = movMap.get(k.movimientoId());
                if (m != null) entries.add(new KardexEntry(k, m));
            }
            costMap.putAll(costSimulator.calcularCostos(entries, metodo));
        }

        return kardexList.stream()
            .map(k -> {
                Movement m = movMap.get(k.movimientoId());
                Product p = productResolver.apply(k.productoId());
                BigDecimal costo = costMap.getOrDefault(k.id(), k.costoProm());
                return buildResponse(k, m, p, metodo, costo);
            })
            .toList();
    }

    public KardexResponse toResponse(Kardex kardex, Movement movement, Product product) {
        return buildResponse(kardex, movement, product, MetodoCosto.PPP, kardex.costoProm());
    }

    private KardexResponse buildResponse(
            Kardex kardex, Movement movement, Product product,
            MetodoCosto metodo, BigDecimal costo) {
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
            costo,
            metodo
        );
    }
}