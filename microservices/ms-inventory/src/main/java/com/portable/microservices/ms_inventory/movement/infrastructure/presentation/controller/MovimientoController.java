package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterAjustePositivoPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterEntradaPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterSalidaPortIn;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.RegisterAjustePositivoRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.RegisterEntradaRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.RegisterSalidaRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper.MovimientoWebMapper;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
@Slf4j
public class MovimientoController {

    private final RegisterEntradaPortIn registerEntradaPortIn;
    private final RegisterAjustePositivoPortIn registerAjustePositivoPortIn;
    private final RegisterSalidaPortIn registerSalidaPortIn;
    private final MovimientoWebMapper mapper;

    @PostMapping("/entrada")
    public ResponseEntity<ApiResponse<MovimientoResponse>> registrarEntrada(
            @Valid @RequestBody RegisterEntradaRequest request) {

        log.info("Registrando entrada para lote: {}, cantidad: {}", request.idLote(), request.cantidad());

        Movimiento movimiento = registerEntradaPortIn.execute(
                request.idLote(),
                request.idUsuario(),
                request.cantidad(),
                request.motivo(),
                request.docRef()
        );

        MovimientoResponse response = mapper.toResponse(movimiento);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Entrada registrada exitosamente", response));
    }

    @PostMapping("/salida")
    public ResponseEntity<ApiResponse<MovimientoResponse>> registrarSalida(
            @Valid @RequestBody RegisterSalidaRequest request) {

        log.info("Registrando salida para lote: {}, cantidad: {}", request.idLote(), request.cantidad());

        Movimiento movimiento = registerSalidaPortIn.execute(
                request.idLote(),
                request.idUsuario(),
                request.cantidad(),
                request.motivo(),
                request.docRef()
        );

        MovimientoResponse response = mapper.toResponse(movimiento);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Salida registrada exitosamente", response));
    }

    @PostMapping("/ajuste-positivo")
    public ResponseEntity<ApiResponse<MovimientoResponse>> registrarAjustePositivo(
            @Valid @RequestBody RegisterAjustePositivoRequest request) {

        log.info("Registrando ajuste positivo para lote: {}, cantidad: {}", request.idLote(), request.cantidad());

        Movimiento movimiento = registerAjustePositivoPortIn.execute(
                request.idLote(),
                request.idUsuario(),
                request.cantidad(),
                request.motivo(),
                request.docRef()
        );

        MovimientoResponse response = mapper.toResponse(movimiento);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Ajuste positivo registrado exitosamente", response));
    }
}
