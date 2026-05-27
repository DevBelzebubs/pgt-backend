package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;

public interface ListMovementsPortIn {
    List<MovimientoListadoResponse> execute(String tipo, LocalDate fechaDesde, LocalDate fechaHasta,
                                             UUID idProducto, String texto, int pagina, int tamanioPagina);

    long count(String tipo, LocalDate fechaDesde, LocalDate fechaHasta, UUID idProducto, String texto);
}
