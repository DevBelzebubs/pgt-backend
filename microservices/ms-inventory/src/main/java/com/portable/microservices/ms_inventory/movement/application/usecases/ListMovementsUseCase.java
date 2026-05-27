package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.movement.domain.ports.in.ListMovementsPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper.MovimientoWebMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListMovementsUseCase implements ListMovementsPortIn {

    private final MovimientoPersistencePortOut persistence;
    private final MovimientoWebMapper mapper;

    @Override
    public List<MovimientoListadoResponse> execute(String tipo, LocalDate fechaDesde, LocalDate fechaHasta,
            UUID idProducto, String texto, int pagina, int tamanioPagina) {
        String textoSeguro = (texto == null) ? "" : texto;
        return persistence.findAllWithFilters(tipo, fechaDesde, fechaHasta, idProducto, textoSeguro, pagina, tamanioPagina)
                .stream()
                .map(mapper::toListadoResponse)
                .toList();
    }

    @Override
    public long count(String tipo, LocalDate fechaDesde, LocalDate fechaHasta, UUID idProducto, String texto) {
        return persistence.countAllWithFilters(tipo, fechaDesde, fechaHasta, idProducto, texto);
    }
}
