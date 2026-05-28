package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.CancelMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.FindMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.ListMovementsPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterAjustePositivoPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterEntradaPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterMovementPortIn.RegisterMovementCommand;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.RegisterAjustePositivoRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.RegisterEntradaRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.RegistrarMovimientoRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper.MovimientoWebMapper;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import io.jsonwebtoken.Claims;
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
        private final ListMovementsPortIn listMovementsUseCase;
        private final FindMovementPortIn findMovementUseCase;
        private final CancelMovementPortIn cancelMovementUseCase;
        private final RegisterMovementPortIn registerMovementUseCase;
        private final MovimientoWebMapper mapper;

        @GetMapping
        public ResponseEntity<List<MovimientoListadoResponse>> listar(
                        @RequestParam(required = false) String tipo,
                        @RequestParam(required = false) String fechaDesde,
                        @RequestParam(required = false) String fechaHasta,
                        @RequestParam(required = false) UUID idProducto,
                        @RequestParam(required = false) String texto,
                        @RequestParam(defaultValue = "0") int pagina,
                        @RequestParam(defaultValue = "50") int tamanioPagina) {

                log.info("Listando movimientos - tipo: {}, fechaDesde: {}, fechaHasta: {}, texto: {}, pagina: {}, tamanioPagina: {}",
                                tipo, fechaDesde, fechaHasta, texto, pagina, tamanioPagina);

                LocalDate desde = (fechaDesde != null && !fechaDesde.trim().isEmpty())
                                ? LocalDate.parse(fechaDesde)
                                : null;
                LocalDate hasta = fechaHasta != null ? LocalDate.parse(fechaHasta) : null;
                String textoDecoded = texto != null ? URLDecoder.decode(texto, StandardCharsets.UTF_8) : null;

                List<MovimientoListadoResponse> movimientos = listMovementsUseCase.execute(
                                tipo, desde, hasta, idProducto, textoDecoded, pagina, tamanioPagina);

                return ResponseEntity.ok(movimientos);
        }

        @GetMapping("/{id}")
        public ResponseEntity<MovimientoListadoResponse> obtenerPorId(@PathVariable UUID id) {
                log.info("Obteniendo movimiento por ID: {}", id);

                MovimientoListadoResponse response = findMovementUseCase.execute(id)
                                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado: " + id));

                return ResponseEntity.ok(response);
        }

        @PostMapping
        public ResponseEntity<Void> registrar(
                        @Valid @RequestBody RegistrarMovimientoRequest request) {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                Long userId = auth != null && auth.getCredentials() instanceof Claims claims
                                ? claims.get("userId", Long.class)
                                : null;
                if (userId == null)
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                RegisterMovementCommand command = new RegisterMovementCommand(
                                request.tipo(),
                                request.idProducto(),
                                request.idLote(),
                                request.idLocacion() != null ? UUID.fromString(request.idLocacion()) : null,
                                request.cantidad(),
                                request.motivo(),
                                request.documentoRef(),
                                request.proveedor(),
                                request.nroLote(),
                                request.costoUnit(),
                                request.fecGarantia(),
                                userId);

                registerMovementUseCase.execute(command);

                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> anular(@PathVariable UUID id) {
                log.info("Anulando movimiento: {}", id);

                cancelMovementUseCase.execute(id);

                return ResponseEntity.noContent().build();
        }

        @PostMapping("/entrada")
        public ResponseEntity<ApiResponse<MovimientoResponse>> registrarEntrada(
                        @Valid @RequestBody RegisterEntradaRequest request) {

                log.info("Registrando entrada para lote: {}, cantidad: {}", request.idLote(), request.cantidad());

                Movimiento movimiento = registerEntradaPortIn.execute(
                                request.idLote(),
                                request.idUsuario(),
                                request.cantidad(),
                                request.motivo(),
                                request.docRef());

                MovimientoResponse response = mapper.toResponse(movimiento);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.ok("Entrada registrada exitosamente", response));
        }

        @PostMapping("/ajuste-positivo")
        public ResponseEntity<ApiResponse<MovimientoResponse>> registrarAjustePositivo(
                        @Valid @RequestBody RegisterAjustePositivoRequest request) {

                log.info("Registrando ajuste positivo para lote: {}, cantidad: {}", request.idLote(),
                                request.cantidad());

                Movimiento movimiento = registerAjustePositivoPortIn.execute(
                                request.idLote(),
                                request.idUsuario(),
                                request.cantidad(),
                                request.motivo(),
                                request.docRef());

                MovimientoResponse response = mapper.toResponse(movimiento);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.ok("Ajuste positivo registrado exitosamente", response));
        }

}
