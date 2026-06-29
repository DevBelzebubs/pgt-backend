package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.time.LocalDate;
import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;
import com.portable.shared.infrastructure.presentation.PagedResponse;

public interface ListMovementsPortIn {
    PagedResponse<MovimientoListadoResponse> execute(String tipo, LocalDate fechaDesde, LocalDate fechaHasta,
                                                      UUID idProducto, String texto, int pagina, int tamanioPagina);
}
