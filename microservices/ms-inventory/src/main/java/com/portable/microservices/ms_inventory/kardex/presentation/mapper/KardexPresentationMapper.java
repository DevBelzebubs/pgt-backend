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
        return toResponseList(kardexList, metodo, movementResolver, productResolver, null);
    }

    public List<KardexResponse> toResponseList(
            List<Kardex> kardexList,
            MetodoCosto metodo,
            Function<UUID, Movement> movementResolver,
            Function<UUID, Product> productResolver,
            Function<UUID, List<Kardex>> allEntriesResolver) {

        if (kardexList.isEmpty()) return List.of();

        Map<UUID, Movement> movMap = new HashMap<>();
        for (Kardex k : kardexList) {
            if (!movMap.containsKey(k.movimientoId())) {
                Movement m = movementResolver.apply(k.movimientoId());
                if (m != null) movMap.put(k.movimientoId(), m);
            }
        }

        Map<UUID, BigDecimal> costMap;

        if (metodo == MetodoCosto.PPP || allEntriesResolver == null) {
            costMap = calcularCostosPaginados(kardexList, metodo, movMap);
        } else {
            costMap = calcularCostosCompletos(kardexList, metodo, movementResolver, allEntriesResolver);
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

    private Map<UUID, BigDecimal> calcularCostosPaginados(
            List<Kardex> kardexList, MetodoCosto metodo, Map<UUID, Movement> movMap) {
        Map<UUID, List<Kardex>> byProduct = kardexList.stream()
            .collect(Collectors.groupingBy(Kardex::productoId));
        Map<UUID, BigDecimal> costMap = new HashMap<>();
        for (List<Kardex> group : byProduct.values()) {
            List<KardexEntry> entries = new ArrayList<>();
            for (Kardex k : group) {
                entries.add(new KardexEntry(k, movMap.get(k.movimientoId())));
            }
            costMap.putAll(costSimulator.calcularCostos(entries, metodo));
        }
        return costMap;
    }

    private Map<UUID, BigDecimal> calcularCostosCompletos(
            List<Kardex> kardexList,
            MetodoCosto metodo,
            Function<UUID, Movement> movementResolver,
            Function<UUID, List<Kardex>> allEntriesResolver) {

        Map<UUID, List<Kardex>> byProduct = kardexList.stream()
            .collect(Collectors.groupingBy(Kardex::productoId));

        Map<UUID, Movement> fullMovMap = new HashMap<>();
        Map<UUID, BigDecimal> costMap = new HashMap<>();

        for (List<Kardex> group : byProduct.values()) {
            UUID productId = group.get(0).productoId();
            List<Kardex> allEntries = allEntriesResolver.apply(productId);
            if (allEntries == null || allEntries.isEmpty()) {
                allEntries = group;
            }

            for (Kardex k : allEntries) {
                if (!fullMovMap.containsKey(k.movimientoId())) {
                    Movement m = movementResolver.apply(k.movimientoId());
                    if (m != null) fullMovMap.put(k.movimientoId(), m);
                }
            }

            List<KardexEntry> entries = new ArrayList<>();
            for (Kardex k : allEntries) {
                entries.add(new KardexEntry(k, fullMovMap.get(k.movimientoId())));
            }
            costMap.putAll(costSimulator.calcularCostos(entries, metodo));
        }
        return costMap;
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