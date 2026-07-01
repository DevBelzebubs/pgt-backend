package com.portable.microservices.ms_inventory.kardex.presentation.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;

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
import com.portable.microservices.ms_inventory.kardex.domain.model.MetodoCosto;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.ExportKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.FindKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.presentation.dto.KardexResponse;
import com.portable.microservices.ms_inventory.kardex.presentation.mapper.KardexPresentationMapper;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovementPersistencePortOut;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import com.portable.shared.infrastructure.presentation.PagedResponse;

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
    public ResponseEntity<PagedResponse<KardexResponse>> findAll(
            @RequestParam(required = false) UUID idProducto,
            @RequestParam(required = false) String tipoMovimiento,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) String texto,
            @RequestParam(defaultValue = "PPP") String metodoCosto,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamanioPagina) {
        MetodoCosto metodo = parseMetodo(metodoCosto);

        boolean hasFilters = StringUtils.hasText(tipoMovimiento)
                || StringUtils.hasText(fechaDesde)
                || StringUtils.hasText(fechaHasta)
                || StringUtils.hasText(texto);

        LocalDate desde = StringUtils.hasText(fechaDesde) ? LocalDate.parse(fechaDesde) : null;
        LocalDate hasta = StringUtils.hasText(fechaHasta) ? LocalDate.parse(fechaHasta) : null;

        PagedResponse<Kardex> page;
        if (idProducto != null) {
            page = findKardexUseCase.findByProductId(idProducto, pagina, tamanioPagina);
        } else if (hasFilters) {
            page = findKardexUseCase.findAllWithFilters(tipoMovimiento, desde, hasta, texto, pagina, tamanioPagina);
        } else {
            page = findKardexUseCase.findAll(pagina, tamanioPagina);
        }

        List<KardexResponse> items = presentationMapper.toResponseList(
                page.items(), metodo,
                id -> movementPersistence.findById(id).orElse(null),
                id -> productPersistence.findById(id).orElse(null),
                id -> findKardexUseCase.findByProductId(id));
        return ResponseEntity.ok(new PagedResponse<>(items, page.total(), page.page(), page.pageSize()));
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<PagedResponse<KardexResponse>> findByProducto(
            @PathVariable UUID idProducto,
            @RequestParam(defaultValue = "PPP") String metodoCosto,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamanioPagina) {
        MetodoCosto metodo = parseMetodo(metodoCosto);
        PagedResponse<Kardex> page = findKardexUseCase.findByProductId(idProducto, pagina, tamanioPagina);
        List<KardexResponse> items = presentationMapper.toResponseList(
                page.items(), metodo,
                id -> movementPersistence.findById(id).orElse(null),
                id -> productPersistence.findById(id).orElse(null),
                id -> findKardexUseCase.findByProductId(id));
        return ResponseEntity.ok(new PagedResponse<>(items, page.total(), page.page(), page.pageSize()));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> export(
            @RequestParam(defaultValue = "EXCEL") String format,
            @RequestParam(defaultValue = "PPP") String metodoCosto) {
        ExportKardexPortIn.ExportFormat exportFormat;
        try {
            exportFormat = ExportKardexPortIn.ExportFormat.valueOf(format.toUpperCase());
        } catch (IllegalArgumentException e) {
            exportFormat = ExportKardexPortIn.ExportFormat.EXCEL;
        }
        MetodoCosto metodo = parseMetodo(metodoCosto);
        Resource resource = exportKardexUseCase.exportToFormat(exportFormat, metodo);
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

    private static MetodoCosto parseMetodo(String value) {
        try {
            return MetodoCosto.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MetodoCosto.PPP;
        }
    }
}