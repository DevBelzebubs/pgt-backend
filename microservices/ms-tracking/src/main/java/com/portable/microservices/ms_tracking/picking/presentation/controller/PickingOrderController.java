package com.portable.microservices.ms_tracking.picking.presentation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_tracking.picking.domain.ports.in.AsignarOrdenPickPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.CompletarOrdenPickPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.CrearOrdenPickPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.ListarOrdenesPickPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.ObtenerOrdenPickPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.OptimizarRutaPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.out.RutaPickPersistencePortOut;
import com.portable.microservices.ms_tracking.picking.presentation.dto.CrearOrdenPickRequest;
import com.portable.microservices.ms_tracking.picking.presentation.dto.OrdenPickResponse;
import com.portable.microservices.ms_tracking.picking.presentation.dto.RutaResponse;
import com.portable.microservices.ms_tracking.picking.presentation.mapper.OrdenPickWebMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/picking/orders")
@RequiredArgsConstructor
public class PickingOrderController {
    private final CrearOrdenPickPortIn crearOrdenPick;
    private final AsignarOrdenPickPortIn asignarOrdenPick;
    private final CompletarOrdenPickPortIn completarOrdenPick;
    private final OptimizarRutaPortIn optimizarRuta;
    private final ObtenerOrdenPickPortIn obtenerOrdenPick;
    private final ListarOrdenesPickPortIn listarOrdenesPick;
    private final OrdenPickWebMapper mapper;
    private final RutaPickPersistencePortOut rutaPickPersistence;
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody CrearOrdenPickRequest request) {
        var domain = mapper.toDomain(request.getItems());
        var orden = crearOrdenPick.execute(request.getUsuarioCreador(), domain, request.getTipoSalida(), request.getDocRef());
        var response = mapper.toResponse(orden);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Orden de picking creada exitosamente",
                "data", response
        ));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(required = false) String estado) {
        List<OrdenPickResponse> ordenes;
        if (estado != null) {
            ordenes = listarOrdenesPick.executeByEstado(estado)
                    .stream().map(mapper::toResponse).toList();
        } else {
            ordenes = listarOrdenesPick.execute()
                    .stream().map(mapper::toResponse).toList();
        }
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Lista de órdenes obtenida",
                "data", ordenes
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable UUID id) {
        var orden = obtenerOrdenPick.execute(id);
        var response = mapper.toResponse(orden);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Orden encontrada",
                "data", response
        ));
    }

    @PostMapping("/{id}/optimize")
    public ResponseEntity<Map<String, Object>> optimizar(@PathVariable UUID id) {
        var ruta = optimizarRuta.execute(id);
        var response = new RutaResponse(ruta.pathSeq(), ruta.distanciaEstimada());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Ruta optimizada calculada",
                "data", response
        ));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Map<String, Object>> asignar(
            @PathVariable UUID id,
            @RequestBody Map<String, Long> body) {
        var orden = asignarOrdenPick.execute(id, body.get("usuarioPickingId"));
        var response = mapper.toResponse(orden);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Operario asignado exitosamente",
                "data", response
        ));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completar(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        String ipOrigen = (String) body.getOrDefault("ipOrigen", "0.0.0.0");
        var orden = completarOrdenPick.execute(id, usuarioId, ipOrigen);
        var response = mapper.toResponse(orden);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Orden completada exitosamente",
                "data", response
        ));
    }

    @GetMapping("/{id}/route")
    public ResponseEntity<Map<String, Object>> obtenerRuta(@PathVariable UUID id) {
        var rutaOpt = rutaPickPersistence.findByIdOrden(id);
        if (rutaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var ruta = rutaOpt.get();
        var response = new RutaResponse(ruta.pathSeq(), ruta.distanciaEstimada());
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("message", "Ruta obtenida");
        body.put("data", response);
        return ResponseEntity.ok(body);
    }
}
