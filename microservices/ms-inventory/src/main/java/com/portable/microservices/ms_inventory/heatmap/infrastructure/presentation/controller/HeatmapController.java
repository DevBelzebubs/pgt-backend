package com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto.HeatmapAlmacenResponse;
import com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto.HeatmapAlmacenResponse.AlmacenInfo;
import com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto.HeatmapLocationDetailResponse;
import com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto.HeatmapLocationResponse;
import com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto.LocationProductResponse;
import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.GetWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository.LocationJpaRepository;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.repository.LoteJpaRepository;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository.MovimientoJpaRepository;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/heatmap")
@RequiredArgsConstructor
@Slf4j
public class HeatmapController {

    private final LocationJpaRepository locationJpaRepository;
    private final LoteJpaRepository loteJpaRepository;
    private final MovimientoJpaRepository movimientoJpaRepository;
    private final GetWarehousePortIn getWarehousePortIn;

    @GetMapping("/almacen/{idAlmacen}")
    public ResponseEntity<ApiResponse<HeatmapAlmacenResponse>> getHeatmapByAlmacen(
            @PathVariable Long idAlmacen) {
        Warehouse warehouse = getWarehousePortIn.execute(idAlmacen);

        List<Object[]> rows = movimientoJpaRepository.findHeatmapByAlmacen(idAlmacen);

        List<HeatmapLocationResponse> locaciones = new ArrayList<>();
        long maxStock = 0;
        for (Object[] row : rows) {
            long stockQty = ((Number) row[5]).longValue();
            if (stockQty > maxStock) {
                maxStock = stockQty;
            }
        }

        for (Object[] row : rows) {
            UUID idLocacion = (UUID) row[0];
            String zona = (String) row[1];
            String pasillo = (String) row[2];
            String estante = (String) row[3];
            String codBarras = (String) row[4];
            long stockQty = ((Number) row[5]).longValue();
            long dailyPicks = ((Number) row[6]).longValue();
            String categoria = (String) row[7];

            int capacity = 0;
            if (row.length > 8 && row[8] != null) {
                capacity = ((Number) row[8]).intValue();
            }

            int intensity = calculateIntensity(stockQty, capacity, maxStock);

            locaciones.add(new HeatmapLocationResponse(
                    idLocacion, zona, pasillo, estante, codBarras,
                    capacity, stockQty, dailyPicks, intensity, categoria));
        }

        var almacenInfo = new AlmacenInfo(warehouse.id(), warehouse.nombre(), warehouse.codAlm());
        var response = new HeatmapAlmacenResponse(almacenInfo, locaciones);
        return ResponseEntity.ok(ApiResponse.ok("Mapa de calor obtenido exitosamente", response));
    }

    @GetMapping("/location/{idLocacion}")
    public ResponseEntity<ApiResponse<HeatmapLocationDetailResponse>> getLocationDetail(
            @PathVariable UUID idLocacion) {

        LocationJpaEntity location = locationJpaRepository.findById(idLocacion)
                .orElseThrow(() -> new IllegalArgumentException("Locación no encontrada: " + idLocacion));

        long stockQty = loteJpaRepository.getTotalQtyByLocation(idLocacion);
        long dailyPicks = movimientoJpaRepository.countDailyMovementsByLocation(idLocacion);

        List<Object[]> productRows = loteJpaRepository.findProductosByLocacion(idLocacion);
        List<LocationProductResponse> productos = productRows.stream().map(p -> new LocationProductResponse(
                (UUID) p[0],
                (String) p[1],
                (String) p[2],
                ((Number) p[3]).intValue(),
                (String) p[4])).collect(Collectors.toList());

        int intensity = calculateIntensity(stockQty,
                location.getCapacidad() != null ? location.getCapacidad() : 0, stockQty);

        String categoria = productos.isEmpty() ? "" : productos.get(0).productoDesc();

        var response = new HeatmapLocationDetailResponse(
                location.getIdLocacion(),
                location.getZona(),
                location.getPasillo(),
                location.getEstante(),
                location.getCodBarras(),
                location.getCapacidad(),
                stockQty,
                dailyPicks,
                intensity,
                categoria,
                productos);

        return ResponseEntity.ok(ApiResponse.ok("Detalle de locación obtenido", response));
    }

    private int calculateIntensity(long movementCount, int capacity, long maxMovement) {
        if (movementCount == 0) return 0;

        if (capacity > 0) {
            int pct = (int) Math.min(100, Math.round((double) movementCount / capacity * 100));
            return Math.max(0, Math.min(100, pct));
        }

        if (maxMovement > 0) {
            int pct = (int) Math.round((double) movementCount / maxMovement * 100);
            return Math.max(1, Math.min(100, pct));
        }

        return (int) Math.min(100, movementCount);
    }
}
