package com.portable.microservices.ms_inventory.kardex.presentation.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.ExportKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.FindKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.presentation.dto.KardexResponse;
import com.portable.microservices.ms_inventory.kardex.presentation.mapper.KardexPresentationMapper;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovementPersistencePortOut;
import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/kardex")
@RequiredArgsConstructor
public class KardexController {
    private final FindKardexPortIn findKardexUseCase;
    private final MovementPersistencePortOut movementPersistence;
    private final ProductPersistencePortOut productPersistence;
    private final KardexPresentationMapper presentationMapper;
    private final ExportKardexPortIn exportKardexUseCase;

    @GetMapping
    public ResponseEntity<List<KardexResponse>> findAll(
            @RequestParam(required = false) UUID idProducto,
            @RequestParam(required = false) String tipoMovimiento) {
        List<Kardex> kardexList = (idProducto != null)
                ? findKardexUseCase.findByProductId(idProducto)
                : findKardexUseCase.findAll();
        List<KardexResponse> response = kardexList.stream()
                .map(k -> toResponse(k))
                .filter(r -> tipoMovimiento == null || tipoMovimiento.equals(r.tipoMovimiento()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<List<KardexResponse>> findByProducto(@PathVariable UUID idProducto) {
        List<KardexResponse> response = findKardexUseCase.findByProductId(idProducto).stream()
                .map(k -> toResponse(k)).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> export(
            @RequestParam(defaultValue = "EXCEL") String format) {
        ExportKardexPortIn.ExportFormat exportFormat;
        try {
            exportFormat = ExportKardexPortIn.ExportFormat.valueOf(format.toUpperCase());
        } catch (IllegalArgumentException e) {
            exportFormat = ExportKardexPortIn.ExportFormat.EXCEL;
        }
        Resource resource = exportKardexUseCase.exportToFormat(exportFormat);
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String extension = exportFormat == ExportKardexPortIn.ExportFormat.PDF ? "pdf" : "xlsx";
        String filename = "kardex-" + fecha + "." + extension;
        String mediaType = exportFormat == ExportKardexPortIn.ExportFormat.PDF
                ? "application/pdf"
                : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(mediaType))
                .body(resource);
    }

    // Helper
    private KardexResponse toResponse(Kardex k) {
        Movement movement = movementPersistence.findById(k.movimientoId()).orElse(null);
        Product product = productPersistence.findById(k.productoId()).orElse(null);
        return presentationMapper.toResponse(k, movement, product);
    }
}