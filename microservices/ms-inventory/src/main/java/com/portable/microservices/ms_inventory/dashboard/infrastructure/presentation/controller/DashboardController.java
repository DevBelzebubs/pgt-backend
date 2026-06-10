package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardKpiResponse;
import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardMovementPointResponse;
import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardOperationResponse;
import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardProductTopResponse;
import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardStockAlertResponse;
import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardTipoProporcionResponse;
import com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto.DashboardZoneCapacityResponse;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository.LocationJpaRepository;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository.MovimientoJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    @PersistenceContext
    private EntityManager entityManager;

    private final MovimientoJpaRepository movimientoJpaRepository;
    private final LocationJpaRepository locationJpaRepository;

    @GetMapping("/kpis")
    public ResponseEntity<List<DashboardKpiResponse>> getKpis() {
        LocalDate now = LocalDate.now();

        long totalInventario = ((Number) entityManager.createNativeQuery(
            "SELECT COALESCE(SUM(sub.stock_actual), 0) FROM (" +
            "  SELECT DISTINCT ON (k.id_producto) k.stock_actual" +
            "  FROM inventory.kardex k" +
            "  ORDER BY k.id_producto, k.id_kardex DESC" +
            ") sub WHERE sub.stock_actual > 0"
        ).getSingleResult()).longValue();

        String totalInvFmt = totalInventario >= 1000
            ? String.format("%,d", totalInventario)
            : String.valueOf(totalInventario);

        long salidasMes = movimientoJpaRepository.countSalidasEsteMes();
        long salidasMesAnt = movimientoJpaRepository.countSalidasDelMes(
            now.minusMonths(1).getMonthValue(),
            now.minusMonths(1).getYear());
        double trendSalidas = salidasMesAnt > 0
            ? Math.round(((double) (salidasMes - salidasMesAnt) / salidasMesAnt) * 100.0) / 100.0
            : 0.0;

        long alertasStock = ((Number) entityManager.createNativeQuery(
            "SELECT COUNT(*) FROM (" +
            "  SELECT DISTINCT ON (k.id_producto) k.stock_actual, p.stock_minimo" +
            "  FROM inventory.kardex k" +
            "  JOIN inventory.producto p ON p.id_producto = k.id_producto" +
            "  WHERE p.estado = true" +
            "  ORDER BY k.id_producto, k.id_kardex DESC" +
            ") sub WHERE sub.stock_actual <= sub.stock_minimo"
        ).getSingleResult()).longValue();

        double trendAlertas = 0.0;

        BigDecimal valorAlmacenado = (BigDecimal) entityManager.createNativeQuery(
            "SELECT COALESCE(SUM(sub.stock_actual * sub.costo_prom), 0) FROM (" +
            "  SELECT DISTINCT ON (k.id_producto) k.stock_actual, k.costo_prom" +
            "  FROM inventory.kardex k" +
            "  ORDER BY k.id_producto, k.id_kardex DESC" +
            ") sub"
        ).getSingleResult();

        long valorLong = valorAlmacenado.setScale(0, RoundingMode.HALF_UP).longValue();
        String valorFmt;
        if (valorLong >= 1_000_000) {
            valorFmt = "$ " + String.format("%.1f", valorLong / 1_000_000.0) + "M";
        } else if (valorLong >= 1_000) {
            valorFmt = "$ " + String.format("%.1f", valorLong / 1_000.0) + "K";
        } else {
            valorFmt = "$ " + valorLong;
        }

        List<Integer> salidasSparkline = buildSalidasSparkline();

        List<DashboardKpiResponse> kpis = List.of(
            new DashboardKpiResponse("total-inventario", "Total Inventario",
                totalInventario, totalInvFmt,
                0.0, "+0.0%", true, "box",
                List.of(
                    (int)(totalInventario * 0.92),
                    (int)(totalInventario * 0.95),
                    (int)(totalInventario * 0.98),
                    (int)(totalInventario * 1.0),
                    (int)(totalInventario * 1.02),
                    (int)(totalInventario * 0.99),
                    (int)totalInventario
                )),
            new DashboardKpiResponse("salidas-mes", "Salidas del Mes",
                salidasMes, String.valueOf(salidasMes),
                trendSalidas, (trendSalidas >= 0 ? "+" : "") + String.format("%.1f", trendSalidas) + "%",
                trendSalidas >= 0, "trending-down",
                salidasSparkline),
            new DashboardKpiResponse("alertas-stock", "Alertas de Stock",
                alertasStock, String.valueOf(alertasStock),
                trendAlertas, (trendAlertas >= 0 ? "+" : "") + String.format("%.1f", trendAlertas) + "%",
                trendAlertas <= 0, "alert",
                List.of(
                    Math.max(1, (int)alertasStock - 2),
                    Math.max(1, (int)alertasStock - 1),
                    (int)alertasStock,
                    (int)alertasStock,
                    Math.max(1, (int)alertasStock - 1),
                    (int)alertasStock,
                    (int)alertasStock
                )),
            new DashboardKpiResponse("valor-almacenado", "Valor Almacenado",
                valorLong, valorFmt,
                0.0, "+0.0%", true, "dollar",
                List.of(
                    (int)(valorLong * 0.93),
                    (int)(valorLong * 0.96),
                    (int)(valorLong * 0.98),
                    (int)(valorLong * 1.0),
                    (int)(valorLong * 1.01),
                    (int)(valorLong * 1.0),
                    (int)valorLong
                ))
        );

        return ResponseEntity.ok(kpis);
    }

    private List<Integer> buildSalidasSparkline() {
        List<Integer> sparkline = new ArrayList<>();
        LocalDate seisMeses = LocalDate.now().minusMonths(6);
        List<Object[]> rows = movimientoJpaRepository.findMovementsGroupedByMonth(seisMeses);

        Map<String, Long> monthMap = new LinkedHashMap<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusMonths(i);
            String key = d.getYear() + "-" + String.format("%02d", d.getMonthValue());
            monthMap.put(key, 0L);
        }

        for (Object[] row : rows) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            String tipo = (String) row[2];
            long cnt = ((Number) row[3]).longValue();
            if ("SALIDA".equals(tipo)) {
                String key = anio + "-" + String.format("%02d", mes);
                monthMap.merge(key, cnt, Long::sum);
            }
        }

        for (Long val : monthMap.values()) {
            sparkline.add(val.intValue());
        }
        return sparkline;
    }

    @GetMapping("/stock-alerts")
    public ResponseEntity<List<DashboardStockAlertResponse>> getStockAlerts() {
        List<Object[]> rows = entityManager.createNativeQuery(
            "SELECT DISTINCT ON (k.id_producto) p.cod_prod, p.descripcion, k.stock_actual, p.stock_minimo, k.costo_prom " +
            "FROM inventory.kardex k " +
            "JOIN inventory.producto p ON p.id_producto = k.id_producto " +
            "WHERE p.estado = true " +
            "ORDER BY k.id_producto, k.id_kardex DESC"
        ).getResultList();

        List<DashboardStockAlertResponse> alerts = new ArrayList<>();
        for (Object[] row : rows) {
            int stockActual = ((Number) row[2]).intValue();
            int stockMinimo = ((Number) row[3]).intValue();
            if (stockActual <= stockMinimo) {
                String sku = (String) row[0];
                String name = (String) row[1];
                double costo = row[4] != null ? ((Number) row[4]).doubleValue() : 0.0;
                String status = stockActual <= 0 ? "Crítico" : "Bajo";
                alerts.add(new DashboardStockAlertResponse(sku, name, stockActual, status, costo));
            }
        }

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/movements-by-time")
    public ResponseEntity<List<DashboardMovementPointResponse>> getMovementsByTime() {
        LocalDate doceMeses = LocalDate.now().minusMonths(11).withDayOfMonth(1);
        List<Object[]> rows = movimientoJpaRepository.findMovementsGroupedByMonth(doceMeses);

        Map<String, DashboardMovementPointResponse> pointMap = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusMonths(i);
            String key = d.getYear() + "-" + String.format("%02d", d.getMonthValue());
            String label = d.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            label = label.substring(0, 1).toUpperCase() + label.substring(1, 3);
            pointMap.put(key, new DashboardMovementPointResponse(
                d.withDayOfMonth(1).toString(), label, 0, 0, 0));
        }

        for (Object[] row : rows) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            String tipo = (String) row[2];
            long cnt = ((Number) row[3]).longValue();

            String key = anio + "-" + String.format("%02d", mes);
            DashboardMovementPointResponse point = pointMap.get(key);
            if (point == null) continue;

            long ingresos = point.ingresos();
            long salidas = point.salidas();
            long ajustes = point.ajustes();

            switch (tipo) {
                case "INGRESO" -> ingresos += cnt;
                case "SALIDA" -> salidas += cnt;
                default -> ajustes += cnt;
            }
            pointMap.put(key, new DashboardMovementPointResponse(
                point.date(), point.label(), ingresos, salidas, ajustes));
        }

        return ResponseEntity.ok(new ArrayList<>(pointMap.values()));
    }

    @GetMapping("/proporcion-por-tipo")
    public ResponseEntity<List<DashboardTipoProporcionResponse>> getProporcionPorTipo() {
        List<Object[]> rows = movimientoJpaRepository.countGroupByTipo();

        Map<String, Long> merged = new HashMap<>();
        for (Object[] row : rows) {
            String tipo = (String) row[0];
            long cnt = ((Number) row[1]).longValue();
            if ("AJUSTE_POSITIVO".equals(tipo) || "AJUSTE_NEGATIVO".equals(tipo)) {
                merged.merge("AJUSTE", cnt, Long::sum);
            } else {
                merged.merge(tipo, cnt, Long::sum);
            }
        }

        Map<String, String> colors = Map.of(
            "INGRESO", "#34A853",
            "SALIDA", "#81000A",
            "AJUSTE", "#F5A623");

        List<DashboardTipoProporcionResponse> result = merged.entrySet().stream()
            .map(e -> new DashboardTipoProporcionResponse(
                e.getKey(), e.getValue(), colors.getOrDefault(e.getKey(), "#4C616C")))
            .sorted((a, b) -> Long.compare(b.value(), a.value()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent-operations")
    public ResponseEntity<List<DashboardOperationResponse>> getRecentOperations() {
        List<Object[]> rows = movimientoJpaRepository.findTop10RecentWithProduct();

        List<DashboardOperationResponse> ops = rows.stream().map(row -> {
            UUID id = (UUID) row[0];
            String tipo = (String) row[1];
            String product = (String) row[2];
            String dateStr = (String) row[3];
            Long userId = row[4] != null ? ((Number) row[4]).longValue() : null;

            return new DashboardOperationResponse(
                "MOV-" + id.toString().substring(0, 8).toUpperCase(),
                tipo,
                product,
                dateStr,
                userId != null ? "Usuario #" + userId : "Sistema",
                "COMPLETADO");
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ops);
    }

    @GetMapping("/zone-capacities")
    public ResponseEntity<List<DashboardZoneCapacityResponse>> getZoneCapacities() {
        List<Object[]> rows = locationJpaRepository.findZoneCapacities();

        List<DashboardZoneCapacityResponse> zones = rows.stream().map(row -> {
            String zona = (String) row[0];
            int capacidad = ((Number) row[1]).intValue();
            long used = ((Number) row[2]).longValue();
            double pct = capacidad > 0
                ? Math.round((double) used / capacidad * 100.0 * 100.0) / 100.0
                : 0.0;
            return new DashboardZoneCapacityResponse(zona, used, capacidad, pct);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(zones);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<DashboardProductTopResponse>> getTopProducts() {
        List<Object[]> rows = movimientoJpaRepository.findTopProductosSalidas();

        List<DashboardProductTopResponse> products = rows.stream()
            .map(row -> new DashboardProductTopResponse((String) row[0], ((Number) row[1]).longValue()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }
}
