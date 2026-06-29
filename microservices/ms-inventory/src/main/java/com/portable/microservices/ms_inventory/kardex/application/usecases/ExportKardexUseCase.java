package com.portable.microservices.ms_inventory.kardex.application.usecases;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.portable.microservices.ms_inventory.kardex.application.dto.KardexExportRow;
import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.model.MetodoCosto;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.ExportKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.domain.ports.out.KardexPersistencePortOut;
import com.portable.microservices.ms_inventory.kardex.domain.service.KardexCostoSimulator;
import com.portable.microservices.ms_inventory.kardex.domain.service.KardexCostoSimulator.KardexEntry;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovementPersistencePortOut;
import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import com.portable.microservices.ms_inventory.shared.application.service.ExcelGeneratorService;
import com.portable.microservices.ms_inventory.shared.application.service.PdfGeneratorService;

@Service
@RequiredArgsConstructor
public class ExportKardexUseCase implements ExportKardexPortIn {
    private final KardexPersistencePortOut kardexPersistence;
    private final MovementPersistencePortOut movementPersistence;
    private final ProductPersistencePortOut productPersistence;
    private final ExcelGeneratorService excelGenerator;
    private final PdfGeneratorService pdfGenerator;
    private final KardexCostoSimulator costSimulator;

    @Override
    public Resource exportToFormat(ExportFormat format, MetodoCosto metodo) {
        List<KardexExportRow> rows = buildExportRows(metodo);
        return switch (format) {
            case EXCEL -> excelGenerator.generateKardexExcel(rows);
            case PDF -> pdfGenerator.generateKardexPdf(rows);
        };
    }

    private List<KardexExportRow> buildExportRows(MetodoCosto metodo) {
        List<Kardex> all = kardexPersistence.findAll();
        Map<UUID, BigDecimal> costMap = buildCostMap(all, metodo);
        return all.stream()
                .map(k -> toExportRow(k, costMap.getOrDefault(k.id(), k.costoProm())))
                .toList();
    }

    private Map<UUID, BigDecimal> buildCostMap(List<Kardex> all, MetodoCosto metodo) {
        if (metodo == MetodoCosto.PPP)
            return Map.of();

        Map<UUID, List<Kardex>> byProduct = all.stream()
                .collect(Collectors.groupingBy(Kardex::productoId));
        Map<UUID, BigDecimal> costMap = new HashMap<>();

        for (List<Kardex> group : byProduct.values()) {
            List<KardexEntry> entries = new ArrayList<>();
            for (Kardex k : group) {
                Movement m = movementPersistence.findById(k.movimientoId()).orElse(null);
                entries.add(new KardexEntry(k, m));
            }
            costMap.putAll(costSimulator.calcularCostos(entries, metodo));
        }
        return costMap;
    }

    private KardexExportRow toExportRow(Kardex k, BigDecimal costo) {
        Movement movement = movementPersistence.findById(k.movimientoId()).orElse(null);
        Product product = productPersistence.findById(k.productoId()).orElse(null);
        return new KardexExportRow(
                movement != null && movement.fecha() != null ? movement.fecha().toString() : "",
                product != null ? product.descripcion() : "",
                product != null ? product.codProd() : "",
                movement != null && movement.tipo() != null ? movement.tipo().name() : "",
                movement != null ? movement.docRef() : "",
                k.stockAnterior(),
                k.cantIngreso(),
                k.cantSalida(),
                k.stockActual(),
                costo);
    }
}
