package com.portable.microservices.ms_inventory.kardex.application.usecases;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.portable.microservices.ms_inventory.kardex.application.dto.KardexExportRow;
import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.ExportKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.domain.ports.out.KardexPersistencePortOut;
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
    @Override
    public Resource exportToFormat(ExportFormat format) {
        List<KardexExportRow> rows = buildExportRows();
        return switch (format) {
            case EXCEL -> excelGenerator.generateKardexExcel(rows);
            case PDF -> pdfGenerator.generateKardexPdf(rows);
        };
    }
    private List<KardexExportRow> buildExportRows() {
        return kardexPersistence.findAll().stream()
                .map(this::toExportRow)
                .toList();
    }
    private KardexExportRow toExportRow(Kardex k) {
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
                k.costoProm()
        );
    }
}
